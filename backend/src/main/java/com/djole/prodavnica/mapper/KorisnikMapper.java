package com.djole.prodavnica.mapper;

import com.djole.prodavnica.dto.KorisnikDTO;
import com.djole.prodavnica.model.Administrator;
import com.djole.prodavnica.model.Korisnik;
import com.djole.prodavnica.model.Kupac;
import com.djole.prodavnica.util.SessionUtil;
import org.springframework.stereotype.Component;

@Component
public class KorisnikMapper implements BaseMapper<KorisnikDTO, Korisnik> {

    @Override
    public KorisnikDTO toDomainDTO(Korisnik entity) {
        if (entity == null) {
            return null;
        }
        KorisnikDTO dto = new KorisnikDTO();
        dto.setId(entity.getIdKorisnik());
        dto.setKorisnickoIme(entity.getKorisnickoIme());
        dto.setEmail(entity.getEmail());

        if (entity instanceof Kupac kupac) {
            dto.setUloga(SessionUtil.ULOGA_KUPAC);
            dto.setAdresa(kupac.getAdresa());
            dto.setBrojTelefona(kupac.getBrojTelefona());
            dto.setBlokiran(kupac.isBlokiran());
        } else if (entity instanceof Administrator admin) {
            dto.setUloga(SessionUtil.ULOGA_ADMIN);
            dto.setNivoPristupa(admin.getNivoPristupa());
        }
        return dto;
    }

    @Override
    public Korisnik toDomainEntity(KorisnikDTO dto) {
        if (dto == null) {
            return null;
        }
        if (SessionUtil.ULOGA_ADMIN.equals(dto.getUloga())) {
            Administrator admin = new Administrator();
            admin.setIdKorisnik(dto.getId());
            admin.setKorisnickoIme(dto.getKorisnickoIme());
            admin.setLozinka(dto.getLozinka());
            admin.setEmail(dto.getEmail());
            admin.setNivoPristupa(dto.getNivoPristupa() == null ? 1 : dto.getNivoPristupa());
            return admin;
        }
        Kupac kupac = new Kupac();
        kupac.setIdKorisnik(dto.getId());
        kupac.setKorisnickoIme(dto.getKorisnickoIme());
        kupac.setLozinka(dto.getLozinka());
        kupac.setEmail(dto.getEmail());
        kupac.setAdresa(dto.getAdresa());
        kupac.setBrojTelefona(dto.getBrojTelefona());
        kupac.setBlokiran(Boolean.TRUE.equals(dto.getBlokiran()));
        return kupac;
    }
}
