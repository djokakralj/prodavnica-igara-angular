import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IgraDTO, RecenzijaDTO } from '../../model/modeli';
import { AutentifikacijaService } from '../../service/autentifikacija.service';
import { IgraService } from '../../service/igra.service';
import { KorpaService } from '../../service/korpa.service';
import { PorukeService } from '../../service/poruke.service';
import { RecenzijaService } from '../../service/recenzija.service';

@Component({
  selector: 'app-detalji-igre',
  imports: [FormsModule, DecimalPipe, DatePipe],
  template: `
    @if (igra(); as i) {
      <h1>{{ i.naziv }}</h1>
      <div class="karta">
        <p>{{ i.opis }}</p>
        <p><strong>Cena:</strong> {{ i.cena | number: '1.2-2' }} RSD</p>
        <p>
          <strong>Žanr:</strong> {{ i.zanr }} &nbsp; <strong>Godina:</strong> {{ i.godinaIzdanja }}
        </p>
        @if (i.izdavac) {
          <p><strong>Izdavač:</strong> {{ i.izdavac.naziv }} ({{ i.izdavac.drzava }})</p>
        }
        <p><strong>Na stanju:</strong> {{ i.kolicinaNaStanju }}</p>
        <div>
          @for (p of i.platforme; track p.idPlatforma) {
            <span class="badge">
              {{ p.nazivPlatforme }}{{ p.ekskluzivno ? ' (ekskluzivno)' : '' }}
            </span>
          }
        </div>
        @if (i.prosecnaOcena != null) {
          <p class="zvezdice">★ Prosečna ocena: {{ i.prosecnaOcena | number: '1.1-1' }}</p>
        }
      </div>

      @if (greska()) {
        <div class="greska">{{ greska() }}</div>
      }

      @if (auth.jeKupac()) {
        <div class="karta">
          <h3>Dodaj u korpu</h3>
          <div style="display: flex; gap: 10px; align-items: center;">
            <input
              type="number"
              min="1"
              [max]="i.kolicinaNaStanju"
              [(ngModel)]="kolicina"
              style="width: 80px"
            />
            <button [disabled]="i.kolicinaNaStanju === 0" (click)="dodajUKorpu()">
              Dodaj u korpu
            </button>
            @if (i.kolicinaNaStanju === 0) {
              <span class="nema-na-stanju">Nema igre na stanju</span>
            }
          </div>
        </div>
      }

      <div class="karta">
        <h3>Recenzije</h3>
        @for (r of recenzije(); track r.id) {
          <div class="recenzija">
            <div class="autor">
              {{ r.korisnickoImeKupca }} — <span class="zvezdice">{{ zvezde(r.ocena) }}</span>
            </div>
            <div class="datum">{{ r.datum | date: 'dd.MM.yyyy. HH:mm' }}</div>
            <p>{{ r.komentar }}</p>
          </div>
        } @empty {
          <p>Još uvek nema recenzija.</p>
        }

        @if (auth.jeKupac()) {
          <form class="forma" (ngSubmit)="posaljiRecenziju()" style="margin-top: 16px">
            <label>
              Ocena (1-5)
              <select [(ngModel)]="ocena" name="ocena">
                @for (o of [5, 4, 3, 2, 1]; track o) {
                  <option [value]="o">{{ o }}</option>
                }
              </select>
            </label>
            <label>
              Komentar
              <textarea rows="3" [(ngModel)]="komentar" name="komentar"></textarea>
            </label>
            <button type="submit">Ostavi recenziju</button>
          </form>
        }
      </div>
    } @else {
      @if (greska()) {
        <div class="greska">{{ greska() }}</div>
      } @else {
        <p>Učitavanje...</p>
      }
    }
  `,
})
export class DetaljiIgrePage implements OnInit {
  private readonly ruta = inject(ActivatedRoute);
  private readonly igraService = inject(IgraService);
  private readonly korpaService = inject(KorpaService);
  private readonly recenzijaService = inject(RecenzijaService);
  private readonly poruke = inject(PorukeService);
  protected readonly auth = inject(AutentifikacijaService);

  protected readonly igra = signal<IgraDTO | null>(null);
  protected readonly recenzije = signal<RecenzijaDTO[]>([]);
  protected readonly greska = signal('');

  protected kolicina = 1;
  protected ocena = 5;
  protected komentar = '';

  private id = 0;

  ngOnInit(): void {
    this.id = Number(this.ruta.snapshot.paramMap.get('id'));
    this.ucitaj();
  }

  private ucitaj(): void {
    this.igraService.detalji(this.id).subscribe({
      next: (i) => this.igra.set(i),
      error: (e: Error) => this.greska.set(e.message),
    });
    this.recenzijaService.zaIgru(this.id).subscribe({
      next: (r) => this.recenzije.set(r),
      error: () => {},
    });
  }

  protected dodajUKorpu(): void {
    this.poruke.ocisti();
    this.greska.set('');
    this.korpaService.dodaj(this.id, Number(this.kolicina)).subscribe({
      next: (r) => {
        this.poruke.prikaziUspeh(r.poruka);
      },
      error: (e: Error) => this.greska.set(e.message),
    });
  }

  protected posaljiRecenziju(): void {
    this.poruke.ocisti();
    this.greska.set('');
    this.recenzijaService.dodaj(this.id, Number(this.ocena), this.komentar).subscribe({
      next: (r) => {
        this.komentar = '';
        this.poruke.prikaziUspeh(r.poruka);
        this.ucitaj();
      },
      error: (e: Error) => this.greska.set(e.message),
    });
  }

  protected zvezde(ocena: number): string {
    return '★'.repeat(ocena);
  }
}
