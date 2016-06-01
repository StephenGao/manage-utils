package com.pkit.util;

/**
 * Created by xiaoping on 2016/5/19.
 */
public class StringUtils {
    private static StringBuffer getTraceInfo(Exception e) {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stacks = e.getStackTrace();
        for (int i = 0; i < stacks.length; i++) {
            sb.append("class: ").append(stacks[i].getClassName())
                    .append("; method: ").append(stacks[i].getMethodName())
                    .append("; line: ").append(stacks[i].getLineNumber())
                    .append(";  Exception: ");
            break;
        }
        return sb;
    }


    public static String getExceptionMessage(Exception e) {
        String message = e.toString();
        if (message.lastIndexOf(":") != -1)
            message = message.substring(0, message.lastIndexOf(":"));
        return getTraceInfo(e).append(message).toString();
    }
}
