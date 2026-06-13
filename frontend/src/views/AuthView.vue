<script setup lang="ts"></script>

<template>
  <div class="w-full max-w-[420px] bg-[#1e1a29] border border-[#322840] rounded-[16px] px-[1.9rem] pt-8 pb-[1.8rem] shadow-[0_24px_60px_rgba(0,0,0,0.45)]">
    <router-link to="/" class="flex items-center justify-center gap-[0.6rem] mb-[1.6rem] no-underline text-[#f3e9e2]" aria-label="Zur Startseite">
      <img class="w-10 h-10 rounded-[11px] object-cover object-[center_18%] border border-[#322840] [image-rendering:pixelated]" src="/mole.png" alt="" aria-hidden="true" />
      <span class="font-extrabold text-xl tracking-[0.2px]">a11y<span class="text-[#ff7a52]">forge</span></span>
    </router-link>

    <router-view />
  </div>
</template>

<style scoped>
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

:deep(a) {
  color: var(--peach);
  text-decoration: none;
}
:deep(a:hover) {
  color: var(--coral);
  text-decoration: underline;
}
</style>
