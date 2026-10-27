import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { NarudzbinaDTO } from '../model/modeli';
import { ApiService, Rezultat } from './api.service';

@Injectable({ providedIn: 'root' })
export class NarudzbinaService {
  private readonly api = inject(ApiService);

  sve(): Observable<NarudzbinaDTO[]> {
    return this.api.getValues<NarudzbinaDTO>('/narudzbine/all');
  }

  detalji(id: number): Observable<NarudzbinaDTO> {
    return this.api.getValue<NarudzbinaDTO>(`/narudzbine/getNarudzbina/${id}`);
  }

  napravi(): Observable<Rezultat<NarudzbinaDTO>> {
    return this.api.postRezultat<NarudzbinaDTO>('/narudzbine/add');
  }

  /**
   * Putanja do PDF-a. Ne ide kroz HttpClient nego kao obican link, da preuzimanje
   * odradi sam pretrazivac -- zahtev je istog porekla (dev proxy), pa nosi i
   * kolacic sesije koji backend trazi.
   */
  pdfPutanja(id: number): string {
    return `/api/narudzbine/pdf/${id}`;
  }
}
