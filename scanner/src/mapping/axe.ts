// npm install axe-core --save gibt fehler mit @axe-core/playwright

import type { AxeResults, Result, NodeResult } from "axe-core";
import type { ViolationDto, ViolationSource, Impact } from "../core/types";
import { randomUUID } from "node:crypto";

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
    })),
  );
}
