<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isPrivacy = computed(() => route.path === '/datenschutz')
</script>

<template>
  <div class="max-w-[720px] mx-auto px-6 pt-8 pb-12 text-[var(--text)] font-sans">
    <template v-if="!isPrivacy">
      <h1 class="text-[1.7rem] font-extrabold text-[var(--text)] mt-0 mb-[1.2rem]">Impressum</h1>
      <div class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] px-[1.6rem] py-6 leading-[1.8]">
        <p class="mt-0 mb-[0.6rem] text-[var(--muted)]"><strong class="text-[var(--text)]">Name / Verantwortlich:</strong> &lt;Vor- und Nachname&gt;</p>
        <p class="mt-0 mb-[0.6rem] text-[var(--muted)]"><strong class="text-[var(--text)]">Adresse:</strong> &lt;Straße Hausnummer, PLZ Ort, Österreich&gt;</p>
        <p class="mt-0 mb-[0.6rem] text-[var(--muted)]"><strong class="text-[var(--text)]">Kontakt (E-Mail):</strong> &lt;name@example.com&gt;</p>
        <p class="mt-[1.2rem] mb-0 text-[var(--muted)]">
          a11yforge ist ein nicht-kommerzielles Abschlussprojekt im Rahmen der Ausbildung bei
          Coders Bay. Das Tool wird ohne Gewinnerzielungsabsicht betrieben und richtet sich
          nicht an die Allgemeinheit.
        </p>
      </div>
    </template>

    <template v-else>
      <h1 class="text-[1.7rem] font-extrabold text-[var(--text)] mt-0 mb-[1.2rem]">Datenschutz</h1>
      <div class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] px-[1.6rem] py-6 leading-[1.8]">
        <p class="mt-0 mb-[0.9rem] text-[var(--muted)]">
          <strong class="text-[var(--text)]">Welche Daten:</strong>
          Account-Daten (E-Mail-Adresse, Benutzername, Passwort ausschließlich als BCrypt-Hash),
          angelegte Projekte (Name, Start-URL) sowie Scan-Ergebnisse. Zu den Scan-Ergebnissen
          zählt auch das gerenderte HTML der gecrawlten Seiten samt der gefundenen Befunde.
        </p>
        <p class="mt-0 mb-[0.9rem] text-[var(--muted)]">
          <strong class="text-[var(--text)]">Zweck:</strong>
          Betrieb des Tools, Authentifizierung der Anmeldung sowie Anzeige und Auswertung der
          eigenen Scan-Historie. Es findet keine Auswertung zu Werbezwecken statt, und die Daten
          werden nicht an Dritte verkauft.
        </p>
        <p class="mt-0 mb-[0.9rem] text-[var(--muted)]">
          <strong class="text-[var(--text)]">Speicherung &amp; Löschung:</strong>
          Alle Daten liegen in einer lokal betriebenen PostgreSQL-Datenbank. Wird ein Projekt
          gelöscht, werden die zugehörigen Scans, Seiten, Befunde und Fix-Vorschläge automatisch
          mitgelöscht.
        </p>
        <p class="mt-0 mb-[0.9rem] text-[var(--muted)]">
          <strong class="text-[var(--text)]">Externe Verarbeitung:</strong>
          Für die Erzeugung von Fix-Vorschlägen werden HTML-Ausschnitte der betroffenen Stelle an
          den im Scan gewählten LLM-Anbieter übermittelt. Bei Auswahl von Anthropic verlassen
          diese Ausschnitte das System; bei Auswahl eines lokalen Ollama-Modells bleibt die
          Verarbeitung auf dem eigenen Rechner.
        </p>
        <p class="mt-0 mb-0 text-[var(--muted)]">
          <strong class="text-[var(--text)]">Kontakt für Auskunft:</strong> &lt;name@example.com&gt;
        </p>
      </div>
    </template>
  </div>
</template>
