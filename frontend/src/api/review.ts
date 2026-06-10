import client from "./client"

export async function createReview(
  fixProposalId: number,
  decision: "ACCEPTED" | "REJECTED",
  comment = "",
) {
  const response = await client.post("/reviews", { fixProposalId, decision, comment })
  return response.data
}
