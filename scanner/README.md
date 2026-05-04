# Scanner

Stateless Service für den regelbasierten WCAG-Scan.
Geplant mit Node, Playwright und axe-core.

Nimmt eine URL entgegen, führt axe-core gegen die geladene Seite aus und gibt
die Violations als JSON zurück. Speichert selbst nichts, das macht das Backend.
