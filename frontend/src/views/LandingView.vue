<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const rules = [
  { rule: 'image-alt', wcag: '1.1.1', impact: 'critical', status: 'auto-fixable' },
  { rule: 'color-contrast', wcag: '1.4.3', impact: 'serious', status: 'auto-fixable' },
  { rule: 'label', wcag: '4.1.2', impact: 'critical', status: 'auto-fixable' },
  { rule: 'html-has-lang', wcag: '3.1.1', impact: 'serious', status: 'auto-fixable' },
  { rule: 'heading-order', wcag: '1.3.1', impact: 'moderate', status: 'auto-fixable' },
]

const index = ref(0)
const current = computed(() => rules[index.value])
let timer: number | undefined

onMounted(() => {
  timer = window.setInterval(() => {
    index.value = (index.value + 1) % rules.length
  }, 2600)
})
onBeforeUnmount(() => window.clearInterval(timer))

const url = ref('')
function startProbescan() {
  router.push('/register')
}
function goLogin() {
  router.push('/login')
}
</script>

<template>
  <div class="landing">
    <header class="nav">
      <div class="brand">
        <img class="brand__logo" src="/mole.png" alt="" aria-hidden="true" />
        <span class="brand__name">a11y<span class="brand__accent">forge</span></span>
      </div>
      <button class="btn btn--ghost" @click="goLogin">Login</button>
    </header>

    <main class="hero">
      <section class="hero__text">
        <h1>
          Barrierefreiheit, die sich <span class="accent">selbst repariert</span>.
        </h1>
        <p class="lead">
          a11yforge scannt deine Website auf WCAG-Verstöße, generiert echte
          Code-Fixes — und <strong>verifiziert jeden Fix deterministisch</strong>,
          bevor du ihn zu sehen bekommst.
        </p>

        <form class="probescan" @submit.prevent="startProbescan">
          <label class="sr-only" for="url">Website-URL für Probescan</label>
          <input
            id="url"
            v-model="url"
            type="url"
            placeholder="https://deine-website.at"
            class="probescan__input"
          />
          <button type="submit" class="btn btn--primary">Probescan ▶</button>
        </form>
        <p class="hint">Kostenlos testen — kein Konto nötig zum Anschauen.</p>
      </section>

      <section class="terminal-wrap">
        <img class="mascot" src="/mole.png" alt="a11yforge Maskottchen: Maulwurf-Bergmann" />
        <div
          class="terminal"
          role="img"
          :aria-label="`WCAG-Regel ${current.rule}, ${current.status}`"
        >
          <div class="terminal__bar">
            <span class="dot dot--red"></span>
            <span class="dot dot--yellow"></span>
            <span class="dot dot--green"></span>
            <span class="terminal__title">wcag-rules.json</span>
          </div>
          <Transition name="fade" mode="out-in">
            <pre :key="current.rule" class="terminal__body"><code>{
  <span class="k">"rule"</span>:   <span class="s">"{{ current.rule }}"</span>,
  <span class="k">"wcag"</span>:   <span class="s">"{{ current.wcag }}"</span>,
  <span class="k">"impact"</span>: <span class="s">"{{ current.impact }}"</span>,
  <span class="k">"status"</span>: <span class="v">"{{ current.status }}"</span>
}</code></pre>
          </Transition>
          <div class="terminal__caption">5 Regeln · ~80 % der Realfälle</div>
        </div>
      </section>
    </main>

    <footer class="foot">
      <span>© a11yforge</span>
      <nav>
        <a href="/impressum">Impressum</a>
        <a href="/datenschutz">Datenschutz</a>
      </nav>
    </footer>
  </div>
</template>

<style scoped>
.landing {
  --bg: #16121d;
  --bg-2: #142323;
  --surface: #1e1a29;
  --term-bg: #14111c;
  --text: #f3e9e2;
  --muted: #a99cb0;
  --coral: #ff7a52;
  --peach: #f6c89a;
  --teal: #5bbeb2;
  --mauve: #8b6b85;
  --border: #322840;

  min-height: 100vh;
  display: flex;
  flex-direction: column;
  color: var(--text);
  background:
    radial-gradient(900px 500px at 70% 18%, rgba(255, 122, 82, 0.12), transparent 60%),
    linear-gradient(160deg, var(--bg) 0%, var(--bg) 55%, var(--bg-2) 100%);
  font-family: ui-sans-serif, system-ui, sans-serif;
}

.nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
}
.brand { display: flex; align-items: center; gap: 0.65rem; }
.brand__logo {
  width: 40px;
  height: 40px;
  border-radius: 11px;
  object-fit: cover;
  object-position: center 18%;
  background: var(--surface);
  border: 1px solid var(--border);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.35);
  image-rendering: pixelated;
}
.brand__name { font-weight: 800; letter-spacing: 0.2px; font-size: 1.2rem; }
.brand__accent { color: var(--coral); }

.hero {
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 3rem;
  align-items: center;
  max-width: 1100px;
  margin: 0 auto;
  padding: 2rem 2rem 4rem;
}
@media (max-width: 860px) {
  .hero { grid-template-columns: 1fr; }
}
.hero__text h1 { font-size: 2.6rem; line-height: 1.1; font-weight: 800; margin: 0 0 1rem; }
.accent { color: var(--coral); }
.lead { color: var(--muted); font-size: 1.1rem; line-height: 1.6; max-width: 38ch; }
.lead strong { color: var(--peach); font-weight: 600; }

.probescan { display: flex; gap: 0.5rem; margin: 1.6rem 0 0.5rem; max-width: 440px; }
.probescan__input {
  flex: 1;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 0.7rem 0.9rem;
  color: var(--text);
}
.probescan__input::placeholder { color: var(--muted); }
.probescan__input:focus-visible { outline: 2px solid var(--teal); outline-offset: 2px; }
.hint { color: var(--muted); font-size: 0.82rem; }

.btn { border: 0; border-radius: 10px; padding: 0.7rem 1.1rem; font-weight: 600; cursor: pointer; }
.btn:focus-visible { outline: 2px solid var(--teal); outline-offset: 2px; }
.btn--primary { background: var(--coral); color: #2a1410; }
.btn--primary:hover { filter: brightness(1.07); }
.btn--ghost { background: transparent; color: var(--text); border: 1px solid var(--border); }
.btn--ghost:hover { border-color: var(--coral); color: var(--coral); }

.terminal-wrap { position: relative; }
.mascot {
  position: absolute;
  bottom: calc(100% - 12px);
  right: 32px;
  width: 168px;
  image-rendering: pixelated;
  filter: drop-shadow(0 8px 6px rgba(0, 0, 0, 0.45));
  z-index: 2;
  pointer-events: none;
}
.terminal {
  background: var(--term-bg);
  border: 1px solid var(--border);
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 24px 60px rgba(0, 0, 0, 0.45);
}
.terminal__bar {
  display: flex;
  align-items: center;
  gap: 0.45rem;
  padding: 0.6rem 0.9rem;
  background: rgba(255, 255, 255, 0.03);
  border-bottom: 1px solid var(--border);
}
.dot { width: 11px; height: 11px; border-radius: 50%; display: inline-block; }
.dot--red { background: #ff5f56; }
.dot--yellow { background: #ffbd2e; }
.dot--green { background: #27c93f; }
.terminal__title { margin-left: 0.6rem; color: var(--muted); font-size: 0.8rem; font-family: ui-monospace, monospace; }
.terminal__body {
  margin: 0;
  padding: 1.3rem 1.4rem;
  font-family: ui-monospace, SFMono-Regular, Menlo, monospace;
  font-size: 0.95rem;
  line-height: 1.7;
  color: var(--text);
  white-space: pre;
}
.k { color: var(--teal); }
.s { color: var(--peach); }
.v { color: var(--coral); }
.terminal__caption {
  padding: 0.6rem 1.4rem 1rem;
  color: var(--muted);
  font-size: 0.8rem;
  font-family: ui-monospace, monospace;
}

.fade-enter-active, .fade-leave-active { transition: opacity 0.35s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

.foot {
  display: flex;
  justify-content: space-between;
  padding: 1.2rem 2rem;
  border-top: 1px solid var(--border);
  color: var(--muted);
  font-size: 0.85rem;
}
.foot a { color: var(--muted); margin-left: 1.2rem; text-decoration: none; }
.foot a:hover { color: var(--coral); }

.sr-only {
  position: absolute; width: 1px; height: 1px; padding: 0; margin: -1px;
  overflow: hidden; clip: rect(0, 0, 0, 0); white-space: nowrap; border: 0;
}
</style>
