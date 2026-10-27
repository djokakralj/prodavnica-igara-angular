package com.djole.prodavnica.web.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.djole.prodavnica.model.Administrator;
import com.djole.prodavnica.model.Korisnik;
import com.djole.prodavnica.service.AutentifikacijaService;
import com.djole.prodavnica.service.exception.NedozvoljenAkcijaException;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.util.SessionUtil;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private AutentifikacijaService autentifikacijaService;

    @Override
    public void init() {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        autentifikacijaService = ctx.getBean(AutentifikacijaService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionUtil.jeAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/admin");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String korisnickoIme = req.getParameter("korisnickoIme");
        String lozinka = req.getParameter("lozinka");
        try {
            Korisnik korisnik = autentifikacijaService.prijaviSe(korisnickoIme, lozinka);
            if (!(korisnik instanceof Administrator)) {
                throw new NedozvoljenAkcijaException("Admin panelu mogu pristupiti samo administratori.");
            }
            SessionUtil.prijaviKorisnika(req, korisnik);
            resp.sendRedirect(req.getContextPath() + "/admin");
        } catch (NevalidniPodaciException | NedozvoljenAkcijaException e) {
            req.setAttribute("greska", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }
}
