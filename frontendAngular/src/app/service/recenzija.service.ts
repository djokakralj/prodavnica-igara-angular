import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { RecenzijaDTO } from '../model/modeli';
import { ApiService, Rezultat } from './api.service';

@Injectable({ providedIn: 'root' })
export class RecenzijaService {
  private readonly api = inject(ApiService);

  zaIgru(idIgra: number | string): Observable<RecenzijaDTO[]> {
    return this.api.getValues<RecenzijaDTO>(`/recenzije/zaIgru/${idIgra}`);
  }

  dodaj(idIgra: number, ocena: number, komentar: string): Observable<Rezultat<RecenzijaDTO>> {
    return this.api.postRezultat<RecenzijaDTO>('/recenzije/add', { idIgra, ocena, komentar });
  }

  izmeni(id: number, ocena: number, komentar: string): Observable<Rezultat<RecenzijaDTO>> {
    return this.api.postRezultat<RecenzijaDTO>('/recenzije/update', { id, ocena, komentar });
  }

  obrisi(id: number): Observable<string> {
    return this.api.postMessage('/recenzije/delete', id);
  }
}
