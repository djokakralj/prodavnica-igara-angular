import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AutentifikacijaService } from '../../service/autentifikacija.service';
import { PorukeService } from '../../service/poruke.service';

@Component({
  selector: 'app-prijava',
  imports: [FormsModule],
  template: `
    <h1>Prijava</h1>
    @if (greska()) {
      <div class="greska">{{ greska() }}</div>
    }
    <form class="forma" (ngSubmit)="posalji()">
      <label>
        Korisničko ime
        <input [(ngModel)]="korisnickoIme" name="korisnickoIme" required autofocus />
      </label>
      <label>
        Lozinka
        <input type="password" [(ngModel)]="lozinka" name="lozinka" required />
      </label>
      <button type="submit">Prijavi se</button>
    </form>
  `,
})
export class PrijavaPage {
  private readonly auth = inject(AutentifikacijaService);
  private readonly poruke = inject(PorukeService);
  private readonly router = inject(Router);

  protected korisnickoIme = '';
  protected lozinka = '';
  protected readonly greska = signal('');

  protected posalji(): void {
    this.greska.set('');
    this.auth.prijaviSe(this.korisnickoIme, this.lozinka).subscribe({
      next: (r) => {
        this.poruke.prikaziUspeh(r.poruka);
        this.router.navigate(['/']);
      },
      error: (e: Error) => this.greska.set(e.message),
    });
  }
}
