import { scan } from "./scan";

async function main() {
  const urlArr = [
    "https://orf.at",
    "https://www.w3.org/WAI/demos/bad/before/home.html",
    "https://example.com",
    "https://this-domain-does-not-exist-12345.xyz",
    "https://httpstat.us/500",
  ];

  for (const url of urlArr) {
    try {
      const results = await scan(url);
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
