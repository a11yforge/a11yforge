# a11yforge – Projektstruktur

## Monorepo-Übersicht

```
a11yforge/
├── .github/
│   └── workflows/          # CI/CD Pipelines (GitHub Actions)
├── docker/
│   └── docker-compose.yml  # PostgreSQL & Services lokal hochfahren
├── docs/
│   ├── adr/                # Architecture Decision Records
│   ├── images/             # Dokumentationsbilder
│   └── postman/
│       └── A11yForge.postman_collection.json
├── backend/                # Spring Boot REST API (Java)
├── frontend/               # Vue 3 SPA (TypeScript)
├── scanner/                # Accessibility-Scanner CLI (Node.js/TypeScript)
├── PROJECT_CONTEXT.md      # Projektbeschreibung & Kontext
└── PROJECT_STRUCTURE.md    # Diese Datei
```

---

## Backend (`backend/`)

Spring Boot 3, Java 21, Maven, PostgreSQL, Flyway, JWT-Auth

```
backend/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/at/a11yforge/
│   │   │   ├── A11yforgeApplication.java       # Spring Boot Entry Point
│   │   │   └── api/
│   │   │       ├── auditevent/                 # Audit-Log: Entity, Repo, Service, DTO, Enum
│   │   │       ├── auth/                       # Login & Registrierung (Controller, DTOs)
│   │   │       ├── exception/                  # GlobalExceptionHandler
│   │   │       ├── fixcache/                   # Fix-Cache: Entity, Repo, Service, DTO
│   │   │       ├── fixproposal/                # Fix-Vorschläge: Entity, Controller, Service, Repo,
│   │   │       │                               #   FixGenerationService, FixGenerationAsyncRunner,
│   │   │       │                               #   FixGenerationRequestedEvent, DTOs, Status-Enum
│   │   │       ├── llm/                        # LLM-Abstraktion:
│   │   │       │   ├── ChatProvider.java        #   Interface für alle Provider
│   │   │       │   ├── ChatProviderFactory.java #   Factory (Auswahl via ProviderType)
│   │   │       │   ├── AnthropicChatProvider.java
│   │   │       │   ├── AnthropicClientConfig.java
│   │   │       │   ├── OllamaChatProvider.java
│   │   │       │   ├── OllamaRequestDTO.java / OllamaResponseDTO.java
│   │   │       │   ├── NoOpChatProvider.java
│   │   │       │   ├── ProviderType.java        #   Enum: ANTHROPIC, OLLAMA, NONE
│   │   │       │   ├── PromptLoader.java        #   Lädt Prompt-Templates aus Resources
│   │   │       │   ├── FixTagExtractor.java     #   Extrahiert <fix>-Tag aus LLM-Antwort
│   │   │       │   ├── AsyncConfig.java         #   Spring @EnableAsync-Konfiguration
│   │   │       │   └── FixGenerationRequest/ResponseDTO.java
│   │   │       ├── page/                       # Page-Entity (gescannte Seite): Controller, Service, Repo, DTOs, Status
│   │   │       ├── project/                    # Projekt-Verwaltung: Controller, Service, Repo, DTOs, Exceptions
│   │   │       ├── review/                     # Review/Approval-Flow: Entity, Controller, Service, Repo, DTOs, Decision-Enum
│   │   │       ├── scan/                       # Scan-Auftrag: Controller, Service, Repo, DTOs, Status, Detail-DTO
│   │   │       ├── scanner/                    # Subprocess-Wrapper für Scanner-CLI: ProcessRunner, Result-DTOs
│   │   │       ├── security/                   # JWT-Filter, JwtUtil, SecurityConfig, CustomUserDetails
│   │   │       ├── user/                       # User-Verwaltung: Entity, Controller (leer), Service, Repo, DTOs, Exceptions
│   │   │       ├── verifier/                   # Subprocess-Wrapper für Verifier-CLI: ProcessRunner, Request/Result-DTOs
│   │   │       └── violation/                  # Accessibility-Verletzung: Entity, Controller, Service, Repo, DTOs, Enums (Impact, ViolationSource)
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-local.properties    # Lokale Overrides (DB-Credentials etc.)
│   │       └── db/migration/                   # Flyway-Migrations
│   │           ├── V1__create_user_account.sql
│   │           ├── V2__create_project.sql
│   │           ├── V3__scan.sql
│   │           ├── V4__page.sql
│   │           ├── V5__violation.sql
│   │           ├── V6__fix_cache.sql
│   │           ├── V7__fix_proposal.sql
│   │           ├── V8__review.sql
│   │           ├── V9__audit_event.sql
│   │           ├── V10__add_llm_provider_to_scan.sql
│   │           ├── V11__relax_fix_proposal_nullability.sql
│   │           └── V12__cascade_delete.sql
│   └── test/
│       └── java/at/a11yforge/
│           ├── A11yforgeApplicationTests.java
│           └── api/
│               ├── fixproposal/FixProposalServiceTest.java
│               ├── scan/ScanServiceTest.java
│               ├── scanner/ScannerProcessRunnerTest.java
│               └── verifyflow/FixGenerationAsyncRunnerTest.java
└── target/                                     # Build-Ausgabe (generiert, nicht committen)
```

### Domänenmodell (Kurzübersicht)

| Entität | Beschreibung |
|---|---|
| `User` | Benutzer mit Rollen |
| `Project` | Barrierefreiheitsprojekt (enthält Scans) |
| `Scan` | Ein Scan-Auftrag für eine URL-Gruppe; hat `llm_provider` |
| `Page` | Einzelne gescannte Seite eines Scans |
| `Violation` | Gefundene Barrierefreiheitsverletzung auf einer Page |
| `FixProposal` | KI-generierter Fix-Vorschlag für eine Violation |
| `FixCache` | Cache für bereits generierte Fix-Vorschläge (SHA-256-Hash) |
| `Review` | Manuelles Review-Ergebnis für einen Fix-Vorschlag |
| `AuditEvent` | Audit-Log über relevante Systemaktionen |

---

## Frontend (`frontend/`)

Vue 3, TypeScript, Vite, Pinia, Vue Router

```
frontend/
├── index.html
├── vite.config.ts
├── tsconfig.json
├── package.json
├── src/
│   ├── main.ts                     # App-Einstiegspunkt
│   ├── App.vue                     # Root-Komponente
│   ├── assets/
│   │   └── main.css
│   ├── api/                        # HTTP-Client-Schicht
│   │   ├── client.ts               # Axios-Instanz, Base URL :8080/api, JWT-Interceptor, 401→Login
│   │   ├── auth.ts                 # login, register, logout
│   │   ├── projects.ts             # Projekt-CRUD-Calls
│   │   ├── project.ts              # (Variante / Duplikat von projects.ts)
│   │   ├── scan.ts                 # startScan(projectId, llmProvider), getScan(id)
│   │   └── fixproposal.ts          # requestFix(violationId), getFixProposal(id)
│   ├── components/
│   │   ├── AppNavbar.vue           # Globale Navigation
│   │   ├── home/
│   │   │   └── WcagCarousel.vue    # WCAG-Regelkarussell auf der Startseite
│   │   ├── project/
│   │   │   ├── ProjectCard.vue
│   │   │   ├── ProjectFormDialog.vue
│   │   │   └── ProjectList.vue
│   │   └── scan/
│   │       ├── ScanDetail.vue      # Violations-Liste, Request-Fix-Button, Before/After-Diff, Polling
│   │       └── UrlInputForm.vue    # Scan starten (URL + LLM-Provider-Auswahl)
│   ├── data/
│   │   └── wcagRules.ts            # Statische WCAG-Regeldaten für UI
│   ├── router/
│   │   └── index.ts                # Vue Router Routen-Definition
│   ├── stores/                     # Pinia State-Management
│   │   ├── auth.ts                 # Auth-State (JWT, email, userName)
│   │   └── projects.ts             # Projekt-State (CRUD, loading, error)
│   └── views/                      # Seitenkomponenten (an Routen gebunden)
│       ├── Home.vue
│       ├── LoginView.vue
│       ├── ProjectsView.vue
│       └── RegisterView.vue
└── public/
    └── favicon.ico
```

---

## Scanner (`scanner/`)

Node.js CLI-Tool (TypeScript), verwendet axe-core + Playwright für Accessibility-Scans

```
scanner/
├── package.json
├── tsconfig.json
├── src/
│   ├── cli.ts                      # CLI-Einstiegspunkt (Scan-Modus)
│   ├── cli-verify.ts               # CLI-Einstiegspunkt (Verify-Modus)
│   ├── core/
│   │   ├── crawl.ts                # BFS-Crawler mit Same-Origin-Filter & URL-Deduplication
│   │   ├── scan.ts                 # Scan-Orchestrierung (axe-core)
│   │   ├── types.ts                # Gemeinsame TypeScript-Typen
│   │   └── verify.ts               # Fix-Verifikations-Logik (axe-core auf gefixter HTML)
│   ├── mapping/
│   │   └── axe.ts                  # Mapping axe-core-Ergebnisse → interne DTOs
│   └── tests/
│       ├── color-contrast.html     # Testseite: Farbkontrast-Verletzung
│       └── missing-alt.html        # Testseite: fehlende Alt-Texte
└── dist/                           # Kompilierte JS-Ausgabe (generiert)
```

---

## Datenfluss (vereinfacht)

```
Browser → Frontend (Vue) → Backend REST API (Spring Boot)
                                    ↓
                            Scanner CLI (Node.js)
                            [axe-core + Playwright]
                                    ↓
                            Violations → DB (PostgreSQL)
                                    ↓
                            Fix Cache Lookup (SHA-256)
                            ↓ Cache Miss          ↓ Cache Hit
                    LLM-Provider              cached_fix_html
                (Anthropic / Ollama)
                            ↓
                    FixProposals (VERIFIED)
                            ↓
                    Fix Cache speichern
                            ↓
                    Review (ACCEPTED / REJECTED)
                            ↓
                    AuditEvent
```
