import { DecimalPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { KorpaDTO } from '../../model/modeli';
import { KorpaService } from '../../service/korpa.service';
import { NarudzbinaService } from '../../service/narudzbina.service';
import { PorukeService } from '../../service/poruke.service';

const PRAZNA_KORPA: KorpaDTO = { stavke: [], ukupnaCena: 0 };

@Component({
  selector: 'app-korpa',
  imports: [FormsModule, DecimalPipe],
  template: `
    <h1>Korpa</h1>
    @if (greska()) {
      <div class="greska">{{ greska() }}</div>
    }

    @if (korpa().stavke.length === 0) {
      <p>Korpa je prazna.</p>
    } @else {
      <table class="tabela">
        <thead>
          <tr>
            <th>Igra</th>
            <th>Cena</th>
            <th>Količina</th>
            <th>Ukupno</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          @for (s of korpa().stavke; track s.idIgra) {
            <tr>
              <td>{{ s.naziv }}</td>
              <td>{{ s.cena | number: '1.2-2' }}</td>
              <td>
                <input
                  type="number"
                  min="1"
                  [max]="s.kolicinaNaStanju"
                  [ngModel]="s.kolicina"
                  (ngModelChange)="izmeniKolicinu(s.idIgra, $event)"
                  style="width: 70px"
                />
              </td>
              <td>{{ s.ukupno | number: '1.2-2' }}</td>
              <td><button class="opasnost" (click)="ukloni(s.idIgra)">Ukloni</button></td>
            </tr>
          }
        </tbody>
      </table>
      <p>
        <strong>Ukupno: {{ korpa().ukupnaCena | number: '1.2-2' }} RSD</strong>
      </p>
      <button (click)="checkout()">Napravi narudžbinu</button>
    }
  `,
})
export class KorpaPage implements OnInit {
  private readonly korpaService = inject(KorpaService);
  private readonly narudzbinaService = inject(NarudzbinaService);
  private readonly poruke = inject(PorukeService);
  private readonly router = inject(Router);

  protected readonly korpa = signal<KorpaDTO>(PRAZNA_KORPA);
  protected readonly greska = signal('');

  ngOnInit(): void {
    this.ucitaj();
  }

  private ucitaj(): void {
    this.korpaService.pregled().subscribe({
      next: (k) => this.korpa.set(k ?? PRAZNA_KORPA),
      error: (e: Error) => this.greska.set(e.message),
    });
  }

  protected izmeniKolicinu(idIgra: number, kolicina: number): void {
    this.greska.set('');
    this.korpaService.izmeni(idIgra, Number(kolicina)).subscribe({
      next: (r) => {
        this.korpa.set(r.vrednost ?? PRAZNA_KORPA);
        this.poruke.prikaziUspeh(r.poruka);
      },
      error: (e: Error) => {
        this.greska.set(e.message);
        this.ucitaj();
      },
    });
  }

  protected ukloni(idIgra: number): void {
    this.greska.set('');
    this.korpaService.ukloni(idIgra).subscribe({
      next: (r) => {
        this.korpa.set(r.vrednost ?? PRAZNA_KORPA);
        this.poruke.prikaziUspeh(r.poruka);
      },
      error: (e: Error) => this.greska.set(e.message),
    });
  }

  protected checkout(): void {
    this.greska.set('');
    this.narudzbinaService.napravi().subscribe({
      next: (r) => {
        this.poruke.prikaziUspeh(r.poruka);
        this.router.navigate(['/moje-narudzbine']);
      },
      error: (e: Error) => {
        this.greska.set(e.message);
        this.ucitaj();
      },
    });
  }
}
