import { scan } from "./scan";
import { pathToFileURL } from "node:url";
import path from "node:path";

async function main() {
  const url = pathToFileURL(
    path.resolve(__dirname, "tests/missing-alt.html"),
  ).href;

  const { results } = await scan(url);

  const violation = results.violations[0];
  if (!violation) {
    console.log("No img-alt violations");
    return;
  }

  const node = violation.nodes[0];
  const originalSnippet = node.html;
  console.log("Missing img-alt", originalSnippet);
}

if (require.main === module) {
  main();
}
