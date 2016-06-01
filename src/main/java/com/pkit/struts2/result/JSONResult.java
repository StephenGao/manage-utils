package com.pkit.struts2.result;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.pkit.util.JSONUtils;
import com.pkit.util.XmlUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author HOK
 */
public class JSONResult extends StrutsResultSupport{
    private final Log log=LogFactory.getLog(this.getClass());

	private static final String DEFAULT_CONTENT_TYPE = "text/json";
	private String root = "json";
	private boolean noCache = false;
	private boolean enableGZIP = false;
	private String contentType;
	private String defaultEncoding = "UTF-8";

    @Inject(StrutsConstants.STRUTS_I18N_ENCODING)
    public void setDefaultEncoding(String val) {
        this.defaultEncoding = val;
    }
	
	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation)throws Exception {
        ActionContext actionContext = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);
        
        String json="";
        Object rootObject;
        if (this.root != null) {
            ValueStack stack = invocation.getStack();
            rootObject = stack.findValue(this.root);
        } else {
            rootObject = invocation.getAction();
        }    
        json= JSONUtils.object2json(rootObject, "yyyy-MM-dd HH:mm:ss");
        boolean writeGzip = enableGZIP && XmlUtils.isGzipInRequest(request);
        writeToResponse(response, json, writeGzip);
	}
	
	
    public static boolean isGzipInRequest(HttpServletRequest request) {
        String header = request.getHeader("Accept-Encoding");
        return (header != null) && (header.indexOf("gzip") >= 0);
    }
    
    protected void writeToResponse(HttpServletResponse response, String json,
			boolean gzip) throws IOException {
    	writeJSONToResponse(response, getEncoding(), json,gzip,
				noCache, StringUtils.defaultString(contentType,
						DEFAULT_CONTENT_TYPE));
	}
    
    
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
    
    
    
    public static void writeJSONToResponse(HttpServletResponse response, 
            String encoding, String json,boolean gzip, boolean noCache, 
            String contentType) throws IOException {
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
                in = new ByteArrayInputStream(json.getBytes());
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
            response.setContentLength(json.getBytes(encoding).length);
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        }
    }
   
}
