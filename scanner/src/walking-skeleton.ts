import { scan } from "./scan";
import { verify } from "./verify";

async function main() {
  const url =
    "file:///Users/maxmayer/dev/a11yforge/scanner/src/tests/missing-alt.html";

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
