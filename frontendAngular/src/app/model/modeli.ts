export interface IzdavacDTO {
  id: number;
  naziv: string;
  drzava?: string;
}

export interface PlatformaDTO {
  id: number;
  naziv: string;
}

export interface IgraPlatformaDTO {
  idPlatforma: number;
  nazivPlatforme: string;
  datumPortovanja?: string;
  ekskluzivno: boolean;
}

export interface IgraDTO {
  id: number;
  naziv: string;
  opis?: string;
  cena: number;
  godinaIzdanja?: number;
  zanr?: string;
  kolicinaNaStanju: number;
  izdavac?: IzdavacDTO;
  platforme: IgraPlatformaDTO[];
  prosecnaOcena?: number;
}

export interface KorisnikDTO {
  id: number;
  korisnickoIme: string;
  email?: string;
  uloga: 'KUPAC' | 'ADMINISTRATOR';
  adresa?: string;
  brojTelefona?: string;
  blokiran?: boolean;
  nivoPristupa?: number;
}

export interface KorpaStavkaDTO {
  idIgra: number;
  naziv: string;
  cena: number;
  kolicina: number;
  ukupno: number;
  kolicinaNaStanju: number;
}

export interface KorpaDTO {
  stavke: KorpaStavkaDTO[];
  ukupnaCena: number;
}

export interface StavkaNarudzbineDTO {
  idIgra: number;
  nazivIgre: string;
  kolicina: number;
  cenaPoKomadu: number;
  ukupno: number;
}

export interface NarudzbinaDTO {
  id: number;
  datum: string;
  status: string;
  ukupnaCena: number;
  idKupac: number;
  korisnickoImeKupca: string;
  stavke: StavkaNarudzbineDTO[];
}

export interface RecenzijaDTO {
  id: number;
  ocena: number;
  komentar?: string;
  datum: string;
  idKupac: number;
  korisnickoImeKupca: string;
  idIgra: number;
}

export interface RegistracijaZahtev {
  korisnickoIme: string;
  lozinka: string;
  email: string;
  adresa?: string;
  brojTelefona?: string;
}

export interface FilterIgara {
  naziv?: string;
  zanr?: string;
  idPlatforma?: string;
  minCena?: string;
  maxCena?: string;
}

export type PoljeSortiranja = 'naziv' | 'cena' | 'godinaIzdanja' | 'kolicinaNaStanju';

export type SmerSortiranja = 'rastuce' | 'opadajuce';

export interface Sortiranje {
  polje: PoljeSortiranja;
  smer: SmerSortiranja;
}

export interface Strana<T> {
  values: T[];
  strana: number;
  velicina: number;
  ukupnoElemenata: number;
  ukupnoStrana: number;
}
