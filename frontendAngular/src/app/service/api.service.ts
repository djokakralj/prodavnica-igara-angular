import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Strana } from '../model/modeli';

export interface Odgovor<T> {
  message: string;
  data?: {
    value?: T;
    values?: T[];
    strana?: number;
    velicina?: number;
    ukupnoElemenata?: number;
    ukupnoStrana?: number;
  };
  status: string;
}

export interface Rezultat<T> {
  poruka: string;
  vrednost: T;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api';

  getValue<T>(putanja: string, params?: HttpParams): Observable<T> {
    return this.http
      .get<Odgovor<T>>(this.base + putanja, { params })
      .pipe(map((o) => o.data?.value as T));
  }

  getValues<T>(putanja: string, params?: HttpParams): Observable<T[]> {
    return this.http
      .get<Odgovor<T>>(this.base + putanja, { params })
      .pipe(map((o) => o.data?.values ?? []));
  }

  getStranu<T>(putanja: string, params?: HttpParams): Observable<Strana<T>> {
    return this.http.get<Odgovor<T>>(this.base + putanja, { params }).pipe(
      map((o) => ({
        values: o.data?.values ?? [],
        strana: o.data?.strana ?? 0,
        velicina: o.data?.velicina ?? 0,
        ukupnoElemenata: o.data?.ukupnoElemenata ?? 0,
        ukupnoStrana: o.data?.ukupnoStrana ?? 0,
      }))
    );
  }

  postValue<T>(putanja: string, telo?: unknown): Observable<T> {
    return this.http
      .post<Odgovor<T>>(this.base + putanja, telo ?? null)
      .pipe(map((o) => o.data?.value as T));
  }

  postRezultat<T>(putanja: string, telo?: unknown): Observable<Rezultat<T>> {
    return this.http
      .post<Odgovor<T>>(this.base + putanja, telo ?? null)
      .pipe(map((o) => ({ poruka: o.message, vrednost: o.data?.value as T })));
  }

  postMessage(putanja: string, telo?: unknown): Observable<string> {
    return this.http
      .post<Odgovor<unknown>>(this.base + putanja, telo ?? null)
      .pipe(map((o) => o.message));
  }

  static params(vrednosti: Record<string, string | undefined>): HttpParams {
    let params = new HttpParams();
    Object.entries(vrednosti).forEach(([kljuc, v]) => {
      if (v !== undefined && v !== null && v !== '') {
        params = params.set(kljuc, v);
      }
    });
    return params;
  }
}
