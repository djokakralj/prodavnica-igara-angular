package com.djole.prodavnica.service;

import com.djole.prodavnica.model.Kupac;
import com.djole.prodavnica.model.Narudzbina;
import com.djole.prodavnica.model.StavkaNarudzbine;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class NarudzbinaPdfService {

    private static final DateTimeFormatter DATUM = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

    private static final Color SIVA_ZAGLAVLJA = new Color(0xEE, 0xF0, 0xF6);
    private static final Color SIVA_TEKSTA = new Color(0x55, 0x5A, 0x66);

    private final Font naslov;
    private final Font podnaslov;
    private final Font obican;
    private final Font podebljan;
    private final Font sitan;

    public NarudzbinaPdfService() {
        BaseFont osnovni = ucitajOsnovniFont();
        this.naslov = new Font(osnovni, 18, Font.BOLD);
        this.podnaslov = new Font(osnovni, 12, Font.BOLD);
        this.obican = new Font(osnovni, 10);
        this.podebljan = new Font(osnovni, 10, Font.BOLD);
        this.sitan = new Font(osnovni, 8, Font.NORMAL, SIVA_TEKSTA);
    }

    public byte[] generisi(Narudzbina narudzbina) {
        Document dokument = new Document(PageSize.A4, 48, 48, 48, 48);
        ByteArrayOutputStream izlaz = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(dokument, izlaz);
            dokument.addTitle("Narudžbina br. " + narudzbina.getIdNarudzbina());
            dokument.open();

            dokument.add(zaglavlje(narudzbina));
            dokument.add(podaciOKupcu(narudzbina.getKupac()));
            dokument.add(tabelaStavki(narudzbina));
            dokument.add(ukupno(narudzbina.getUkupnaCena()));
            dokument.add(napomena());

            dokument.close();
        } catch (DocumentException e) {
            throw new IllegalStateException("Neuspešno generisanje PDF-a narudžbine.", e);
        }

        return izlaz.toByteArray();
    }

    private Paragraph zaglavlje(Narudzbina narudzbina) {
        Paragraph blok = new Paragraph();
        blok.add(new Paragraph("Prodavnica igara", naslov));
        blok.add(new Paragraph("Narudžbina br. " + narudzbina.getIdNarudzbina(), podnaslov));
        blok.add(new Paragraph("Datum: " + narudzbina.getDatum().format(DATUM), obican));
        blok.add(new Paragraph("Status: " + narudzbina.getStatus(), obican));
        blok.setSpacingAfter(18f);
        return blok;
    }

    private Paragraph podaciOKupcu(Kupac kupac) {
        Paragraph blok = new Paragraph();
        blok.add(new Paragraph("Kupac", podnaslov));
        blok.add(new Paragraph(kupac.getKorisnickoIme(), obican));
        blok.add(new Paragraph(kupac.getEmail(), obican));
        if (kupac.getAdresa() != null && !kupac.getAdresa().isBlank()) {
            blok.add(new Paragraph(kupac.getAdresa(), obican));
        }
        if (kupac.getBrojTelefona() != null && !kupac.getBrojTelefona().isBlank()) {
            blok.add(new Paragraph(kupac.getBrojTelefona(), obican));
        }
        blok.setSpacingAfter(18f);
        return blok;
    }

    private PdfPTable tabelaStavki(Narudzbina narudzbina) throws DocumentException {
        PdfPTable tabela = new PdfPTable(new float[]{46f, 14f, 20f, 20f});
        tabela.setWidthPercentage(100f);
        tabela.setSpacingAfter(14f);
        tabela.setHeaderRows(1);

        tabela.addCell(celijaZaglavlja("Igra", Element.ALIGN_LEFT));
        tabela.addCell(celijaZaglavlja("Količina", Element.ALIGN_CENTER));
        tabela.addCell(celijaZaglavlja("Cena po komadu", Element.ALIGN_RIGHT));
        tabela.addCell(celijaZaglavlja("Ukupno", Element.ALIGN_RIGHT));

        for (StavkaNarudzbine stavka : narudzbina.getStavke()) {
            tabela.addCell(celija(stavka.getIgra().getNaziv(), Element.ALIGN_LEFT, obican));
            tabela.addCell(celija(String.valueOf(stavka.getKolicina()), Element.ALIGN_CENTER, obican));
            tabela.addCell(celija(novac(stavka.getCenaPoKomadu()), Element.ALIGN_RIGHT, obican));
            tabela.addCell(celija(novac(stavka.getUkupno()), Element.ALIGN_RIGHT, obican));
        }
        return tabela;
    }

    private Paragraph ukupno(BigDecimal ukupnaCena) {
        Paragraph red = new Paragraph("Ukupna cena: " + novac(ukupnaCena) + " RSD", podnaslov);
        red.setAlignment(Element.ALIGN_RIGHT);
        red.setSpacingAfter(24f);
        return red;
    }

    private Paragraph napomena() {
        return new Paragraph("Dokument je generisan automatski i važi bez potpisa i pečata.", sitan);
    }

    private PdfPCell celijaZaglavlja(String tekst, int poravnanje) {
        PdfPCell celija = celija(tekst, poravnanje, podebljan);
        celija.setBackgroundColor(SIVA_ZAGLAVLJA);
        return celija;
    }

    private PdfPCell celija(String tekst, int poravnanje, Font font) {
        PdfPCell celija = new PdfPCell(new Phrase(tekst, font));
        celija.setHorizontalAlignment(poravnanje);
        celija.setPadding(6f);
        return celija;
    }

    private String novac(BigDecimal iznos) {
        DecimalFormatSymbols simboli = new DecimalFormatSymbols(Locale.forLanguageTag("sr-Latn-RS"));
        simboli.setGroupingSeparator('.');
        simboli.setDecimalSeparator(',');
        return new DecimalFormat("#,##0.00", simboli).format(iznos);
    }

    private static BaseFont ucitajOsnovniFont() {
        try {
            return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new IllegalStateException("Neuspešno učitavanje fonta za PDF.", e);
        }
    }
}
