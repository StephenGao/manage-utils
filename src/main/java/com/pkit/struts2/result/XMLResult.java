package com.pkit.struts2.result;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * 
 * @author HOK
 *
 */
public class XMLResult extends StrutsResultSupport {
	private final Log log = LogFactory.getLog(this.getClass());
    private static final String DEFAULT_CONTENT_TYPE = "text/xml";
    private String root="xml";
    private boolean noCache = false;
    private boolean enableGZIP = false;
    private String contentType;
    private String defaultEncoding = "UTF-8";
    
    @Inject(StrutsConstants.STRUTS_I18N_ENCODING)
    public void setDefaultEncoding(String val) {
        this.defaultEncoding = val;
    }
    
    @Override
    protected void doExecute(String finalLocation, ActionInvocation invocation)
            throws Exception {
        ActionContext actionContext = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);

        try {
            String xml;
            Object rootObject;
            if (this.root != null) {
                ValueStack stack = invocation.getStack();
                rootObject = stack.findValue(this.root);
            } else {
                rootObject = invocation.getAction();
            }
            xml = XmlUtils.serialize(rootObject);
            boolean writeGzip = enableGZIP && XmlUtils.isGzipInRequest(request);
            writeToResponse(response, xml, writeGzip);

        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        }
    }
    
    protected void writeToResponse(HttpServletResponse response, String xml, boolean gzip)
            throws IOException {
        XmlUtils.writeXmlToResponse(response, getEncoding(), xml, true, gzip, noCache, StringUtils.defaultString(contentType, DEFAULT_CONTENT_TYPE));
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
    
    /**
     * @return OGNL expression of root object to be serialized
     */
    public String getRoot() {
        return this.root;
    }

    /**
     * Sets the root object to be serialized, defaults to the Action
     * 
     * @param root
     *            OGNL expression of root object to be serialized
     */
    public void setRoot(String root) {
        this.root = root;
    }
    
    public boolean isEnableGZIP() {
        return enableGZIP;
    }

    public void setEnableGZIP(boolean enableGZIP) {
        this.enableGZIP = enableGZIP;
    }
    
    public boolean isNoCache() {
        return noCache;
    }

    /**
     * Add headers to response to prevent the browser from caching the response
     * 
     * @param noCache
     */
    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }
    
    /**
     * Content type to be set in the response
     * 
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
