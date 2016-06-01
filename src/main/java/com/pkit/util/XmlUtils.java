package com.pkit.util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author hok
 *
 */
public abstract class XmlUtils {
	
	private final static Log log = LogFactory.getLog(XmlUtils.class);
	
	public static Document getDocument(String xmlPath) {
        SAXReader redaer = new SAXReader();
        Document document = null;
        try {
            InputStream in = XmlUtils.class.getClassLoader().getResourceAsStream(xmlPath);
            document = redaer.read(in);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            //throw new UnexpectedException(EnumerableErrorCodes.CONFIGURATION_ERROR, e.getMessage(), e);
        }
        return  document;
    }
    
    public static boolean isGzipInRequest(HttpServletRequest request) {
        String header = request.getHeader("Accept-Encoding");
        return (header != null) && (header.indexOf("gzip") >= 0);
    }
    
    public static String serialize(Object object) throws IOException {
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        return xStream.toXML(object);
    }
    
    public static void writeXmlToResponse(HttpServletResponse response, 
            String encoding, String xml, boolean addXmlHeader, boolean gzip, boolean noCache, 
            String contentType) throws IOException {
        response.setContentType(contentType + ";charset=" + encoding);
        if (noCache) {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expires", "0");
            response.setHeader("Pragma", "No-cache");
        }
        if (addXmlHeader) {
            xml = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>" + xml;
        }
        if (gzip) {
            response.addHeader("Content-Encoding", "gzip");
            GZIPOutputStream out = null;
            InputStream in = null;
            try {
                out = new GZIPOutputStream(response.getOutputStream());
                in = new ByteArrayInputStream(xml.getBytes());
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
            response.setContentLength(xml.getBytes(encoding).length);
            PrintWriter out = response.getWriter();
            out.print(xml);
            out.flush();
            out.close();
        }
    }
}
