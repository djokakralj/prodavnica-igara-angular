import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { Injector, inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AutentifikacijaService } from './autentifikacija.service';

export const kolacicInterceptor: HttpInterceptorFn = (req, next) =>
  next(req.clone({ withCredentials: true }));

export const greskaInterceptor: HttpInterceptorFn = (req, next) => {
  const injector = inject(Injector);

  return next(req).pipe(
    catchError((greska: HttpErrorResponse) => {
      if (greska.status === 401 && !req.url.includes('/auth/')) {
        injector.get(AutentifikacijaService).ocistiLokalnoStanje();
      }

      const poruka =
        greska.error?.message ??
        (greska.status === 0
          ? 'Backend nije dostupan. Da li je Tomcat pokrenut na portu 8080?'
          : 'Došlo je do greške.');
      return throwError(() => new Error(poruka));
    })
  );
};
