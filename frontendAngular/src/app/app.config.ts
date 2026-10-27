import {
  ApplicationConfig,
  inject,
  provideAppInitializer,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { routes } from './app.routes';
import { AutentifikacijaService } from './service/autentifikacija.service';
import { greskaInterceptor, kolacicInterceptor } from './service/interceptori';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withFetch(), withInterceptors([kolacicInterceptor, greskaInterceptor])),

    provideAppInitializer(() => firstValueFrom(inject(AutentifikacijaService).ucitajTrenutnog())),
  ],
};
