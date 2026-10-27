import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AutentifikacijaService } from '../../service/autentifikacija.service';
import { PorukeService } from '../../service/poruke.service';

@Component({
  selector: 'app-registracija',
  imports: [FormsModule],
  template: `
    <h1>Registracija</h1>
    @if (greska()) {
      <div class="greska">{{ greska() }}</div>
    }
    <form class="forma" (ngSubmit)="posalji()">
      <label>
        Korisničko ime
        <input [(ngModel)]="korisnickoIme" name="korisnickoIme" required autofocus />
      </label>
      <label>
        Email
        <input type="email" [(ngModel)]="email" name="email" required />
      </label>
      <label>
        Lozinka (min. 6 karaktera)
        <input type="password" minlength="6" [(ngModel)]="lozinka" name="lozinka" required />
      </label>
      <label>
        Adresa
        <input [(ngModel)]="adresa" name="adresa" />
      </label>
      <label>
        Broj telefona
        <input [(ngModel)]="brojTelefona" name="brojTelefona" />
      </label>
      <button type="submit">Registruj se</button>
    </form>
  `,
})
export class RegistracijaPage {
  private readonly auth = inject(AutentifikacijaService);
  private readonly poruke = inject(PorukeService);
  private readonly router = inject(Router);

  protected korisnickoIme = '';
  protected lozinka = '';
  protected email = '';
  protected adresa = '';
  protected brojTelefona = '';
  protected readonly greska = signal('');

  protected posalji(): void {
    this.greska.set('');
    this.auth
      .registrujSe({
        korisnickoIme: this.korisnickoIme,
        lozinka: this.lozinka,
        email: this.email,
        adresa: this.adresa,
        brojTelefona: this.brojTelefona,
      })
      .subscribe({
        next: (r) => {
          this.poruke.prikaziUspeh(r.poruka);
          this.router.navigate(['/']);
        },
        error: (e: Error) => this.greska.set(e.message),
      });
  }
}
