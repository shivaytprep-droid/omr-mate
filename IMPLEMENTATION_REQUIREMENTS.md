# OMR Mate — Consolidated Implementation Requirements

This addendum is the source of truth for the next implementation phase. Apply it across the Product Plan, Roadmap, UI/UX Spec, Architecture & Privacy, and Test Plan. Where older wording conflicts, this addendum takes precedence.

## 1. Bilingual UI (Hindi and English)
- The first usable UI must support Hindi (हिन्दी) and English.
- Show a visible language switch on the main screen; apply changes immediately without losing the current test or answers.
- Remember the user's language choice on the device.
- Translate navigation, buttons, labels, validation messages, question navigation, empty states, privacy explanations, and offline/online status.
- Use mobile-first responsive layouts and accessible contrast, keyboard focus, and touch targets.
- Prototype entry point: [index.html](index.html).

## 2. Offline-first data boundary
The following data stays on the user's device and must not be transmitted to ad networks, analytics, or telemetry:
- OMR sheets and test definitions
- Marked answers and answer keys
- Test attempts, history, and scores
- Local scoring calculations

The core workflow must create tests, save answers, reopen attempts, and calculate any available manual-key scores with no internet connection. No account is required for these features.

## 3. Online ads boundary
- Ads may be requested only when internet connectivity is available.
- Ads must never cover, interrupt, or appear inside the active answer-marking flow.
- Never pass OMR answers, answer keys, test history, or scores to ad providers, including as custom events or targeting parameters.
- Third-party ad providers may process technical data (for example IP address, device/ad identifiers, diagnostics, and ad interactions). Do not promise zero collection. Disclose the actual provider and actual SDK behavior in the final privacy notice.
- Ads failing offline must not delay or break OMR features. Hide or collapse the ad area offline.
- Keep the ad SDK isolated from local OMR storage.

## 4. Optional AI evaluation
- AI evaluation is not part of the offline core and remains off by default.
- Before any upload, explain what fields are sent, to which provider, and why; require explicit consent.
- Send only the minimum data required for the user-requested evaluation. Do not reuse it for advertising.
- Scores should be calculated by deterministic app code against an answer key, not accepted from an AI response as authoritative.

## 5. Roadmap updates
1. Planning and privacy boundary (current).
2. Bilingual offline UI prototype and usability review.
3. Offline test creation, answer marking, local persistence, answer-key entry, and deterministic scoring.
4. Automated and manual offline/privacy verification.
5. Optional AI with consent and clear upload review.
6. Online-only ads after SDK review, privacy disclosure, and network inspection.
7. Release hardening and accessibility review.

Do not mark later phases complete merely because the prototype UI contains a placeholder.

## 6. Architecture requirements
- Keep UI language preference, test definitions, answer data, and history in local storage/IndexedDB.
- Separate local OMR modules from ads and optional AI integrations.
- Do not initialize ad requests while offline; do not send OMR payloads in ad requests.
- Treat the prototype's localStorage implementation as demonstration code, not a production persistence/security guarantee.
- Before release, inspect real network traffic and verify every third-party SDK's data collection.

## 7. Acceptance tests
- Switch English to Hindi and back on every screen without losing a test or marked answer.
- Reload and verify local test data and selected language persist on the same device.
- In airplane mode, create a test, mark answers, save/exit, reopen, and finish it.
- Verify offline status hides the ad placeholder and core actions remain usable.
- Online network inspection must confirm ad requests contain no questions, answers, answer keys, test IDs linked to user responses, history, or scores.
- Confirm ads are not shown during active answering.
- Confirm AI uploads never occur before explicit consent.
- Verify narrow mobile layout, focus visibility, readable Devanagari text, and accessible control labels.

## 8. Prototype limitations
The current `index.html` is a front-end interaction prototype. It uses browser localStorage, has no production answer-key scoring, does not include an ad SDK, does not call an AI provider, and has not passed a full browser/device or network audit. A visible ad area is only a placeholder. Do not describe the app as release-ready.
