# Eingebaute WCAG-Verstöße – „Zum Goldenen Maulwurf"

Diese Demo-Site sieht aus wie eine echte Wiener Kaffeehaus-Website, enthält aber
gezielt eingestreute Barrierefreiheits-Verstöße als Testziel für A11yForge.

**Alle Angaben in diesem Dokument sind gegen axe-core 4.11.4 verifiziert**
(dieselbe Version, die der Scanner bündelt), Viewport 1280×720.

Das A11yForge-Tool scannt nur **fünf Regeln**:
`image-alt`, `color-contrast`, `label`, `html-has-lang`, `heading-order`.
Weitere hier eingebaute Verstöße (`link-name`, `button-name`, `listitem`,
`select-name`) tauchen erst bei einem **vollständigen axe-Lauf** auf – sie sorgen
für ein realistisches Gesamtbild, werden vom Tool aber nicht gemeldet/gefixt.

> ⚠️ **Wichtig – drei „Verstöße" aus dem Konzept lösen bei axe bewusst NICHT aus**
> (siehe Abschnitt [„Nicht von axe erkennbar"](#nicht-von-axe-erkennbar)):
> schlechte/leere `alt`-Werte und placeholder-only-Felder. Die Markup-Muster sind
> trotzdem vorhanden (echte Praxis-Fehler), nur die Regel greift nicht.

---

## index.html

| Regel-ID | Element / Position | Was genau falsch ist | Erwartete axe-Meldung |
| --- | --- | --- | --- |
| `html-has-lang` | `<html>` | lang-Attribut fehlt komplett | „`<html>` element must have a `lang` attribute" |
| `image-alt` | Sektion „Unser Haus", `cafe-counter-display.jpg` | `<img>` ganz ohne `alt`-Attribut | „Images must have alternate text" |
| `color-contrast` | Hero, Untertitel unter der h1 | Text `#A9A9A9` auf `--color-cream` → ~2,2:1 | „Elements must meet minimum color contrast ratio" |
| `color-contrast` | Footer, 3 Kontakt-Links (Tel, E-Mail, Anfahrt) | `#8F9A92` auf Footer-Fläche → ~2,5:1 (3 Instanzen) | color-contrast (3×) |
| `button-name` *(nur Voll-axe)* | Header, Menü-Icon-Button | `<button>` nur mit SVG, kein zugänglicher Name | „Buttons must have discernible text" |
| `link-name` *(nur Voll-axe)* | Footer, 3 Social-Icons | `<a>` nur mit Inline-SVG, kein Text/`aria-label` (3×) | „Links must have discernible text" |

**Korrekt (nicht anfassen):** `cafe-interior-dining-room.jpg`
(`alt="Gastraum mit Holztischen und Fensterfront"`), Hero-Bild.

## speisekarte.html

| Regel-ID | Element / Position | Was genau falsch ist | Erwartete axe-Meldung |
| --- | --- | --- | --- |
| `heading-order` | 1. Kategorie „Kaffee & Getränke" | `h3` direkt nach der `h1` (h2 übersprungen) | „Heading levels should only increase by one" |
| `heading-order` | Unterüberschrift „Kalt & erfrischend" | `h5` direkt nach der `h3`-Kategorie (h4 übersprungen) | heading-order |
| `image-alt` | Mehlspeisen, `croissant-pastry.jpg` (Butterkipferl) | `<img>` ganz ohne `alt`-Attribut | image-alt |
| `color-contrast` | **alle** Preisangaben (Karten + Textzeilen) | `#C9A28C` auf `#FFFFFF` → ~2,3:1 (20 Instanzen) | color-contrast (20×) |
| `color-contrast` | Footer-Links | wie index (3×) | color-contrast (3×) |
| `listitem` *(nur Voll-axe)* | Kategorie „Herzhaftes" | zwei `<li>` direkt im `<div>`, kein umschließendes `<ul>` (2×) | „`<li>` elements must be contained in a `<ul>`/`<ol>`" |
| `button-name` / `link-name` *(nur Voll-axe)* | Header / Footer | wie index | button-name (1×), link-name (3×) |

**Korrekt (nicht anfassen):** `cappuccino-cup.jpg`
(`alt="Cappuccino mit Milchschaumherz in blauer Tasse"`),
`croissant-filled-close-up.jpg` (aussagekräftiges alt).

## team.html

| Regel-ID | Element / Position | Was genau falsch ist | Erwartete axe-Meldung |
| --- | --- | --- | --- |
| `heading-order` | Personen-Karten | Karten-Titel als `h4` direkt nach der `h1`; axe meldet den ersten Sprung | heading-order (1×) |
| `color-contrast` | Footer-Links | wie index (3×) | color-contrast (3×) |
| `button-name` / `link-name` *(nur Voll-axe)* | Header / Footer | wie index | button-name (1×), link-name (3×) |

## kontakt.html

| Regel-ID | Element / Position | Was genau falsch ist | Erwartete axe-Meldung |
| --- | --- | --- | --- |
| `label` | Reservierung, Feld „Name" | sichtbares `<span>` statt `<label for>`, keine Zuordnung | „Form elements must have labels" |
| `label` | Reservierung, Feld „E-Mail" | `<input type=email>` mit `<span>` statt zugeordnetem Label | label |
| `label` | Reservierung, Feld „Uhrzeit" | `<input type=time>` mit `<span>` statt zugeordnetem Label | label |
| `color-contrast` | Absende-Button | Text `#E8D9CE` auf `--color-terracotta` → ~3,2:1 | color-contrast |
| `color-contrast` | Footer-Links | wie index (3×) | color-contrast (3×) |
| `select-name` *(nicht in den 5 Tool-Regeln)* | Reservierung, Feld „Personenzahl" | `<select>` ohne Label, davor nur ein `<span>` | „Select element must have an accessible name" |
| `button-name` / `link-name` *(nur Voll-axe)* | Header / Footer | wie index | button-name (1×), link-name (3×) |

**Korrekt (nicht anfassen):** Feld „Wunschdatum" (`<label for="datum">`),
Feld „Anmerkungen" (`<label for="nachricht">`).

---

## Nicht von axe erkennbar

Drei im Konzept genannte Fälle sind als **echte** Praxis-Fehler im Markup
vorhanden, lösen bei axe aber **keine** Meldung aus. Bewusst so belassen (Realismus,
manuelle Prüfung würde sie finden) – hier dokumentiert, damit die Erwartung stimmt:

| Fall | Position | Warum keine axe-Meldung |
| --- | --- | --- |
| `alt="IMG_4821.jpg"` (Dateiname) | speisekarte, `chocolate-cake-slice.jpg` | `image-alt` prüft nur, **ob** ein `alt` existiert, nicht dessen Qualität |
| `alt="bild"` (nichtssagend) | speisekarte, `breakfast-coffee-croissant.jpg` | dito |
| `alt=""` obwohl inhaltstragend | team, `barista-latte-art.jpg` | leeres `alt` gilt für axe als „dekorativ" → gültig |

## Bewusste Abweichungen vom Konzept (mit Grund)

1. **Füllflächen `--color-sage-dark` statt `--color-sage`.** Weißer/cremefarbener
   Text auf `--color-sage` (#6F8577) erreicht nur ~3,96:1 und würde flächendeckend
   *unbeabsichtigt* scheitern (Header-Nav, Footer, Öffnungszeiten-Block, Primär-Button).
   Auf `--color-sage-dark` (#4A5B50) liegt derselbe Text bei ~7:1 → sauber.
   `--color-sage` bleibt als Akzent/Hover/Border. Der gewollte Footer-Link-Verstoß
   (`#8F9A92`) scheitert auf der dunkleren Fläche weiterhin (~2,5:1).
2. **Terracotta-Text via `--color-terracotta-ink` (#9C4F2F).** `--color-terracotta`
   (#B5613C) erreicht als *Text* auf Creme/Weiß nur ~4,1–4,4:1 (knapp unter 4,5) und
   würde bei Eyebrows, Preisen und Rollen unbeabsichtigt scheitern. Als Fläche/Border/
   Badge (inkl. Absende-Button-Hintergrund) bleibt `--color-terracotta` unverändert.
3. **label-Felder mit `<span>`-Pseudo-Label statt „nur placeholder".** axe-core 4.11
   wertet einen `placeholder` als zugänglichen Namen → placeholder-only löst `label`
   **nicht** aus. Damit der beabsichtigte Verstoß erkennbar bleibt (und das Formular
   professionell aussieht), tragen Name/E-Mail/Uhrzeit ein sichtbares, aber nicht
   zugeordnetes `<span>` – derselbe reale Fehler, aber von axe erfasst.

---

## Zusammenfassung – Verstöße je Regel

**Vom A11yForge-Tool gemeldet (5 Regeln):**

| Regel | index | speisekarte | team | kontakt | Summe |
| --- | :-: | :-: | :-: | :-: | :-: |
| `image-alt` | 1 | 1 | – | – | **2** |
| `color-contrast` | 4 | 23 | 3 | 4 | **34** |
| `label` | – | – | – | 3 | **3** |
| `html-has-lang` | 1 | – | – | – | **1** |
| `heading-order` | – | 2 | 1 | – | **3** |
| **Summe/Seite** | **6** | **26** | **4** | **7** | **43** |

*(`color-contrast` enthält je Seite 3 Footer-Links; speisekarte zusätzlich 20 Preise.)*

**Zusätzlich bei vollständigem axe-Lauf (nicht in den 5 Tool-Regeln):**

| Regel | Summe |
| --- | :-: |
| `link-name` | 12 (3 je Seite) |
| `button-name` | 4 (1 je Seite) |
| `listitem` | 2 (speisekarte) |
| `select-name` | 1 (kontakt) |

---

## Lokal servieren

`file://` funktioniert **nicht** – der Same-Origin-Crawler folgt dann den relativen
Links nicht. Über einen lokalen HTTP-Server ausliefern:

```bash
npx serve scanner/src/tests/demo-site
```

Danach die ausgegebene URL (z. B. `http://localhost:3000`) im Scanner als Ziel
angeben. Alle vier Seiten sind same-origin verlinkt und werden vom Crawler erreicht.
