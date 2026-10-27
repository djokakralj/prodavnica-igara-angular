package com.djole.prodavnica.web.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class Flash {

    private static final String KLJUC = "flashPoruka";

    private Flash() {
    }

    public static void postavi(HttpServletRequest req, String poruka) {
        req.getSession().setAttribute(KLJUC, poruka);
    }

    public static void preuzmi(HttpServletRequest req) {
        HttpSession sesija = req.getSession(false);
        if (sesija == null) {
            return;
        }
        Object poruka = sesija.getAttribute(KLJUC);
        if (poruka != null) {
            sesija.removeAttribute(KLJUC);
            req.setAttribute("poruka", poruka);
        }
    }
}
