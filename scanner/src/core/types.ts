export type PageStatus = "scanned" | "skipped" | "failed";

export type ViolationSource =
  | "axe_violation"
  | "axe_incomplete"
  | "axe_passes_semantic";

export type Impact = "minor" | "moderate" | "serious" | "critical";

export interface ViolationDto {
  violationId: string;
  source: ViolationSource;
  ruleId: string;
  impact: Impact;
  wcagTags: string[];
  helpUrl: string;
  description: string;
  failureSummary: string;
  target: string[];
  htmlSnippet: string;
  domPath?: string;
  screenshot?: string;
  fgColor?: string;
  bgColor?: string;
  contrastRatio?: number;
  expectedContrastRatio?: string;
  detectedLang?: string;
  langSample?: string;
}

export interface PageScanResult {
  scannerVersion: string;
  url: string;
  finalUrl: string;
  scannedAt: string;
  durationMs: number;
  pageStatus: PageStatus;
  failureReason?: string;
  httpStatus?: number;
  pageTitle?: string;
  ruleSet: string[];
  renderedHtml: string;
  violations: ViolationDto[];
  incomplete: ViolationDto[];
}
