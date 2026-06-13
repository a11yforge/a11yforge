<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <main
    class="notfound flex min-h-screen flex-col items-center justify-center gap-6 px-6 text-center"
  >
    <img
      src="/mole.png"
      alt=""
      aria-hidden="true"
      class="h-32 w-32 object-contain"
      style="image-rendering: pixelated"
    />

    <p class="text-6xl font-extrabold tracking-tight text-emerald-500">404</p>

    <div class="space-y-2">
      <h1 class="text-2xl font-bold text-gray-100">Diese Seite hat sich verlaufen</h1>
      <p class="max-w-md text-gray-400">
        Unser Maulwurf hat überall gegraben — aber die Seite, die du suchst, gibt es
        nicht (mehr). Vielleicht ein Tippfehler in der Adresse?
      </p>
    </div>

    <RouterLink
      to="/"
      class="rounded-lg bg-emerald-600 px-5 py-2.5 font-medium text-white transition hover:bg-emerald-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-emerald-400"
    >
      Zurück zur Startseite
    </RouterLink>
  </main>
</template>

<style scoped>
.notfound {
  background:
    radial-gradient(900px 500px at 70% 18%, rgba(255, 122, 82, 0.12), transparent 60%),
    linear-gradient(160deg, #16121d 0%, #16121d 55%, #142323 100%);
}
</style>
