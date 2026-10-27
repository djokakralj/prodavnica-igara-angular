package com.djole.prodavnica.web.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.djole.prodavnica.service.*;

import java.io.IOException;

public class AdminDashboardServlet extends HttpServlet {

    private IgraService igraService;
    private IzdavacService izdavacService;
    private PlatformaService platformaService;
    private NarudzbinaService narudzbinaService;
    private KorisnikAdminService korisnikAdminService;

    @Override
    public void init() {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        igraService = ctx.getBean(IgraService.class);
        izdavacService = ctx.getBean(IzdavacService.class);
        platformaService = ctx.getBean(PlatformaService.class);
        narudzbinaService = ctx.getBean(NarudzbinaService.class);
        korisnikAdminService = ctx.getBean(KorisnikAdminService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("brojIgara", igraService.pretraziIgre(null, null, null, null, null).size());
        req.setAttribute("brojIzdavaca", izdavacService.pretrazi(null).size());
        req.setAttribute("brojPlatformi", platformaService.svePlatforme().size());
        req.setAttribute("brojNarudzbina", narudzbinaService.sveNarudzbine().size());
        req.setAttribute("brojKupaca", korisnikAdminService.pretraziKupce(null).size());
        req.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(req, resp);
    }
}
