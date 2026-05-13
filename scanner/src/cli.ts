import { scan } from "./core/scan";

const url = process.argv[2];
const rulesArg = process.argv[3];

if (!url || !rulesArg) {
  console.error("Usage: cli.ts <url> <rule1,rule2,...>");
  process.exit(1);
}

const rules = rulesArg.split(",");

async function main() {
  const result = await scan(url, rules);
  process.stdout.write(JSON.stringify(result));
}

main().catch((err) => {
  console.error("Scan faild:", err);
  process.exit(1);
});
