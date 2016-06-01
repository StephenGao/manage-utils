package com.pkit.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class CookieUtils {

    public static void setCookieValue(HttpServletRequest request, HttpServletResponse response, String name, String value, Date expiretime) {

        Cookie cookie = new Cookie(name, value);
        if (expiretime != null) {
            cookie.setMaxAge(new Long(expiretime.getTime()).intValue());
        } else {
            cookie.setMaxAge(-1);
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void setCookieValue(HttpServletRequest request, HttpServletResponse response, String name, String value, int addtime) {

        Cookie cookie = new Cookie(name, value);
        if (addtime != 0) {
            cookie.setMaxAge(new Long(new Date(new Date().getTime() + addtime).getTime()).intValue());
        } else {
            cookie.setMaxAge(-1);
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void SetCookieValue(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        setCookieValue(request, response, name, value, null);
    }

    public static String getCookieValue(HttpServletRequest request, HttpServletResponse response, String name) {

        String cookies = "";
        Cookie[] cs = request.getCookies();
        if (cs != null) {
            for (Cookie c : cs) {
                if (c != null && c.getName().equals(name)) {
                    cookies = c.getValue();
                }
            }
        }
        return cookies;
    }
}
