package com.djole.prodavnica.web.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.djole.prodavnica.service.IgraService;
import com.djole.prodavnica.service.IzdavacService;
import com.djole.prodavnica.service.PlatformaService;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.service.exception.NijePronadjenoException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AdminIgreServlet extends HttpServlet {

    private IgraService igraService;
    private IzdavacService izdavacService;
    private PlatformaService platformaService;

    @Override
    public void init() {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        igraService = ctx.getBean(IgraService.class);
        izdavacService = ctx.getBean(IzdavacService.class);
        platformaService = ctx.getBean(PlatformaService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Flash.preuzmi(req);
        req.setAttribute("igre", igraService.pretraziIgre(req.getParameter("pretraga"), null, null, null, null));
        req.setAttribute("izdavaci", izdavacService.pretrazi(null));
        req.setAttribute("platforme", platformaService.svePlatforme());

        String id = req.getParameter("id");
        if (id != null && !id.isBlank()) {
            try {
                req.setAttribute("igraZaIzmenu", igraService.nadjiPoId(Long.valueOf(id)));
            } catch (NijePronadjenoException e) {
                req.setAttribute("greska", e.getMessage());
            }
        }
        req.getRequestDispatcher("/WEB-INF/jsp/admin/igre.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String akcija = req.getParameter("akcija");
        try {
            switch (akcija) {
                case "kreiraj" -> {
                    igraService.kreiraj(
                            req.getParameter("naziv"), req.getParameter("opis"), cena(req), godina(req),
                            req.getParameter("zanr"), kolicina(req), idIzdavac(req));
                    Flash.postavi(req, "Igra je uspešno dodata!");
                }
                case "izmeni" -> {
                    igraService.izmeni(Long.valueOf(req.getParameter("id")),
                            req.getParameter("naziv"), req.getParameter("opis"), cena(req), godina(req),
                            req.getParameter("zanr"), kolicina(req), idIzdavac(req));
                    Flash.postavi(req, "Igra je uspešno izmenjena!");
                }
                case "obrisi" -> {
                    igraService.obrisi(Long.valueOf(req.getParameter("id")));
                    Flash.postavi(req, "Igra je uspešno obrisana!");
                }
                case "dodajPlatformu" -> {
                    LocalDate datum = req.getParameter("datumPortovanja") == null || req.getParameter("datumPortovanja").isBlank()
                            ? null : LocalDate.parse(req.getParameter("datumPortovanja"));
                    igraService.dodajNaPlatformu(Long.valueOf(req.getParameter("idIgra")),
                            Long.valueOf(req.getParameter("idPlatforma")), datum,
                            "on".equals(req.getParameter("ekskluzivno")));
                    Flash.postavi(req, "Igra je uspešno dodata na platformu!");
                }
                case "ukloniPlatformu" -> {
                    igraService.ukloniSaPlatforme(
                            Long.valueOf(req.getParameter("idIgra")), Long.valueOf(req.getParameter("idPlatforma")));
                    Flash.postavi(req, "Igra je uspešno uklonjena sa platforme!");
                }
                default -> throw new NevalidniPodaciException("Nepoznata akcija.");
            }
        } catch (NevalidniPodaciException | NijePronadjenoException e) {
            req.setAttribute("greska", e.getMessage());
            doGet(req, resp);
            return;
        }
        String povratakId = req.getParameter("idIgra") != null ? req.getParameter("idIgra") : req.getParameter("id");
        boolean prikaziId = !"obrisi".equals(akcija) && !"kreiraj".equals(akcija) && povratakId != null && !povratakId.isBlank();
        String suffix = prikaziId ? "?id=" + povratakId : "";
        resp.sendRedirect(req.getContextPath() + "/admin/igre" + suffix);
    }

    private static BigDecimal cena(HttpServletRequest req) {
        String v = req.getParameter("cena");
        return (v == null || v.isBlank()) ? null : new BigDecimal(v);
    }

    private static Integer godina(HttpServletRequest req) {
        String v = req.getParameter("godinaIzdanja");
        return (v == null || v.isBlank()) ? null : Integer.valueOf(v);
    }

    private static int kolicina(HttpServletRequest req) {
        String v = req.getParameter("kolicinaNaStanju");
        return (v == null || v.isBlank()) ? 0 : Integer.parseInt(v);
    }

    private static Long idIzdavac(HttpServletRequest req) {
        String v = req.getParameter("idIzdavac");
        return (v == null || v.isBlank()) ? null : Long.valueOf(v);
    }
}
