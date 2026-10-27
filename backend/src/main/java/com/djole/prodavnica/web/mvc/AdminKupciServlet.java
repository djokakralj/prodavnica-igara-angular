package com.djole.prodavnica.web.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.djole.prodavnica.service.KorisnikAdminService;

import java.io.IOException;

public class AdminKupciServlet extends HttpServlet {

    private KorisnikAdminService korisnikAdminService;

    @Override
    public void init() {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        korisnikAdminService = ctx.getBean(KorisnikAdminService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Flash.preuzmi(req);
        req.setAttribute("kupci", korisnikAdminService.pretraziKupce(req.getParameter("pretraga")));
        req.getRequestDispatcher("/WEB-INF/jsp/admin/kupci.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        boolean blokiraj = "blokiraj".equals(req.getParameter("akcija"));
        korisnikAdminService.postaviBlokiran(id, blokiraj);
        Flash.postavi(req, blokiraj ? "Kupac je uspešno blokiran!" : "Blokada kupca je uspešno uklonjena!");
        resp.sendRedirect(req.getContextPath() + "/admin/kupci");
    }
}
