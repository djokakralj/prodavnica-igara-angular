import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, catchError, of, tap } from 'rxjs';
import { KorisnikDTO, RegistracijaZahtev } from '../model/modeli';
import { ApiService, Rezultat } from './api.service';

@Injectable({ providedIn: 'root' })
export class AutentifikacijaService {
  private readonly api = inject(ApiService);

  private readonly _korisnik = signal<KorisnikDTO | null>(null);
  private readonly _ucitavanje = signal(true);

  readonly korisnik = this._korisnik.asReadonly();
  readonly ucitavanje = this._ucitavanje.asReadonly();
  readonly jePrijavljen = computed(() => this._korisnik() !== null);
  readonly jeKupac = computed(() => this._korisnik()?.uloga === 'KUPAC');

  ucitajTrenutnog(): Observable<KorisnikDTO | null> {
    return this.api.getValue<KorisnikDTO>('/auth/ja').pipe(
      tap((k) => this._korisnik.set(k)),
      catchError(() => {
        this._korisnik.set(null);
        return of(null);
      }),
      tap(() => this._ucitavanje.set(false))
    );
  }

  prijaviSe(korisnickoIme: string, lozinka: string): Observable<Rezultat<KorisnikDTO>> {
    return this.api
      .postRezultat<KorisnikDTO>('/auth/prijava', { korisnickoIme, lozinka })
      .pipe(tap((r) => this._korisnik.set(r.vrednost)));
  }

  registrujSe(zahtev: RegistracijaZahtev): Observable<Rezultat<KorisnikDTO>> {
    return this.api
      .postRezultat<KorisnikDTO>('/auth/registracija', zahtev)
      .pipe(tap((r) => this._korisnik.set(r.vrednost)));
  }

  ocistiLokalnoStanje(): void {
    this._korisnik.set(null);
  }

  odjaviSe(): Observable<string> {
    return this.api.postMessage('/auth/odjava').pipe(tap(() => this._korisnik.set(null)));
  }
}
