import { scan } from "./scan";
import { pathToFileURL } from "node:url";
import path from "node:path";

async function main() {
  const urlArr = ["https://www.krone.at", "https://www.heute.at"];

  for (const url of urlArr) {
    try {
      const { results } = await scan(url);
      console.log(`-----------------------${url}-----------------------`);
      console.log(JSON.stringify(results.violations, null, 2));
    } catch (error) {
      console.error(`Scan failed: ${url}`, error);
    }
  }
}
if (require.main === module) {
  main();
}
