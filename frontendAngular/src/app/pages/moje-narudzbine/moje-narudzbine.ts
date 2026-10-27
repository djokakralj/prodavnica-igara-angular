import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { NarudzbinaDTO } from '../../model/modeli';
import { NarudzbinaService } from '../../service/narudzbina.service';

@Component({
  selector: 'app-moje-narudzbine',
  imports: [DecimalPipe, DatePipe],
  template: `
    <h1>Moje narudžbine</h1>
    @if (greska()) {
      <div class="greska">{{ greska() }}</div>
    }

    @for (n of narudzbine(); track n.id) {
      <div class="karta">
        <p>
          <strong>Narudžbina #{{ n.id }}</strong> — {{ n.datum | date: 'dd.MM.yyyy. HH:mm' }} —
          status: <strong>{{ n.status }}</strong>
        </p>
        <table class="tabela">
          <thead>
            <tr>
              <th>Igra</th>
              <th>Količina</th>
              <th>Cena po komadu</th>
              <th>Ukupno</th>
            </tr>
          </thead>
          <tbody>
            @for (s of n.stavke; track s.idIgra) {
              <tr>
                <td>{{ s.nazivIgre }}</td>
                <td>{{ s.kolicina }}</td>
                <td>{{ s.cenaPoKomadu | number: '1.2-2' }}</td>
                <td>{{ s.ukupno | number: '1.2-2' }}</td>
              </tr>
            }
          </tbody>
        </table>
        <div class="red-ukupno">
          <strong>Ukupna cena: {{ n.ukupnaCena | number: '1.2-2' }} RSD</strong>
          <a class="dugme" [href]="pdfPutanja(n.id)" [download]="'narudzbina-' + n.id + '.pdf'">
            Preuzmi PDF
          </a>
        </div>
      </div>
    } @empty {
      <p>Još uvek nemate narudžbina.</p>
    }
  `,
})
export class MojeNarudzbinePage implements OnInit {
  private readonly narudzbinaService = inject(NarudzbinaService);

  protected readonly narudzbine = signal<NarudzbinaDTO[]>([]);
  protected readonly greska = signal('');

  ngOnInit(): void {
    this.narudzbinaService.sve().subscribe({
      next: (n) => this.narudzbine.set(n),
      error: (e: Error) => this.greska.set(e.message),
    });
  }

  protected pdfPutanja(id: number): string {
    return this.narudzbinaService.pdfPutanja(id);
  }
}
