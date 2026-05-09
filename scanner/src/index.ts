import { scan } from "./scan";

async function main() {
  const urlArr = [
    "file:///C:/Users/1_Phil/2_Wissen/5_Coden/Projekte/private-projects/coriolisGalerie/index.html"
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

