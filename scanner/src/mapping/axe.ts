// npm install axe-core --save gibt fehler mit @axe-core/playwright

import type { AxeResults, Result, NodeResult } from "axe-core";
import type { ViolationDto, ViolationSource, Impact } from "../core/types";
import { randomUUID } from "node:crypto";

interface AxeContrastData {
  fgColor?: string;
  bgColor?: string;
  contrastRatio?: number;
  expectedContrastRatio?: string;
}

function extractContrastData(node: NodeResult): AxeContrastData {
  const checks = [...(node.any ?? []), ...(node.all ?? []), ...(node.none ?? [])];
  for (const check of checks) {
    const data = check.data as Record<string, unknown> | undefined;
    if (data && ("contrastRatio" in data || "fgColor" in data)) {
      return {
        fgColor: typeof data.fgColor === "string" ? data.fgColor : undefined,
        bgColor: typeof data.bgColor === "string" ? data.bgColor : undefined,
        contrastRatio:
          typeof data.contrastRatio === "number" ? data.contrastRatio : undefined,
        expectedContrastRatio:
          typeof data.expectedContrastRatio === "string"
            ? data.expectedContrastRatio
            : undefined,
      };
    }
  }
  return {};
}

export function axeResultsToViolationDtos(
  result: AxeResults,
  source: ViolationSource,
): ViolationDto[] {
  const ruleArray =
    source === "axe_violation"
      ? result.violations
      : source === "axe_incomplete"
        ? result.incomplete
        : result.passes;

  return ruleArray.flatMap((rule) =>
    rule.nodes.map((node) => ({
      violationId: randomUUID(),
      source: source,
      ruleId: rule.id,
      impact: rule.impact as Impact,
      wcagTags: rule.tags,
      helpUrl: rule.helpUrl,
      description: rule.description,
      failureSummary: node.failureSummary ?? "",
      target: node.target.flat().map(String),
      htmlSnippet: node.html,
      ...(rule.id === "color-contrast" ? extractContrastData(node) : {}),
    })),
  );
}
