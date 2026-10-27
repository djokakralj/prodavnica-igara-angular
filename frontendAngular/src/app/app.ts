import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './components/navbar/navbar';
import { PorukeService } from './service/poruke.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar],
  template: `
    <app-navbar />

    @if (poruke.uspeh() || poruke.greska()) {
      <div class="obavestenja">
        @if (poruke.uspeh()) {
          <div class="poruka-ok" (click)="poruke.ocisti()">{{ poruke.uspeh() }}</div>
        }
        @if (poruke.greska()) {
          <div class="greska" (click)="poruke.ocisti()">{{ poruke.greska() }}</div>
        }
      </div>
    }

    <div class="container">
      <router-outlet />
    </div>
  `,
})
export class App {
  protected readonly poruke = inject(PorukeService);
}
