package com.djole.prodavnica.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.djole.prodavnica.model.Administrator;
import com.djole.prodavnica.model.Korisnik;
import com.djole.prodavnica.model.Kupac;
import com.djole.prodavnica.service.exception.NeautentifikovanException;
import com.djole.prodavnica.service.exception.NedozvoljenAkcijaException;

public final class SessionUtil {

    private static final String ATTR_ID = "idKorisnik";
    private static final String ATTR_ULOGA = "uloga";
    private static final String ATTR_KORISNICKO_IME = "korisnickoIme";

    public static final String ULOGA_KUPAC = "KUPAC";
    public static final String ULOGA_ADMIN = "ADMINISTRATOR";

    private SessionUtil() {
    }

    public static void prijaviKorisnika(HttpServletRequest req, Korisnik korisnik) {
        HttpSession stara = req.getSession(false);
        if (stara != null) {
            stara.invalidate();
        }
        HttpSession session = req.getSession(true);
        session.setAttribute(ATTR_ID, korisnik.getIdKorisnik());
        session.setAttribute(ATTR_KORISNICKO_IME, korisnik.getKorisnickoIme());
        session.setAttribute(ATTR_ULOGA, korisnik instanceof Administrator ? ULOGA_ADMIN : ULOGA_KUPAC);
    }

    public static void odjaviKorisnika(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static Long trenutniIdKorisnika(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (Long) session.getAttribute(ATTR_ID);
    }

    public static String trenutnaUloga(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (String) session.getAttribute(ATTR_ULOGA);
    }

    public static String trenutnoKorisnickoIme(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (String) session.getAttribute(ATTR_KORISNICKO_IME);
    }

    public static boolean jePrijavljen(HttpServletRequest req) {
        return trenutniIdKorisnika(req) != null;
    }

    public static boolean jeKupac(HttpServletRequest req) {
        return ULOGA_KUPAC.equals(trenutnaUloga(req));
    }

    public static boolean jeAdmin(HttpServletRequest req) {
        return ULOGA_ADMIN.equals(trenutnaUloga(req));
    }

    public static Long zahtevajPrijavu(HttpServletRequest req) {
        Long id = trenutniIdKorisnika(req);
        if (id == null) {
            throw new NeautentifikovanException("Niste prijavljeni.");
        }
        return id;
    }

    public static Long zahtevajKupca(HttpServletRequest req) {
        Long id = zahtevajPrijavu(req);
        if (!jeKupac(req)) {
            throw new NedozvoljenAkcijaException("Akcija je dozvoljena samo kupcima.");
        }
        return id;
    }

    public static void zahtevajAdmina(HttpServletRequest req) {
        zahtevajPrijavu(req);
        if (!jeAdmin(req)) {
            throw new NedozvoljenAkcijaException("Akcija je dozvoljena samo administratorima.");
        }
    }
}
