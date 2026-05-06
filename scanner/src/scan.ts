import { AxeBuilder } from "@axe-core/playwright";
import { chromium } from "playwright";

const TIMEOUT = 20_000;

export async function scan(url: string) {
  const browser = await chromium.launch();
  try {
    const context = await browser.newContext();
    const page = await context.newPage();
    await page.goto(url, { timeout: TIMEOUT });
    const results = await new AxeBuilder({ page }).analyze();
    return results;
  } finally {
    await browser.close();
  }
}
