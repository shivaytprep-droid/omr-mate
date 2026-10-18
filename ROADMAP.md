# Development Roadmap

## Phase 0 - Planning (current)
- Define product principles, offline constraints, core screens, and privacy boundaries.
- Review the UI wireframes and acceptance criteria.
- Do not implement application code until planning is approved.

## Phase 1 - Offline OMR MVP
- Build responsive app shell and Home, My Tests, Settings navigation.
- Create tests with name, question count, A-D/A-E options, positive marks, negative marks, and optional timer.
- Build OMR selection screen with question palette, clear answer, and review marker.
- Persist settings and answers in IndexedDB.
- Recover active tests after refresh or app restart.
- Exit criteria: create, fill, save, reload, and resume works in airplane mode.

## Phase 2 - Manual answer key and scoring
- Enter correct answers manually without network access.
- Implement scoring as pure deterministic functions with unit tests.
- Show correct, wrong, blank, positive marks, deductions, and final score.
- Show question-wise comparison and copy answers/prompt.
- Add backup export/import and local data deletion.
- Exit criteria: all scoring fixtures match hand calculations.

## Phase 3 - Optional AI
- Add answer-key photo/PDF selection and preview.
- Ask explicit consent before sending any file or answer data to a provider.
- Parse structured answer-key entries and mark uncertain entries for review.
- Support default provider and optional custom API key.
- Keep score calculation local and deterministic.
- Exit criteria: malformed AI output or provider failures never lose local data.

## Phase 4 - Ads and billing
- Add default AI credits and transparent usage estimates if monetization is enabled.
- Use idempotent payment records and safe retries.
- Review ad provider data collection, consent, and regional requirements.
- Load ads only online and outside active test, submit, and scoring screens.
- Exit criteria: ad or AI outage does not block core functionality.

## Phase 5 - Release hardening
- Accessibility, touch-target, contrast, and keyboard audits.
- Offline service-worker and update tests.
- Storage quota, export/import, and recovery tests.
- Provider key security and upload permissions review.
- Cross-browser/mobile regression suite.
- Exit criteria: all P0 tests pass and privacy statements match actual network behavior.

## Priorities
P0: offline OMR, local persistence, manual scoring, backup/export, privacy controls.
P1: AI extraction, custom provider, default AI credits.
P2: ads, analytics-like features (only if privacy-reviewed), templates, advanced trends.

## Risks
- Local browser data can be cleared: offer backups and explain the limitation.
- OCR can misread options: show confidence and require correction for ambiguous entries.
- Provider costs can vary: estimate usage, set limits, and avoid duplicate charges.
- Ad SDKs may process identifiers: audit provider behavior before launch.
- Service worker updates can be stale: version assets and preserve IndexedDB schema compatibility.
