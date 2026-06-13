<script setup lang="ts"></script>

<template>
  <div class="auth">
    <slot />
  </div>
</template>

<style scoped>
.auth {
  --bg: #16121d;
  --bg-2: #142323;
  --surface: #1e1a29;
  --field: #14111c;
  --text: #f3e9e2;
  --muted: #a99cb0;
  --coral: #ff7a52;
  --peach: #f6c89a;
  --teal: #5bbeb2;
  --border: #322840;

  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1.5rem;
  color: var(--text);
  background:
    radial-gradient(800px 480px at 50% 0%, rgba(255, 122, 82, 0.12), transparent 60%),
    linear-gradient(160deg, var(--bg) 0%, var(--bg) 55%, var(--bg-2) 100%);
  font-family: ui-sans-serif, system-ui, sans-serif;
}
</style>
