package com.djole.prodavnica.web.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.djole.prodavnica.service.PlatformaService;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.service.exception.NijePronadjenoException;

import java.io.IOException;

public class AdminPlatformeServlet extends HttpServlet {

    private PlatformaService platformaService;

    @Override
    public void init() {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        platformaService = ctx.getBean(PlatformaService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Flash.preuzmi(req);
        req.setAttribute("platforme", platformaService.svePlatforme());
        String id = req.getParameter("id");
        if (id != null && !id.isBlank()) {
            try {
                req.setAttribute("platformaZaIzmenu", platformaService.nadjiPoId(Long.valueOf(id)));
            } catch (NijePronadjenoException e) {
                req.setAttribute("greska", e.getMessage());
            }
        }
        req.getRequestDispatcher("/WEB-INF/jsp/admin/platforme.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String akcija = req.getParameter("akcija");
        try {
            switch (akcija) {
                case "kreiraj" -> {
                    platformaService.kreiraj(req.getParameter("naziv"));
                    Flash.postavi(req, "Platforma je uspešno dodata!");
                }
                case "izmeni" -> {
                    platformaService.izmeni(Long.valueOf(req.getParameter("id")), req.getParameter("naziv"));
                    Flash.postavi(req, "Platforma je uspešno izmenjena!");
                }
                case "obrisi" -> {
                    platformaService.obrisi(Long.valueOf(req.getParameter("id")));
                    Flash.postavi(req, "Platforma je uspešno obrisana!");
                }
                default -> throw new NevalidniPodaciException("Nepoznata akcija.");
            }
        } catch (NevalidniPodaciException e) {
            req.setAttribute("greska", e.getMessage());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/platforme");
    }
}
