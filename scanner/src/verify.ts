import { AxeBuilder } from "@axe-core/playwright";
import { chromium } from "playwright";

export async function verify(
  originalHtml: string,
  oldSnippet: string,
  newSnippet: string,
) {
  const patchedHtml = originalHtml.replace(oldSnippet, newSnippet);
  const browser = await chromium.launch();
  try {
    const context = await browser.newContext();
    const page = await context.newPage();
    await page.setContent(patchedHtml);
    const results = await new AxeBuilder({ page })
      .withRules(["image-alt"])
      .analyze();
    return { results, patchedHtml };
  } finally {
    await browser.close();
  }
}
