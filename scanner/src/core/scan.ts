import { AxeBuilder } from "@axe-core/playwright";
import { chromium } from "playwright";
import type { PageScanResult } from "./types";
import { axeResultsToViolationDtos } from "../mapping/axe";

const TIMEOUT = 50_000;

export async function scan(
  url: string,
  rules: string[],
): Promise<PageScanResult> {
  const start = Date.now();
  const browser = await chromium.launch();
  try {
    const context = await browser.newContext();
    const page = await context.newPage();
    const response = await page.goto(url, { timeout: TIMEOUT });
    const results = await new AxeBuilder({ page }).withRules(rules).analyze();
    const renderedHtml = await page.content();
    const pageTitle = await page.title();
    const finalUrl = page.url();
    const httpStatus = response?.status();

    const violations = axeResultsToViolationDtos(results, "axe_violation");
    const incomplete = axeResultsToViolationDtos(results, "axe_incomplete");

    for(const v of violations) {
      if(v.ruleId === "image-alt") {
        const selector = v.target[0];
        if (selector) {
          try {
            const buffer = await page.locator(selector).first().screenshot();
            v.screenshot = buffer.toString("base64");
          } catch (e) {
            console.error(`Screenshot ${selector}:`, (e as Error).message);
          }
        }
      }
    }

    return {
      scannerVersion: "a11yforge-scanner@0.1.0",
      url,
      finalUrl,
      scannedAt: new Date().toISOString(),
      durationMs: Date.now() - start,
      pageStatus: "scanned",
      ...(httpStatus !== undefined && { httpStatus }),
      pageTitle,
      ruleSet: rules,
      renderedHtml,
      violations: violations,
      incomplete: incomplete,
    };
  } finally {
    await browser.close();
  }
}
