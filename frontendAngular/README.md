# Prodavnica igara — Angular frontend

Druga varijanta klijenta, uz postojeći React (`frontend/`). Obe rade sa istim
backendom i mogu se koristiti naizmenično — samo ne istovremeno na istom portu.

- **Angular 20**, standalone komponente, signali, `@if` / `@for` sintaksa
- Dev server na **`http://localhost:4200`**
- Zahtevi ka `/api/*` idu kroz proxy na backend (`http://localhost:8080`)

## Pokretanje

Backend mora biti pokrenut (Tomcat 10.1+ na portu 8080) i MySQL dostupan.

```bash
cd frontendAngular
npm install
npm start
```

Aplikacija se otvara na `http://localhost:4200`.

## Testovi

```bash
npm test -- --watch=false --browsers=ChromeHeadless
```

Ako nemaš Chrome, postavi `CHROME_BIN` na neki Chromium pregledač, npr. Edge:

```bash
CHROME_BIN="/c/Program Files (x86)/Microsoft/Edge/Application/msedge.exe" \
  npm test -- --watch=false --browsers=ChromeHeadless
```

## Struktura

```
src/app/
├── model/modeli.ts        TypeScript parnjaci DTO klasa sa backenda
├── service/
│   ├── api.service.ts             raspakuje {message, data:{value|values}, status}
│   ├── interceptori.ts            kolačići (sesija) + prevod grešaka u Error.message
│   ├── autentifikacija.service.ts trenutni korisnik, u signalima
│   └── igra / platforma / korpa / narudzbina / recenzija .service.ts
├── guard/kupac.guard.ts   štiti /korpa i /moje-narudzbine
├── components/navbar/
├── pages/                 katalog, detalji-igre, korpa, moje-narudzbine, prijava, registracija
├── app.config.ts          HttpClient + interceptori + APP_INITIALIZER (ko je prijavljen)
└── app.routes.ts          lenjo učitavanje stranica
```

## Zašto proxy

Angular dev server radi na portu 4200, backend na 8080 — to su različiti origin-i.
`proxy.conf.json` prosleđuje sve `/api/*` zahteve na 8080, pa je za pregledač sve
isti origin. Time nema CORS-a i `JSESSIONID` kolačić se prenosi normalno.

Ako bi se ipak išlo direktno na `http://localhost:8080` bez proxy-ja, backend bi
morao da dozvoli origin `http://localhost:4200` u `@CrossOrigin` anotacijama
kontrolera (sada stoji `http://localhost:3000`, zbog React-a).

## Odnos prema React verziji

Ista funkcionalnost, isti API pozivi, isti CSS. Razlike su idiomatske:

| React (`frontend/`) | Angular (`frontendAngular/`) |
|---|---|
| `client.js` sa `fetch` | `ApiService` sa `HttpClient` |
| `credentials: "include"` po pozivu | `kolacicInterceptor` globalno |
| `try/catch` + `e.message` | `greskaInterceptor` globalno |
| `AuthContext` + `useState` | `AutentifikacijaService` + signali |
| `useEffect` za učitavanje | `ngOnInit` + `subscribe` |
| `setTimeout` za debounce | `Subject` + `debounceTime` + `switchMap` |
| `ZahtevaKupca` komponenta | `kupacGuard` (`CanActivateFn`) |
