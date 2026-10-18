# OMR Mate: Offline Data and Online Ads Policy

## Core promise
Offline means the core OMR experience does not require a network connection and personal test data stays on the user's device. Internet access for ads does not grant ad providers access to OMR data.

## Data that stays local
- OMR sheets and question selections
- Marked answers and answer keys
- Test attempts, history, and scores
- Manual scoring and result calculations

These features must work without an account and without a network request. Do not transmit this data to advertising SDKs, ad providers, analytics, or telemetry services.

## Ads when online
- Ads may load only when a network connection is available.
- Ads must not appear in the active OMR answering flow or obstruct test completion.
- The ad integration must receive only the minimum information required by the chosen provider.
- Ad providers may process technical data such as IP address, device identifiers, diagnostics, or ad interaction events. The final privacy notice must identify the actual provider and accurately disclose what it collects.
- Never claim "zero data collection" while third-party ads are enabled.
- When offline, ad requests must not block or delay OMR functionality.

## Optional AI evaluation
AI evaluation is separate from advertising. It is off by default and requires clear, informed consent before any test content or answers are uploaded. Upload only the data necessary for the requested evaluation. Never send data to AI silently. Scores must be computed by app code where applicable, not trusted from an AI response.

## Engineering requirements
- Keep OMR storage and ad integration isolated.
- Do not pass answers, answer keys, test history, or scores as ad targeting parameters or custom events.
- No analytics or telemetry by default.
- Audit network requests with online and offline tests before release.
- If ad SDK behavior cannot be verified or minimized, do not ship that integration until reviewed.

## Release acceptance checks
1. Create, answer, save, and score a test with airplane mode enabled.
2. Verify no OMR content leaves the device when ads load online.
3. Verify ad requests fail gracefully offline and do not block the app.
4. Verify AI uploads occur only after explicit consent.
5. Publish an accurate privacy notice based on the final SDKs and observed network traffic.

This is a product requirement, not a claim that the not-yet-built app has already passed these checks.
