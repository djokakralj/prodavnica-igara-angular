import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { KorpaDTO } from '../model/modeli';
import { ApiService, Rezultat } from './api.service';

@Injectable({ providedIn: 'root' })
export class KorpaService {
  private readonly api = inject(ApiService);

  pregled(): Observable<KorpaDTO> {
    return this.api.getValue<KorpaDTO>('/korpa/all');
  }

  dodaj(idIgra: number, kolicina: number): Observable<Rezultat<KorpaDTO>> {
    return this.api.postRezultat<KorpaDTO>('/korpa/add', { idIgra, kolicina });
  }

  izmeni(idIgra: number, kolicina: number): Observable<Rezultat<KorpaDTO>> {
    return this.api.postRezultat<KorpaDTO>('/korpa/update', { idIgra, kolicina });
  }

  ukloni(idIgra: number): Observable<Rezultat<KorpaDTO>> {
    return this.api.postRezultat<KorpaDTO>('/korpa/delete', idIgra);
  }
}
