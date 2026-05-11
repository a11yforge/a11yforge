import { AxeBuilder } from "@axe-core/playwright";
import { chromium } from "playwright";

const TIMEOUT = 50_000;

export async function scan(url: string) {
  const browser = await chromium.launch();
  try {
    const context = await browser.newContext();
    const page = await context.newPage();
    await page.goto(url, { timeout: TIMEOUT });
    const results = await new AxeBuilder({ page })
      //.withRules(["image-alt"])
      .withRules(["color-contrast"])
      .analyze();
    const renderedHtml = await page.content();
    return { results, renderedHtml };
  } finally {
    await browser.close();
  }
}
