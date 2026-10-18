# Test Plan

## P0 offline acceptance
- Create tests with A-D and A-E options.
- Select, change, and clear answers; mark and unmark review.
- Refresh/reopen and confirm settings and answers persist.
- Resume multiple tests without mixing their answers.
- Create, fill, save, score, and export with airplane mode enabled.
- Core OMR/manual scoring/export flows make no network requests.

## Scoring unit tests
- All correct, all wrong, and mixed responses.
- Negative marking enabled and disabled.
- Decimal deductions such as 0.25 and 0.33.
- Unattempted answers score zero by default.
- Missing or ambiguous key entries remain unresolved.
- Maximum score and attempted-answer accuracy use documented denominators.
- Invalid rules and unsupported options are rejected.
- Display rounding does not change the internal calculation.

## Persistence and recovery
- Autosave during fast option changes.
- Simulated IndexedDB failure and quota exceeded.
- Export/import valid backup and reject malformed backup.
- Delete one test and delete all data with confirmation.
- Confirm test IDs isolate answers and answer keys.

## Submission and result
- Review counts match saved responses.
- Submission creates a stable response snapshot.
- Question-wise result matches scoring engine output.
- Result is provisional when answer-key entries are unresolved.
- Copy answer list and prompt produce stable numbered text.
- Clipboard failure offers a manual selection fallback.

## AI tests (later phase)
- Explicit consent is required before external upload.
- Valid structured output passes schema validation.
- Missing, duplicate, invalid-option, and out-of-range entries are flagged.
- Low-confidence extraction requires review.
- Timeout, rate limit, provider error, and malformed output preserve local data.
- Score is calculated locally, not accepted from AI.
- Retries do not create duplicate billing charges.

## Ads and privacy tests (later phase)
- No ad request while offline.
- No ad in active OMR, submit confirmation, or scoring flow.
- Ad failure does not block navigation or saved tests.
- Ad requests contain no answers, answer keys, test IDs, or scores.
- Privacy disclosures match provider configuration.
- No first-party analytics/telemetry by default.

## Accessibility and compatibility
- Keyboard-only navigation and visible focus.
- Screen reader announces option and palette states.
- Touch targets, contrast, text zoom, and narrow viewport are checked.
- Service-worker update preserves local database.
- Test selected Android and desktop browsers before release.

## Release gate
Do not release until P0 tests pass, backup/restore is verified, scoring fixtures pass, and privacy claims match observed network behavior.
