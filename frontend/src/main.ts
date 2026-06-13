import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import PrimeVue from 'primevue/config'
import Aura from '@primeuix/themes/aura'
import { definePreset } from '@primeuix/themes'
import ConfirmationService from 'primevue/confirmationservice'
import ToastService from 'primevue/toastservice'
import 'primeicons/primeicons.css'
import './assets/main.css'

// Sunset-Palette als PrimeVue-Preset -> Dialoge/Confirm/Select/Inputs passen zum Design
const Sunset = definePreset(Aura, {
  semantic: {
    primary: {
      50: '#fff1ec',
      100: '#ffe0d4',
      200: '#ffc3ad',
      300: '#ffa183',
      400: '#ff8a63',
      500: '#ff7a52',
      600: '#ec6a45',
      700: '#c2552f',
      800: '#8f3e22',
      900: '#5e2a17',
      950: '#3a1a0e',
    },
    colorScheme: {
      dark: {
        primary: {
          color: '#ff7a52',
          contrastColor: '#2a1410',
          hoverColor: '#ff8a63',
          activeColor: '#ec6a45',
        },
        surface: {
          0: '#ffffff',
          50: '#f3e9e2',
          100: '#cabfd0',
          200: '#a99cb0',
          300: '#8b7d92',
          400: '#6f6377',
          500: '#574d61',
          600: '#3f364a',
          700: '#322840',
          800: '#221a2c',
          900: '#1e1a29',
          950: '#16121d',
        },
      },
    },
  },
})

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(PrimeVue, {
  theme: {
    preset: Sunset,
    options: {
      darkModeSelector: '.dark',
    },
  },
})
app.use(ConfirmationService)
app.use(ToastService)
app.mount('#app')
