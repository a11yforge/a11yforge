import { crawl } from "./core/crawl";

const url = process.argv[2];
const rulesArg = process.argv[3];
const maxPagesArg = process.argv[4];

if (!url || !rulesArg || !maxPagesArg) {
  console.error("Usage: cli.ts <url> <rule1,rule2,> <maxPages>");
  process.exit(1);
}

const rules = rulesArg.split(",");
const maxPages = Number(maxPagesArg);

async function main() {
  const result = await crawl(url, rules, maxPages);
  process.stdout.write(JSON.stringify(result));
}

main().catch((err) => {
  console.error("Scan failed:", err);
  process.exit(1);
});
