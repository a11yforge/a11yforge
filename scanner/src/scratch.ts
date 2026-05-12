import { scan } from "./core/scan";

async function main() {
  const result = await scan("https://www.krone.at", ["color-contrast"]);
  console.log(JSON.stringify(result, null, 2));
}
main();
