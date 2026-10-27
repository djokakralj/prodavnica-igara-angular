package com.djole.prodavnica.web.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.djole.prodavnica.model.StatusNarudzbine;
import com.djole.prodavnica.service.NarudzbinaService;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;

import java.io.IOException;

public class AdminNarudzbineServlet extends HttpServlet {

    private NarudzbinaService narudzbinaService;

    @Override
    public void init() {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        narudzbinaService = ctx.getBean(NarudzbinaService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Flash.preuzmi(req);
        req.setAttribute("narudzbine", narudzbinaService.sveNarudzbine());
        req.setAttribute("statusi", StatusNarudzbine.values());
        req.getRequestDispatcher("/WEB-INF/jsp/admin/narudzbine.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            StatusNarudzbine status = StatusNarudzbine.valueOf(req.getParameter("status"));
            narudzbinaService.promeniStatus(Long.valueOf(req.getParameter("id")), status);
            Flash.postavi(req, "Status narudžbine je uspešno promenjen!");
        } catch (IllegalArgumentException e) {
            req.setAttribute("greska", "Nepoznat status narudžbine.");
            doGet(req, resp);
            return;
        } catch (NevalidniPodaciException e) {
            req.setAttribute("greska", e.getMessage());
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/narudzbine");
    }
}
