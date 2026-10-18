# OMR Mate

Offline-first, privacy-focused OMR mock-test companion.

Create OMR sheets with A-D/A-E options, save responses locally, calculate scores offline, and optionally use AI evaluation or online ads.

## Privacy and connectivity rules
- OMR sheets, answers, answer keys, test history, and scores stay on the user's device and work offline.
- The app does not send OMR data to ad networks. Ads may load only when internet is available and must remain outside the active OMR flow.
- Ad providers may process technical data such as IP address or device identifiers to serve and measure ads; the privacy notice must disclose this.
- When offline, ads do not load and core OMR features continue working.
- AI evaluation is optional and may upload only the necessary data after explicit, informed user consent.
- No analytics or telemetry by default.

## Planning documents
- [Product plan](PRODUCT_PLAN.md)
- [Roadmap](ROADMAP.md)
- [UI/UX specification and wireframes](UI_UX_SPEC.md)
- [Architecture and privacy](ARCHITECTURE_PRIVACY.md)
- [Test plan](TEST_PLAN.md)

## Principles
- Local-first IndexedDB storage; no account required.
- Core OMR and manual scoring work offline.
- Scores are calculated by app code, not by AI.
- Ads are online-only and never appear inside the active OMR flow.

**Status: planning phase only.** Application implementation has not started.
