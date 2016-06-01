package com.pkit.struts2.result;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author HOK
 *
 */
public class PlainTextResult extends StrutsResultSupport{

	private final Log log = LogFactory.getLog(getClass());

	private static final String DEFAULT_CONTENT_TYPE = "text/plain";
	private String root = "text";
	private boolean noCache = false;
	private boolean enableGZIP = false;
    private String contentType;
    private String defaultEncoding = "UTF-8";
	
	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {
		ActionContext actionContext = invocation.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);
		try {
			Object rootObject;
			if(this.root != null) {
				ValueStack stack = invocation.getStack();
				rootObject = stack.findValue(this.root);
			} else {
				rootObject = invocation.getAction();
			}
			boolean writeGzip = enableGZIP && isGzipInRequest(request);
			writeToResponse(response, String.valueOf(rootObject), writeGzip);
		} catch (IOException exception) {
			log.error(exception.getMessage(), exception);
            throw exception;
		}
	}

	private static boolean isGzipInRequest(HttpServletRequest request) {
        String header = request.getHeader("Accept-Encoding");
        return (header != null) && (header.indexOf("gzip") >= 0);
    }
	 protected void writeToResponse(HttpServletResponse response, String text,boolean gzip) throws IOException {
		writeTextToResponse(response, getEncoding(), text, gzip, noCache,
		StringUtils.defaultString(contentType, DEFAULT_CONTENT_TYPE));
	}

	private static void writeTextToResponse(HttpServletResponse response,String encoding, String text, boolean gzip, boolean noCache,String contentType) throws IOException {
		response.setContentType(contentType + ";charset=" + encoding);
		if (noCache) {
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Expires", "0");
			response.setHeader("Pragma", "No-cache");
		}

		if (gzip) {
			response.addHeader("Content-Encoding", "gzip");
			GZIPOutputStream out = null;
			InputStream in = null;
			try {
				out = new GZIPOutputStream(response.getOutputStream());
				in = new ByteArrayInputStream(text.getBytes());
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			} finally {
				if (in != null)
					in.close();
				if (out != null) {
					out.finish();
					out.close();
				}
			}
		} else {
			response.setContentLength(text.getBytes(encoding).length);
			PrintWriter out = response.getWriter();
			out.print(text);
			out.flush();
			out.close();
		}
	}
	
	/**
     * Retrieve the encoding <p/>
     * 
     * @return The encoding associated with this template (defaults to the value
     *         of 'struts.i18n.encoding' property)
     */
    protected String getEncoding() {
        String encoding = this.defaultEncoding;

        if (encoding == null) {
            encoding = System.getProperty("file.encoding");
        }

        if (encoding == null) {
            encoding = "UTF-8";
        }

        return encoding;
    }
    
	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public boolean isNoCache() {
		return noCache;
	}

	public void setNoCache(boolean noCache) {
		this.noCache = noCache;
	}

	public boolean isEnableGZIP() {
		return enableGZIP;
	}

	public void setEnableGZIP(boolean enableGZIP) {
		this.enableGZIP = enableGZIP;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

}
