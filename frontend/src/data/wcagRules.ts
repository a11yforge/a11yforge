export interface WcagRules {
  id: string
  title: string
  wcag: string
  description: string
}

export const wcagRules: WcagRules[] = [
  { id: 'image-alt', title: 'Alternativtexte für Bilder', wcag: '1.1.1',
    description: 'Jedes informative Bild braucht einen aussagekräftigen alt-Text.' },
  { id: 'color-contrast', title: 'Ausreichender Farbkontrast', wcag: '1.4.3',
    description: 'Text muss sich klar vom Hintergrund abheben (mind. 4.5:1).' },
  { id: 'label', title: 'Formularfelder beschriften', wcag: '3.3.2',
    description: 'Jedes Eingabefeld braucht ein zugeordnetes Label.' },
  { id: 'html-has-lang', title: 'Sprache der Seite angeben', wcag: '3.1.1',
    description: 'Das html-Element braucht ein lang-Attribut.' },
  { id: 'heading-order', title: 'Logische Überschriften-Reihenfolge', wcag: '1.3.1',
    description: 'Überschriften-Ebenen (h1–h6) dürfen keine Stufen überspringen.' },
  ]
