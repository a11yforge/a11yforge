<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const { isAuthenticated, userName } = storeToRefs(authStore)

async function handleLogout() {
  await authStore.logout()
}
</script>

<template>
  <nav class="nav" aria-label="Hauptnavigation">
    <router-link to="/" class="brand" aria-label="Zur Startseite">
      <img class="brand__logo" src="/mole.png" alt="" aria-hidden="true" />
      <span class="brand__name">a11y<span class="brand__accent">forge</span></span>
    </router-link>

    <div class="links">
      <template v-if="isAuthenticated">
        <router-link to="/projects" class="link" active-class="link--active">Projekte</router-link>
        <router-link to="/account" class="link" active-class="link--active">Account</router-link>
        <router-link v-if="userName" to="/account" class="user" active-class="link--active">
          {{ userName }}
        </router-link>
        <button class="logout" type="button" @click="handleLogout">
          <i class="pi pi-sign-out" aria-hidden="true"></i>
          Logout
        </button>
      </template>

      <template v-else>
        <router-link to="/login" class="link" active-class="link--active">Login</router-link>
        <router-link to="/register" class="link" active-class="link--active">Registrieren</router-link>
      </template>
    </div>
  </nav>
</template>

<style scoped>
.nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.9rem 2rem;
  border-bottom: 1px solid var(--border);
}

.brand {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  text-decoration: none;
  color: var(--text);
}
.brand__logo {
  width: 34px;
  height: 34px;
  border-radius: 9px;
  object-fit: cover;
  object-position: center 18%;
  border: 1px solid var(--border);
  image-rendering: pixelated;
}
.brand__name {
  font-weight: 800;
  font-size: 1.15rem;
  letter-spacing: 0.2px;
}
.brand__accent {
  color: var(--coral);
}

.links {
  display: flex;
  align-items: center;
  gap: 1.3rem;
}
.link {
  color: var(--muted);
  text-decoration: none;
  font-size: 0.95rem;
  transition: color 0.15s ease;
}
.link:hover {
  color: var(--text);
}
.link--active {
  color: var(--coral);
}
.user {
  color: var(--muted);
  font-size: 0.85rem;
}

.logout {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  background: transparent;
  border: 1px solid var(--border);
  color: var(--text);
  border-radius: 9px;
  padding: 0.45rem 0.85rem;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition:
    border-color 0.15s ease,
    color 0.15s ease;
}
.logout:hover {
  border-color: var(--coral);
  color: var(--coral);
}
.logout:focus-visible {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}
</style>
