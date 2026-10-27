import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AutentifikacijaService } from '../../service/autentifikacija.service';
import { PorukeService } from '../../service/poruke.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  template: `
    <nav class="navbar">
      <a routerLink="/" class="brand">Prodavnica igara</a>
      <div class="links">
        <a routerLink="/">Katalog</a>
        @if (auth.jePrijavljen()) {
          @if (auth.jeKupac()) {
            <a routerLink="/korpa">Korpa</a>
            <a routerLink="/moje-narudzbine">Moje narudžbine</a>
          }
          <span>{{ auth.korisnik()!.korisnickoIme }}</span>
          <button (click)="odjava()">Odjava</button>
        } @else {
          <a routerLink="/prijava">Prijava</a>
          <a routerLink="/registracija">Registracija</a>
        }
      </div>
    </nav>
  `,
})
export class Navbar {
  protected readonly auth = inject(AutentifikacijaService);
  private readonly poruke = inject(PorukeService);
  private readonly router = inject(Router);

  protected odjava(): void {
    this.auth.odjaviSe().subscribe({
      next: (poruka) => {
        this.poruke.prikaziUspeh(poruka);
        this.router.navigate(['/']);
      },
      error: () => this.router.navigate(['/']),
    });
  }
}
