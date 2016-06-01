package com.pkit.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by xiaoping on 2016/5/12.
 */
public class DataType {
    private static Log log = LogFactory.getLog(DataType.class.getName());

    public static int getInt(Object obj) {
        int tmpret = 0;
        if (obj != null) {
            try {
                String objStr = obj.toString();
                if (objStr.indexOf(".") > -1) {
                    double objDou = Double.parseDouble(objStr);
                    objStr = Math.round(objDou) + "";
                }
                tmpret = Integer.parseInt(objStr);
            } catch (Exception ex) {
                log.error(ex, ex);
            }
        }
        return tmpret;
    }

    public static Long getLong(Object obj, Long defvalue) {
        Long tmpret = defvalue;
        if (obj != null) {
            try {
                tmpret = Long.parseLong(obj.toString());
            } catch (Exception ex) {
                log.error(ex, ex);
            }
        }
        return tmpret;
    }

    public static Long getLong(Object obj) {
        Long tmpret = 0L;
        if (obj != null) {
            try {
                tmpret = Long.parseLong(obj.toString());
            } catch (Exception ex) {
            }
        }
        return tmpret;
    }

    public static int getInt(Object obj, int defvalue) {
        int tmpret = defvalue;
        if (obj != null) {
            try {
                tmpret = Integer.parseInt(obj.toString());
            } catch (Exception ex) {

            }
        }
        return tmpret;
    }

    public static Integer getInteger(Object obj) {
        Integer tmpret = new Integer(0);
        if (obj != null) {
            try {
                tmpret = Integer.valueOf(obj.toString());
            } catch (Exception ex) {
                log.error("getInteger:" + ex);
            }
        }
        return tmpret;
    }

    public static Integer getInteger(Object obj, int defvalue) {
        Integer tmpret = new Integer(defvalue);
        if (obj != null) {
            try {
                tmpret = Integer.valueOf(obj.toString());
            } catch (Exception ex) {
                log.error("getInteger:" + ex);
            }
        }
        return tmpret;
    }

    public static double getDouble(Object obj) {
        double tmpvalue = 0;
        try {
            if (obj != null && !"".equals(obj)) {
                tmpvalue = Double.parseDouble(obj.toString());
            }

        } catch (Exception ex) {
            log.error("getDouble :" + ex);
        }
        return tmpvalue;
    }

    public static double getDouble(Object obj, double defvalue) {
        double tmpvalue = defvalue;
        try {
            tmpvalue = Double.parseDouble(obj.toString());
        } catch (Exception ex) {
            log.error("getDouble :" + ex);
        }
        return tmpvalue;
    }

    public static String getString(Object obj) {
        String tmpret = "";
        if (obj != null)
            tmpret = obj.toString();
        return tmpret.trim();
    }


}
