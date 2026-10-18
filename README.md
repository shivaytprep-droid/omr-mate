# OMR Mate

Offline-first, privacy-focused OMR mock-test companion.

Create OMR sheets with A-D/A-E options, save responses locally, calculate scores offline, and optionally use AI evaluation or online ads.

## Planning documents
- [Product plan](PRODUCT_PLAN.md)
- [Roadmap](ROADMAP.md)
- [UI/UX specification and wireframes](UI_UX_SPEC.md)
- [Architecture and privacy](ARCHITECTURE_PRIVACY.md)
- [Test plan](TEST_PLAN.md)

## Principles
- Local-first IndexedDB storage; no account required.
- No analytics or telemetry by default.
- Core OMR and manual scoring work offline.
- AI uploads require explicit consent; scores are calculated by app code.
- Ads are online-only and never appear inside the active OMR flow.

**Status: planning phase only.** Application implementation has not started.
