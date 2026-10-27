package com.djole.prodavnica.web.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.djole.prodavnica.service.IzdavacService;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.service.exception.NijePronadjenoException;

import java.io.IOException;

public class AdminIzdavaciServlet extends HttpServlet {

    private IzdavacService izdavacService;

    @Override
    public void init() {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        izdavacService = ctx.getBean(IzdavacService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Flash.preuzmi(req);
        req.setAttribute("izdavaci", izdavacService.pretrazi(req.getParameter("pretraga")));
        String id = req.getParameter("id");
        if (id != null && !id.isBlank()) {
            try {
                req.setAttribute("izdavacZaIzmenu", izdavacService.nadjiPoId(Long.valueOf(id)));
            } catch (NijePronadjenoException e) {
                req.setAttribute("greska", e.getMessage());
            }
        }
        req.getRequestDispatcher("/WEB-INF/jsp/admin/izdavaci.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String akcija = req.getParameter("akcija");
        try {
            switch (akcija) {
                case "kreiraj" -> {
                    izdavacService.kreiraj(req.getParameter("naziv"), req.getParameter("drzava"));
                    Flash.postavi(req, "Izdavač je uspešno dodat!");
                }
                case "izmeni" -> {
                    izdavacService.izmeni(Long.valueOf(req.getParameter("id")),
                            req.getParameter("naziv"), req.getParameter("drzava"));
                    Flash.postavi(req, "Izdavač je uspešno izmenjen!");
                }
                case "obrisi" -> {
                    izdavacService.obrisi(Long.valueOf(req.getParameter("id")));
                    Flash.postavi(req, "Izdavač je uspešno obrisan!");
                }
                default -> throw new NevalidniPodaciException("Nepoznata akcija.");
            }
        } catch (NevalidniPodaciException e) {
            req.setAttribute("greska", e.getMessage());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/izdavaci");
    }
}
