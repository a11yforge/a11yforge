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
  } finally {
    await browser.close();
  }
}
