import { AxeBuilder } from "@axe-core/playwright";
import { chromium } from "playwright";
import type { ViolationDto } from "./types";
import { axeResultsToViolationDtos } from "../mapping/axe";

type VerifyResult =
  | { status: "verified" }
  | {
      status: "discarded";
      reason: "original_persists" | "new_violation" | "malformed_patch";
    };

function isSameViolation(a: ViolationDto, b: ViolationDto): boolean {
  return (
    a.ruleId === b.ruleId &&
    a.htmlSnippet === b.htmlSnippet &&
    JSON.stringify(a.target) === JSON.stringify(b.target)
  );
}

export async function verify(
  originalHtml: string,
  oldSnippet: string,
  newSnippet: string,
  originalViolations: ViolationDto[],
  rules: string[],
  targetViolation: ViolationDto,
): Promise<VerifyResult> {
  const patchedHtml = originalHtml.replace(oldSnippet, newSnippet);
  if (patchedHtml === originalHtml) {
    return { status: "discarded", reason: "malformed_patch" };
  }

  const browser = await chromium.launch();
  try {
    const context = await browser.newContext();
    const page = await context.newPage();
    await page.setContent(patchedHtml);
    const results = await new AxeBuilder({ page }).withRules(rules).analyze();
    const newViolations = axeResultsToViolationDtos(results, "axe_violation");

    const originalStillThere = newViolations.some((v) =>
      isSameViolation(v, targetViolation),
    );
    if (originalStillThere) {
      return { status: "discarded", reason: "original_persists" };
    }
    const hasNewViolation = newViolations.some(
      (nv) => !originalViolations.some((ov) => isSameViolation(nv, ov)),
    );
    if (hasNewViolation) {
      return { status: "discarded", reason: "new_violation" };
    }

    return { status: "verified" };
  } finally {
    await browser.close();
  }
}
