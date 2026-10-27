package com.djole.prodavnica.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.LinkedHashMap;
import java.util.Map;

public final class KorpaUtil {

    private static final String ATTR_KORPA = "korpa";

    private KorpaUtil() {
    }

    @SuppressWarnings("unchecked")
    public static Map<Long, Integer> korpa(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        Map<Long, Integer> korpa = (Map<Long, Integer>) session.getAttribute(ATTR_KORPA);
        if (korpa == null) {
            korpa = new LinkedHashMap<>();
            session.setAttribute(ATTR_KORPA, korpa);
        }
        return korpa;
    }

    public static void isprazni(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(ATTR_KORPA);
        }
    }
}
