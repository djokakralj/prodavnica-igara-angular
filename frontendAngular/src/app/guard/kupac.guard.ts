import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AutentifikacijaService } from '../service/autentifikacija.service';

export const kupacGuard: CanActivateFn = () => {
  const auth = inject(AutentifikacijaService);
  const router = inject(Router);

  if (auth.jeKupac()) {
    return true;
  }
  return router.createUrlTree([auth.jePrijavljen() ? '/' : '/prijava']);
};
