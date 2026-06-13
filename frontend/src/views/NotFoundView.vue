<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <main class="notfound">
    <img src="/mole.png" alt="" aria-hidden="true" class="notfound__mole" />

    <p class="notfound__code">404</p>

    <div class="notfound__text">
      <h1>Diese Seite hat sich verlaufen</h1>
      <p>
        Unser Maulwurf hat überall gegraben — aber die Seite, die du suchst, gibt es nicht
        (mehr). Vielleicht ein Tippfehler in der Adresse?
      </p>
    </div>

    <RouterLink to="/" class="notfound__btn">Zurück zur Startseite</RouterLink>
  </main>
</template>

<style scoped>
.notfound {
  --text: #f3e9e2;
  --muted: #a99cb0;
  --coral: #ff7a52;
  --teal: #5bbeb2;

  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1.5rem;
  padding: 0 1.5rem;
  text-align: center;
  color: var(--text);
  font-family: ui-sans-serif, system-ui, sans-serif;
  background:
    radial-gradient(900px 500px at 70% 18%, rgba(255, 122, 82, 0.12), transparent 60%),
    linear-gradient(160deg, #16121d 0%, #16121d 55%, #142323 100%);
}
.notfound__mole {
  width: 128px;
  height: 128px;
  object-fit: contain;
  image-rendering: pixelated;
}
.notfound__code {
  font-size: 4rem;
  font-weight: 800;
  letter-spacing: -1px;
  color: var(--coral);
  margin: 0;
}
.notfound__text h1 {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text);
  margin: 0 0 0.5rem;
}
.notfound__text p {
  max-width: 30rem;
  color: var(--muted);
  margin: 0;
}
.notfound__btn {
  background: var(--coral);
  color: #2a1410;
  border-radius: 10px;
  padding: 0.7rem 1.3rem;
  font-weight: 600;
  text-decoration: none;
}
.notfound__btn:hover {
  filter: brightness(1.07);
}
.notfound__btn:focus-visible {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}
</style>
