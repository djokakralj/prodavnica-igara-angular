import { Routes } from '@angular/router';
import { kupacGuard } from './guard/kupac.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/katalog/katalog').then((m) => m.KatalogPage),
  },
  {
    path: 'igra/:id',
    loadComponent: () => import('./pages/detalji-igre/detalji-igre').then((m) => m.DetaljiIgrePage),
  },
  {
    path: 'prijava',
    loadComponent: () => import('./pages/prijava/prijava').then((m) => m.PrijavaPage),
  },
  {
    path: 'registracija',
    loadComponent: () =>
      import('./pages/registracija/registracija').then((m) => m.RegistracijaPage),
  },
  {
    path: 'korpa',
    canActivate: [kupacGuard],
    loadComponent: () => import('./pages/korpa/korpa').then((m) => m.KorpaPage),
  },
  {
    path: 'moje-narudzbine',
    canActivate: [kupacGuard],
    loadComponent: () =>
      import('./pages/moje-narudzbine/moje-narudzbine').then((m) => m.MojeNarudzbinePage),
  },
  { path: '**', redirectTo: '' },
];
