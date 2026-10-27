import { DecimalPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Subject, catchError, debounceTime, map, of, switchMap } from 'rxjs';
import { IgraDTO, PlatformaDTO, PoljeSortiranja, SmerSortiranja, Strana } from '../../model/modeli';
import { IgraService } from '../../service/igra.service';
import { PlatformaService } from '../../service/platforma.service';

const PRAZNA_STRANA: Strana<IgraDTO> = {
  values: [],
  strana: 0,
  velicina: 0,
  ukupnoElemenata: 0,
  ukupnoStrana: 0,
};

@Component({
  selector: 'app-katalog',
  imports: [FormsModule, RouterLink, DecimalPipe],
  template: `
    <h1>Katalog igara</h1>

    <div class="filters">
      <input
        placeholder="Pretraga po nazivu"
        [(ngModel)]="naziv"
        (ngModelChange)="promena()"
      />
      <input placeholder="Žanr" [(ngModel)]="zanr" (ngModelChange)="promena()" />
      <select [(ngModel)]="idPlatforma" (ngModelChange)="promena()">
        <option value="">Sve platforme</option>
        @for (p of platforme(); track p.id) {
          <option [value]="p.id">{{ p.naziv }}</option>
        }
      </select>
      <input type="number" placeholder="Min. cena" [(ngModel)]="minCena" (ngModelChange)="promena()" />
      <input type="number" placeholder="Maks. cena" [(ngModel)]="maxCena" (ngModelChange)="promena()" />
      <select [(ngModel)]="sortKljuc" (ngModelChange)="promena()" aria-label="Sortiranje">
        <option value="naziv|rastuce">Naziv: A–Š</option>
        <option value="naziv|opadajuce">Naziv: Š–A</option>
        <option value="cena|rastuce">Cena: od najniže</option>
        <option value="cena|opadajuce">Cena: od najviše</option>
        <option value="godinaIzdanja|opadajuce">Godina: najnovije</option>
        <option value="godinaIzdanja|rastuce">Godina: najstarije</option>
        <option value="kolicinaNaStanju|opadajuce">Zaliha: najveća</option>
      </select>
    </div>

    @if (greska()) {
      <div class="greska">{{ greska() }}</div>
    }

    <div class="grid">
      @for (igra of stranica().values; track igra.id) {
        <a [routerLink]="['/igra', igra.id]" class="detalji-link">
          <div class="igra-kartica">
            <h3>{{ igra.naziv }}</h3>
            <div class="zanr">
              {{ igra.zanr }}{{ igra.godinaIzdanja ? ' · ' + igra.godinaIzdanja : '' }}
            </div>
            @if (igra.izdavac) {
              <div class="zanr">{{ igra.izdavac.naziv }}</div>
            }
            <div class="cena">{{ igra.cena | number: '1.2-2' }} RSD</div>
            @if (igra.prosecnaOcena != null) {
              <div class="zvezdice">★ {{ igra.prosecnaOcena | number: '1.1-1' }}</div>
            }
            <div>
              {{ igra.kolicinaNaStanju > 0 ? 'Na stanju: ' + igra.kolicinaNaStanju : 'Nema na stanju' }}
            </div>
          </div>
        </a>
      } @empty {
        @if (!greska()) {
          <p>Nema pronađenih igara.</p>
        }
      }
    </div>

    @if (stranica().ukupnoStrana > 1) {
      <div class="paginacija">
        <button
          type="button"
          class="sekundarno"
          [disabled]="stranica().strana === 0"
          (click)="idiNaStranu(stranica().strana - 1)"
        >
          ‹ Prethodna
        </button>

        <span class="paginacija-info">
          Strana {{ stranica().strana + 1 }} od {{ stranica().ukupnoStrana }}
          ({{ stranica().ukupnoElemenata }}
          {{ stranica().ukupnoElemenata === 1 ? 'igra' : 'igara' }})
        </span>

        <button
          type="button"
          class="sekundarno"
          [disabled]="stranica().strana + 1 >= stranica().ukupnoStrana"
          (click)="idiNaStranu(stranica().strana + 1)"
        >
          Sledeća ›
        </button>
      </div>
    }
  `,
})
export class KatalogPage implements OnInit {
  private static readonly VELICINA_STRANE = 6;

  private readonly igraService = inject(IgraService);
  private readonly platformaService = inject(PlatformaService);

  protected naziv = '';
  protected zanr = '';
  protected idPlatforma = '';
  protected minCena = '';
  protected maxCena = '';
  protected sortKljuc = 'naziv|rastuce';

  protected readonly stranica = signal<Strana<IgraDTO>>(PRAZNA_STRANA);
  protected readonly platforme = signal<PlatformaDTO[]>([]);
  protected readonly greska = signal('');

  private brojStrane = 0;
  private readonly promene = new Subject<void>();

  ngOnInit(): void {
    this.platformaService.sve().subscribe({
      next: (p) => this.platforme.set(p),
      error: () => {},
    });

    this.promene
      .pipe(
        debounceTime(250),
        switchMap(() => {
          const [polje, smer] = this.sortKljuc.split('|');
          return this.igraService
            .filter(
              {
                naziv: this.naziv,
                zanr: this.zanr,
                idPlatforma: this.idPlatforma,
                minCena: this.minCena,
                maxCena: this.maxCena,
              },
              { polje: polje as PoljeSortiranja, smer: smer as SmerSortiranja },
              this.brojStrane,
              KatalogPage.VELICINA_STRANE
            )
            .pipe(
              map((stranica) => ({ stranica, greska: '' })),
              catchError((e: Error) => of({ stranica: PRAZNA_STRANA, greska: e.message }))
            );
        })
      )
      .subscribe(({ stranica, greska }) => {
        this.stranica.set(stranica);
        this.greska.set(greska);
      });

    this.promena();
  }

  protected promena(): void {
    this.brojStrane = 0;
    this.promene.next();
  }

  protected idiNaStranu(strana: number): void {
    this.brojStrane = strana;
    this.promene.next();
  }
}
