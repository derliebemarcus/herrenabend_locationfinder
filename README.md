# Herrenabend Locationfinder

Ein moderner Locationfinder für Herrenabende, der über die Google Places API nach Bars und Cafés sucht und eine zufällige, passende Location für den Abend auswählt. 

## Features

- **Responsive & Modern:** Single Page Application (SPA) basierend auf Astro, React und Tailwind CSS v4.
- **Dark & Light Mode:** Automatischer Theme-Wechsel über `prefers-color-scheme`.
- **Deeplinking:** Einfaches Teilen von Locations, da die gewählte `placeId` in der URL hinterlegt wird.
- **Native Share-Funktion:** Unterstützt die Web Share API für Mobile Devices (schnelles Teilen über WhatsApp, Telegram, etc.).
- **Smart Filtering:** Schließt unpassende Kategorien wie Nachtclubs, Kunstgalerien oder Hotels automatisch aus.
- **Barrierefrei:** Entspricht grundlegenden WCAG-Richtlinien (Kontraste, Tastatur-Bedienbarkeit).

## Voraussetzungen

Du benötigst einen Google Maps / Google Places API Key.

1. Erstelle eine `.env` Datei im Hauptverzeichnis.
2. Füge den folgenden Schlüssel ein:
   ```env
   PUBLIC_GOOGLE_PLACES_API_KEY=Dein_API_Key_hier
   ```

## Installation & Start

1. Installiere die Abhängigkeiten:
   ```bash
   npm install
   ```

2. Starte den lokalen Entwicklungsserver:
   ```bash
   npm run dev
   ```

Die Anwendung ist standardmäßig unter `http://localhost:4321` erreichbar.

## Dokumentation

Die vollständige Architektur- und Entwicklerdokumentation findest du unter [docs/index.md](docs/index.md).
Dieses Repository folgt dem **Repository Documentation Standard v1** (Application Profile).

## Deployment

Da es sich um eine statische SPA handelt, lässt sie sich problemlos auf Plattformen wie GitHub Pages, Netlify oder Vercel hosten. Bei Vercel oder Netlify einfach das Repository verknüpfen und den Build-Befehl `npm run build` ausführen lassen. Wichtig: Der API Key muss in den Build-Umgebungsvariablen hinterlegt werden.
