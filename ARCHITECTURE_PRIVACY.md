# Architecture and Privacy

## Architecture principles
The first release should be an installable PWA. Core OMR features must not require an application backend, account, cloud database, or AI service.

## Proposed stack
- React + TypeScript for UI (confirm framework before implementation).
- Service worker and versioned static assets for offline launch.
- IndexedDB for structured test data and optional local file blobs.
- Pure TypeScript scoring module with unit tests.
- Optional AI provider adapter isolated from local data and scoring.
- Optional ad integration disabled offline and absent from active test screens.

## Local data model
- Test: id, title, questionCount, optionSet, markingRules, duration, createdAt, updatedAt.
- Response: testId, questionNumber, selectedOption|null, markedForReview, updatedAt.
- AnswerKey: testId, questionNumber, correctOption, source, confidence, verificationState.
- Result: testId, correctCount, incorrectCount, blankCount, positiveMarks, deductions, finalScore, evaluatedAt.
- Settings: theme, accessibility, and optional provider metadata.

## Storage
Use IndexedDB instead of localStorage for structured records and image blobs. Autosave answer changes, handle write/quota failures, and recover incomplete tests. Provide versioned JSON export/import. Explain that browser data clearing can remove local tests. Never silently sync or upload local data.

## Scoring contract
- Correct: add configured positive marks.
- Incorrect: subtract configured negative marks when enabled.
- Unattempted: zero by default.
- Missing or ambiguous answer key: unresolved; do not guess.
Scoring must run locally and deterministically. AI output is never authoritative for arithmetic.

## Optional AI flow
1. User chooses an AI mode and sees a data-transfer notice.
2. User explicitly confirms the selected file/data may be sent to the chosen provider.
3. Provider adapter submits only the minimum required data.
4. Validate output against a strict schema and allowed options.
5. Mark extraction unverified until uncertain entries are reviewed.
6. Run local scoring after answer-key verification.

## API key handling
Never embed the default provider secret in client code. A paid default API requires a server-side proxy, cost limits, and clear disclosure that selected data leaves the device. Custom keys should not be sent to our server unless explicitly disclosed. Browser storage is not a secure vault; provide delete-key controls and document limitations. Never log keys, answer content, or images.

## Ads and data collection
- Core OMR screens must not depend on ads.
- Do not attach test IDs, answers, answer keys, or score details to ad requests.
- Ad providers may process IP addresses, identifiers, or usage signals; do not promise zero data collection unless verified.
- Review consent, regional requirements, and contextual/non-personalized settings before launch.
- No first-party analytics or telemetry by default.

## Failure handling
Storage quota, provider timeout, malformed AI output, invalid custom key, unavailable ads, and stale service-worker cache must never destroy local responses. Show a recoverable message and preserve the last saved test.
