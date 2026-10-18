# Product Plan

## Vision
Build a privacy-first, offline-first OMR companion for students taking mock exams. It works without an account, cloud database, or AI subscription.

## Core journey
1. Create a test with a name, question count, A-D/A-E options, marks, negative marking, and optional duration.
2. Fill one option per question and leave any question blank.
3. Review answered, unanswered, and marked-for-review questions.
4. Submit and lock a response snapshot.
5. Evaluate with manual answer-key entry (offline), default AI, custom API, or copied prompt.
6. View deterministic score, question-wise comparison, and saved result.

## Principles
- Store tests, responses, answer keys, and results locally by default.
- No mandatory account, analytics, telemetry, or automatic sync in the MVP.
- Do not send answer data or images to a third party without explicit consent.
- Application code calculates marks; AI may extract a structured answer key but does not calculate the authoritative score.
- Flag uncertain or incomplete answer-key extraction for user review.
- Ads are optional, online-only, and outside the active test flow.

## MVP scope
- Create, rename, duplicate, resume, and delete tests.
- A-D/A-E selection, question palette, clear answer, and mark for review.
- IndexedDB autosave and recovery after refresh/reopen.
- Review/submit confirmation and locked response snapshot.
- Manual answer-key entry, deterministic scoring, and question-wise review.
- Copy OMR answers, copy an evaluation prompt, export/import backup.
- Local data management and clear-all confirmation.

## Later scope
- Optional default AI and custom provider adapter.
- Photo/PDF answer-key extraction with confidence and correction UI.
- Credit/usage controls for default AI.
- Online ads after privacy and consent review.
- Section analytics and result comparisons.

## Non-goals
- Mandatory login or cloud sync.
- Server-side storage of student responses.
- AI arithmetic as the source of truth.
- AI required to calculate a score.
- Ads while offline or during answer selection.

## Acceptance criteria
- Create, fill, save, reload, score, and export a test with airplane mode enabled.
- Reloading or reopening does not lose saved answers.
- Scores match hand-calculated fixtures.
- Core offline workflows make no network requests.
- External upload only happens after a clear user action and consent.

## Decisions before implementation
- Confirm PWA-only versus native Android wrapper.
- Confirm supported browsers and minimum viewport.
- Decide whether sections are MVP or later.
- Define backup format and compatibility policy.
- Choose AI and ad providers only after privacy, security, and cost review.
