import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { FilterIgara, IgraDTO, Sortiranje, Strana } from '../model/modeli';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class IgraService {
  private readonly api = inject(ApiService);

  sve(): Observable<IgraDTO[]> {
    return this.api.getValues<IgraDTO>('/igre/all');
  }

  filter(
    filteri: FilterIgara,
    sortiranje: Sortiranje,
    strana: number,
    velicina: number
  ): Observable<Strana<IgraDTO>> {
    return this.api.getStranu<IgraDTO>(
      '/igre/filter',
      ApiService.params({
        ...filteri,
        sort: sortiranje.polje,
        smer: sortiranje.smer,
        strana: String(strana),
        velicina: String(velicina),
      })
    );
  }

  detalji(id: number | string): Observable<IgraDTO> {
    return this.api.getValue<IgraDTO>(`/igre/getIgra/${id}`);
  }
}
