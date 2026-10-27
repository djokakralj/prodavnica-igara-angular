import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { PlatformaDTO } from '../model/modeli';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class PlatformaService {
  private readonly api = inject(ApiService);

  sve(): Observable<PlatformaDTO[]> {
    return this.api.getValues<PlatformaDTO>('/platforme/all');
  }
}
