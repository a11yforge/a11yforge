<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <main class="min-h-screen flex flex-col items-center justify-center gap-6 px-6 text-center text-[#f3e9e2] font-sans bg-[radial-gradient(900px_500px_at_70%_18%,rgba(255,122,82,0.12),transparent_60%),linear-gradient(160deg,#16121d_0%,#16121d_55%,#142323_100%)]">
    <img src="/mole.png" alt="" aria-hidden="true" class="w-32 h-32 object-contain [image-rendering:pixelated]" />

    <p class="text-[4rem] font-extrabold tracking-[-1px] text-[#ff7a52] m-0">404</p>

    <div>
      <h1 class="text-[1.5rem] font-bold text-[#f3e9e2] mt-0 mb-2">Diese Seite hat sich verlaufen</h1>
      <p class="max-w-[30rem] text-[#a99cb0] m-0">
        Unser Maulwurf hat überall gegraben — aber die Seite, die du suchst, gibt es nicht
        (mehr). Vielleicht ein Tippfehler in der Adresse?
      </p>
    </div>

    <RouterLink to="/" class="bg-[#ff7a52] text-[#2a1410] rounded-[10px] py-[0.7rem] px-[1.3rem] font-semibold no-underline hover:brightness-[1.07] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]">Zurück zur Startseite</RouterLink>
  </main>
</template>
