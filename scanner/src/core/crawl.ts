import { AxeBuilder } from "@axe-core/playwright";
import { chromium } from "playwright";
import type { PageScanResult } from "./types";
import { axeResultsToViolationDtos } from "../mapping/axe";

const TIMEOUT = 50_000;

export async function crawl(
  baseUrl: string,
  rules: string[],
  maxPages: number,
): Promise<PageScanResult[]> {
  const browser = await chromium.launch();

  try {
    const results: PageScanResult[] = [];
    const queue: string[] = [baseUrl];
    const visited = new Set<string>();
    const context = await browser.newContext();

    while (queue.length > 0 && results.length < maxPages) {
      const url = queue.shift()!;
      if (visited.has(url)) continue;
      visited.add(url);

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
        violations: axeResultsToViolationDtos(axeResults, "axe_violation"),
      });

      const links = await page.$$eval("a[href]", (anchors) =>
        anchors.map((a) => (a as HTMLAnchorElement).href),
      );
      const origin = new URL(baseUrl).origin;

      for (const link of links) {
        const linkUrl = new URL(link);
        linkUrl.hash = "";
        const clean = linkUrl.href;
        if (linkUrl.origin === origin && !visited.has(clean)) {
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
