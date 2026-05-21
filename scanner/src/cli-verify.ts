import { verify } from "./core/verify";
import type { ViolationDto } from "./core/types";

interface VerifyInput {
  originalHtml: string;
  oldSnippet: string;
  newSnippet: string;
  originalViolations: ViolationDto[];
  rules: string[];
  targetViolation: ViolationDto;
}

async function main() {
  const chunks: Buffer[] = [];
  for await (const chunk of process.stdin) {
    chunks.push(chunk as Buffer);
  }
  const input: VerifyInput = JSON.parse(
    Buffer.concat(chunks).toString("utf-8"),
  );

  const result = await verify(
    input.originalHtml,
    input.oldSnippet,
    input.newSnippet,
    input.originalViolations,
    input.rules,
    input.targetViolation,
  );
  process.stdout.write(JSON.stringify(result));
}

main().catch((err) => {
  process.stderr.write(String(err));
  process.exit(1);
});
