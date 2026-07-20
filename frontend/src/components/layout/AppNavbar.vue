<script setup lang="ts">
import { ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import Menu from 'primevue/menu'
import type { MenuItem } from 'primevue/menuitem'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const { isAuthenticated, userName } = storeToRefs(authStore)

const menu = ref()
const menuItems = ref<MenuItem[]>([
  {
    label: 'Account',
    icon: 'pi pi-user',
    command: () => router.push('/account'),
  },
  {
    label: 'Projekte',
    icon: 'pi pi-folder',
    command: () => router.push('/projects'),
  },
  {
    separator: true,
  },
  {
    label: 'Logout',
    icon: 'pi pi-sign-out',
    command: () => handleLogout(),
  },
])

function toggleMenu(event: Event) {
  menu.value.toggle(event)
}

async function handleLogout() {
  await authStore.logout()
}
</script>

<template>
  <nav
    class="flex items-center justify-between py-[0.9rem] px-8 border-b border-[var(--border)]"
    aria-label="Hauptnavigation"
  >
    <router-link
      to="/projects"
      class="flex items-center gap-[0.6rem] no-underline text-[var(--text)]"
      aria-label="Zur Startseite"
    >
      <img
        class="w-[34px] h-[34px] rounded-[9px] object-cover object-[center_18%] border border-[var(--border)] [image-rendering:pixelated]"
        src="/mole.png"
        alt=""
        aria-hidden="true"
      />
      <span class="font-extrabold text-[1.15rem] tracking-[0.2px]"
        >a11y<span class="text-[var(--coral)]">forge</span></span
      >
    </router-link>

    <div class="flex items-center gap-[1.3rem]">
      <template v-if="isAuthenticated">
        <router-link
          to="/projects"
          class="text-[var(--muted)] no-underline text-[0.95rem] transition-colors duration-[150ms] ease hover:text-[var(--text)]"
          active-class="!text-[var(--coral)]"
          >Projekte</router-link
        >
        <button
          type="button"
          class="inline-flex items-center gap-[0.5rem] bg-transparent border border-[var(--border)] text-[var(--text)] rounded-[9px] py-[0.45rem] px-[0.85rem] text-[0.9rem] font-semibold cursor-pointer transition-[border-color,color] duration-[150ms] ease hover:border-[var(--coral)] hover:text-[var(--coral)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
          aria-haspopup="true"
          aria-controls="user-menu"
          @click="toggleMenu"
        >
          <i class="pi pi-user" aria-hidden="true"></i>
          <span v-if="userName">{{ userName }}</span>
          <i class="pi pi-chevron-down text-[0.75rem]" aria-hidden="true"></i>
        </button>
        <Menu id="user-menu" ref="menu" :model="menuItems" :popup="true" />
      </template>

      <template v-else>
        <router-link
          to="/login"
          class="text-[var(--muted)] no-underline text-[0.95rem] transition-colors duration-[150ms] ease hover:text-[var(--text)]"
          active-class="!text-[var(--coral)]"
          >Login</router-link
        >
        <router-link
          to="/register"
          class="text-[var(--muted)] no-underline text-[0.95rem] transition-colors duration-[150ms] ease hover:text-[var(--text)]"
          active-class="!text-[var(--coral)]"
          >Registrieren</router-link
        >
      </template>
    </div>
  </nav>
</template>

<style scoped>
:deep(.p-menu) {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 0.3rem;
}
:deep(.p-menu .p-menuitem-link) {
  color: var(--text);
  border-radius: 7px;
  padding: 0.55rem 0.8rem;
}
:deep(.p-menu .p-menuitem-link .p-menuitem-icon) {
  color: var(--muted);
}
:deep(.p-menu .p-menuitem-link:hover) {
  background: var(--field);
  color: var(--coral);
}
:deep(.p-menu .p-menuitem-link:hover .p-menuitem-icon) {
  color: var(--coral);
}
:deep(.p-menu .p-menuitem-separator) {
  border-top: 1px solid var(--border);
  margin: 0.3rem 0;
}
</style>
