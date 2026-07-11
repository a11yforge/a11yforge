<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { probeScan, type ProbeScanResponse } from '@/api/probescan'

const router = useRouter()

const rules = [
  { rule: 'image-alt', wcag: '1.1.1', impact: 'critical', status: 'auto-fixable' },
  { rule: 'color-contrast', wcag: '1.4.3', impact: 'serious', status: 'auto-fixable' },
  { rule: 'label', wcag: '4.1.2', impact: 'critical', status: 'auto-fixable' },
  { rule: 'html-has-lang', wcag: '3.1.1', impact: 'serious', status: 'auto-fixable' },
  { rule: 'heading-order', wcag: '1.3.1', impact: 'moderate', status: 'auto-fixable' },
]

const index = ref(0)
const current = computed(() => rules[index.value] ?? rules[0]!)
let timer: number | undefined
const loading = ref(false)
const result = ref<ProbeScanResponse | null>(null)

onMounted(() => {
  timer = window.setInterval(() => {
    index.value = (index.value + 1) % rules.length
  }, 2600)
})
onBeforeUnmount(() => window.clearInterval(timer))

const url = ref('')

function goLogin() {
  router.push('/login')
}

async function startProbescan() {
  loading.value = true
  try {
    result.value = await probeScan(url.value)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="relative isolate min-h-screen flex flex-col text-[var(--text)] font-sans bg-[#16121d]">
    <!-- Gradient als dahinterliegende Ebene (Sibling, nicht Vorfahre des Texts),
         damit axe-core den soliden Wurzel-Hintergrund lesen kann statt "cantTell". -->
    <div aria-hidden="true" class="absolute inset-0 -z-10 pointer-events-none bg-[radial-gradient(900px_500px_at_70%_18%,rgba(255,122,82,0.12),transparent_60%),linear-gradient(160deg,#16121d_0%,#16121d_55%,#142323_100%)]"></div>
    <header class="flex items-center justify-between py-4 px-8">
      <div class="flex items-center gap-[0.65rem]">
        <img class="w-10 h-10 rounded-[11px] object-cover object-[center_18%] bg-[var(--surface)] border border-[var(--border)] shadow-[0_2px_8px_rgba(0,0,0,0.35)] [image-rendering:pixelated]" src="/mole.png" alt="" aria-hidden="true" />
        <span class="font-extrabold tracking-[0.2px] text-[1.2rem]">a11y<span class="text-[var(--coral)]">forge</span></span>
      </div>
      <button class="border border-[var(--border)] rounded-[10px] py-[0.7rem] px-[1.1rem] font-semibold cursor-pointer bg-transparent text-[var(--text)] hover:border-[var(--coral)] hover:text-[var(--coral)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" @click="goLogin">Login</button>
    </header>

    <main class="flex-1 grid grid-cols-2 max-[860px]:grid-cols-1 gap-12 items-center max-w-[1100px] mx-auto px-8 pt-8 pb-16">
      <section>
        <h1 class="text-[2.6rem] leading-[1.1] font-extrabold mt-0 mb-4">
          Barrierefreiheit, die sich <span class="text-[var(--coral)]">selbst repariert</span>.
        </h1>
        <p class="text-[var(--muted)] text-[1.1rem] leading-[1.6] max-w-[38ch]">
          a11yforge scannt deine Website auf WCAG-Verstöße, generiert echte
          Code-Fixes — und <strong class="text-[var(--peach)] font-semibold">verifiziert jeden Fix deterministisch</strong>,
          bevor du ihn zu sehen bekommst.
        </p>

        <form class="flex gap-2 mt-[1.6rem] mb-2 max-w-[440px]" @submit.prevent="startProbescan">
          <label class="sr-only" for="url">Website-URL für Probescan</label>
          <input
            id="url"
            v-model="url"
            type="text"
            placeholder="https://deine-website.at"
            class="flex-1 bg-[var(--surface)] border border-[var(--border)] rounded-[10px] py-[0.7rem] px-[0.9rem] text-[var(--text)] placeholder:text-[var(--muted)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
          />
          <button type="submit" class="border-0 rounded-[10px] py-[0.7rem] px-[1.1rem] font-semibold cursor-pointer bg-[var(--coral)] text-[var(--on-coral)] hover:brightness-[1.07] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" :disabled="loading">{{ loading ? 'Scan läuft…' : 'Probescan ▶' }}</button>
        </form>
        <p class="text-[var(--muted)] text-[0.82rem]">Kostenlos testen — kein Konto nötig zum Anschauen.</p>
      </section>

      <section class="relative">
        <img class="absolute bottom-[calc(100%-12px)] right-8 w-[168px] [image-rendering:pixelated] drop-shadow-[0_8px_6px_rgba(0,0,0,0.45)] z-[2] pointer-events-none" src="/mole.png" alt="a11yforge Maskottchen: Maulwurf-Bergmann" />
        <div
          class="bg-[var(--field)] border border-[var(--border)] rounded-[14px] overflow-hidden shadow-[0_24px_60px_rgba(0,0,0,0.45)]"
          role="img"
          :aria-label="`WCAG-Regel ${current.rule}, ${current.status}`"
        >
          <div class="flex items-center gap-[0.45rem] py-[0.6rem] px-[0.9rem] bg-[rgba(255,255,255,0.03)] border-b border-[var(--border)]">
            <span class="w-[11px] h-[11px] rounded-full inline-block bg-[#ff5f56]"></span>
            <span class="w-[11px] h-[11px] rounded-full inline-block bg-[#ffbd2e]"></span>
            <span class="w-[11px] h-[11px] rounded-full inline-block bg-[#27c93f]"></span>
            <span class="ml-[0.6rem] text-[var(--muted)] text-[0.8rem] font-mono">wcag-rules.json</span>
          </div>
          <Transition name="fade" mode="out-in">
            <pre :key="current.rule" class="m-0 py-[1.3rem] px-[1.4rem] font-mono text-[0.95rem] leading-[1.7] text-[var(--text)] whitespace-pre"><code>{
  <span class="text-[var(--teal)]">"rule"</span>:   <span class="text-[var(--peach)]">"{{ current.rule }}"</span>,
  <span class="text-[var(--teal)]">"wcag"</span>:   <span class="text-[var(--peach)]">"{{ current.wcag }}"</span>,
  <span class="text-[var(--teal)]">"impact"</span>: <span class="text-[var(--peach)]">"{{ current.impact }}"</span>,
  <span class="text-[var(--teal)]">"status"</span>: <span class="text-[var(--coral)]">"{{ current.status }}"</span>
}</code></pre>
          </Transition>
          <div class="pt-[0.6rem] px-[1.4rem] pb-4 text-[var(--muted)] text-[0.8rem] font-mono">5 Regeln · ~80 % der Realfälle</div>
        </div>
      </section>
    </main>

    <footer class="flex justify-between py-[1.2rem] px-8 border-t border-[var(--border)] text-[var(--muted)] text-[0.85rem]">
      <span>© a11yforge</span>
      <nav>
        <a href="/impressum" class="text-[var(--muted)] ml-[1.2rem] no-underline hover:text-[var(--coral)]">Impressum</a>
        <a href="/datenschutz" class="text-[var(--muted)] ml-[1.2rem] no-underline hover:text-[var(--coral)]">Datenschutz</a>
      </nav>
    </footer>
    <div
      v-if="result"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 px-4"
      @click.self="result = null"
    >
      <div class="w-full max-w-[520px] max-h-[80vh] overflow-y-auto bg-[var(--field)] border border-[var(--border)] rounded-[14px] shadow-[0_24px_60px_rgba(0,0,0,0.5)]">
        <div class="flex items-center justify-between py-4 px-6 border-b border-[var(--border)]">
          <h2 class="m-0 text-[1.2rem] font-extrabold">{{ result.totalViolations }} Probleme gefunden</h2>
          <button class="bg-transparent border-0 text-[var(--muted)] text-[1.4rem] cursor-pointer hover:text-[var(--coral)]" aria-label="Schließen" @click="result = null">×</button>
        </div>

        <ul class="list-none m-0 p-0">
          <li v-for="(v, i) in result.violations" :key="i" class="py-3 px-6 border-b border-[#241d30]">
            <div class="flex items-center gap-2 mb-1">
              <span class="font-mono text-[0.85rem] text-[var(--peach)]">{{ v.ruleId }}</span>
              <span class="text-[0.72rem] uppercase tracking-wide text-[var(--coral)]">{{ v.impact }}</span>
            </div>
            <p class="m-0 text-[var(--muted)] text-[0.9rem]">{{ v.description }}</p>
          </li>
        </ul>

        <div v-if="result.totalViolations > result.violations.length" class="py-3 px-6 text-[var(--muted)] text-[0.85rem]">
          + {{ result.totalViolations - result.violations.length }} weitere — sichtbar nach Registrierung
        </div>

        <div class="p-6 border-t border-[var(--border)]">
          <button class="w-full border-0 rounded-[10px] py-[0.8rem] font-semibold cursor-pointer bg-[var(--coral)] text-[var(--on-coral)] hover:brightness-[1.07]" @click="router.push('/register')">
            Jetzt registrieren für vollständigen Scan + Fixes
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.35s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
