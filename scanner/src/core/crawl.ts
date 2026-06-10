import { AxeBuilder } from "@axe-core/playwright";
import { chromium } from "playwright";
import type { PageScanResult } from "./types";
import { axeResultsToViolationDtos } from "../mapping/axe";

const TIMEOUT = 50_000;

function normalize(u: string): string {
  const url = new URL(u);
  url.hash = "";
  if (url.pathname.endsWith("/") && url.pathname !== "/") {
    url.pathname = url.pathname.slice(0, -1);
  }

  return url.href;
}

export async function crawl(
  baseUrl: string,
  rules: string[],
  maxPages: number,
): Promise<PageScanResult[]> {
  const browser = await chromium.launch();

  try {
    const results: PageScanResult[] = [];
    const queue: string[] = [normalize(baseUrl)];
    const visited = new Set<string>([normalize(baseUrl)]);
    const context = await browser.newContext();

    while (queue.length > 0 && results.length < maxPages) {
      const url = queue.shift()!;

      const page = await context.newPage();
      const start = Date.now();
      const response = await page.goto(url, { timeout: TIMEOUT });
      const axeResults = await new AxeBuilder({ page })
        .withRules(rules)
        .analyze();
      const renderedHtml = await page.content();
      const pageTitle = await page.title();
      const finalUrl = page.url();
      const httpStatus = response?.status();

      const violations = axeResultsToViolationDtos(axeResults, "axe_violation");

      for (const v of violations) {
        if (v.ruleId === "image-alt") {
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

      results.push({
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
        incomplete: axeResultsToViolationDtos(axeResults, "axe_incomplete"),
      });

      const links = await page.$$eval("a[href]", (anchors) =>
        anchors.map((a) => (a as HTMLAnchorElement).href),
      );
      const origin = new URL(baseUrl).origin;

      for (const link of links) {
        const clean = normalize(link);
        if (new URL(clean).origin === origin && !visited.has(clean)) {
          visited.add(clean);
          queue.push(clean);
        }
      }
      await page.close();
    }

    return results;
  } finally {
    await browser.close();
  }
}
