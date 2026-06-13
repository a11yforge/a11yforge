<script setup lang="ts"></script>

<template>
  <div class="card">
    <router-link to="/" class="brand" aria-label="Zur Startseite">
      <img class="brand__logo" src="/mole.png" alt="" aria-hidden="true" />
      <span class="brand__name">a11y<span class="brand__accent">forge</span></span>
    </router-link>

    <router-view />
  </div>
</template>

<style scoped>
.card {
  width: 100%;
  max-width: 420px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 2rem 1.9rem 1.8rem;
  box-shadow: 0 24px 60px rgba(0, 0, 0, 0.45);
}

.brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.6rem;
  margin-bottom: 1.6rem;
  text-decoration: none;
  color: var(--text);
}
.brand__logo {
  width: 40px;
  height: 40px;
  border-radius: 11px;
  object-fit: cover;
  object-position: center 18%;
  border: 1px solid var(--border);
  image-rendering: pixelated;
}
.brand__name {
  font-weight: 800;
  font-size: 1.25rem;
  letter-spacing: 0.2px;
}
.brand__accent {
  color: var(--coral);
}

/* --- PrimeVue + Inhalt der Auth-Forms an die Sunset-Palette angleichen --- */
:deep(h2) {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 1.1rem;
  color: var(--text);
}

:deep(.p-inputtext),
:deep(.p-password),
:deep(.p-password-input) {
  width: 100%;
}
:deep(.p-inputtext),
:deep(.p-password-input) {
  background: var(--field);
  border: 1px solid var(--border);
  color: var(--text);
  border-radius: 10px;
  padding: 0.7rem 0.9rem;
}
:deep(.p-inputtext::placeholder),
:deep(.p-password-input::placeholder) {
  color: var(--muted);
}
:deep(.p-inputtext:enabled:focus),
:deep(.p-password-input:enabled:focus) {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
  border-color: var(--teal);
  box-shadow: none;
}

:deep(.p-button) {
  background: var(--coral);
  border: 1px solid var(--coral);
  color: #2a1410;
  font-weight: 600;
  border-radius: 10px;
  padding: 0.7rem 1.1rem;
  justify-content: center;
}
:deep(.p-button:enabled:hover) {
  background: var(--coral);
  border-color: var(--coral);
  filter: brightness(1.07);
}
:deep(.p-button:focus-visible) {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}

/* Links (Passwort vergessen / Registrieren) im Coral-Akzent statt Blau */
:deep(a) {
  color: var(--peach);
  text-decoration: none;
}
:deep(a:hover) {
  color: var(--coral);
  text-decoration: underline;
}
</style>
