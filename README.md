# OMR Mate

Offline-first, privacy-focused OMR mock-test companion.

## First UI prototype
Open [index.html](index.html) in a browser to try the responsive bilingual prototype. It includes English/Hindi switching, local test creation, A-D/A-E answer marking, save/resume, and a results summary.

## Planning and requirements
- [Product plan](PRODUCT_PLAN.md)
- [Roadmap](ROADMAP.md)
- [UI/UX specification](UI_UX_SPEC.md)
- [Architecture and privacy](ARCHITECTURE_PRIVACY.md)
- [Test plan](TEST_PLAN.md)
- [Offline data and online ads policy](OFFLINE_ADS_PRIVACY_POLICY.md)
- [Consolidated implementation requirements](IMPLEMENTATION_REQUIREMENTS.md)

## Non-negotiable privacy boundary
- OMR sheets, answers, answer keys, test history, and scores stay on the user's device.
- The core OMR flow works offline and requires no account.
- Ads may load only when internet is available, outside active answering. OMR data must never be sent to ad providers.
- Ad providers may process limited technical data; disclose actual provider behavior accurately.
- AI evaluation is optional and requires explicit consent before any necessary data is uploaded.
- No analytics or telemetry by default.

## Prototype limitations
This is a front-end prototype, not a production release. It uses browser localStorage; it does not include real ads, AI integration, answer-key scoring, or a completed network/privacy audit. No claim of zero third-party data collection should be made while ads are enabled.
