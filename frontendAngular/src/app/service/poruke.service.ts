import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class PorukeService {
  private static readonly TRAJANJE_MS = 5000;

  private readonly _uspeh = signal('');
  private readonly _greska = signal('');
  private tajmer?: ReturnType<typeof setTimeout>;

  readonly uspeh = this._uspeh.asReadonly();
  readonly greska = this._greska.asReadonly();

  prikaziUspeh(poruka: string): void {
    this.postavi(poruka, '');
  }

  prikaziGresku(poruka: string): void {
    this.postavi('', poruka);
  }

  ocisti(): void {
    clearTimeout(this.tajmer);
    this._uspeh.set('');
    this._greska.set('');
  }

  private postavi(uspeh: string, greska: string): void {
    clearTimeout(this.tajmer);
    this._uspeh.set(uspeh);
    this._greska.set(greska);
    this.tajmer = setTimeout(() => this.ocisti(), PorukeService.TRAJANJE_MS);
  }
}
