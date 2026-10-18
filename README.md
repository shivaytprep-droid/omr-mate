# OMR Mate

Offline-first, privacy-focused OMR mock-test companion.

OMR Mate lets students create answer sheets, mark A/B/C/D/E choices, save tests on their own device, and calculate scores against an answer key. Optional online features may provide AI-assisted answer-key extraction and ads; the core OMR workflow must remain usable offline.

## Product principles

- **Local-first:** test answers, answer keys, history, and results stay on the device by default.
- **Offline-capable:** create tests, fill OMR, manually enter keys, score, review, and export without internet.
- **No account required:** no mandatory sign-in, analytics, or telemetry.
- **Explicit sharing:** never upload answer data or answer-key images to AI without a clear user action and consent.
- **Deterministic scoring:** app code calculates marks; AI may extract structured answer keys but must not be trusted to do arithmetic.
- **Respectful monetization:** optional online ads are separated from exam flows and never shown as a requirement to access local tests.

## Documentation

- [Product plan](docs/PRODUCT_PLAN.md)
- [Roadmap](docs/ROADMAP.md)
- [UI/UX specification](docs/UI_UX_SPEC.md)
- [Architecture and privacy](docs/ARCHITECTURE_PRIVACY.md)
- [Test plan](docs/TEST_PLAN.md)

## Planned delivery phases

1. Planning and UX sign-off (current phase).
2. Offline OMR MVP and local persistence.
3. Manual answer-key entry, scoring, and export/import.
4. Optional AI provider integration with explicit consent.
5. Online ads, privacy review, accessibility, and release hardening.

## Status

**Planning only.** No application implementation or external AI/ad integration has been committed yet.

## Non-goals for the first MVP

- Mandatory accounts or cloud sync.
- Server-side storage of student answer sheets.
- AI-generated score calculations.
- Ads while offline or inside the active answer-selection area.
