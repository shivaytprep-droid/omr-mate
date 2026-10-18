# UI/UX Specification and Wireframes

## Visual direction
Mobile-first, calm, exam-focused. Light theme by default: white surfaces, navy text, blue primary actions, green answered state, amber review state, and red errors. Optional dark theme. Use labels and icons together; never rely on color alone.

## Navigation
Bottom navigation outside active tests: Home, My Tests, Results, Settings. Hide it during a test to prevent accidental navigation.

## Screen 1: Home
- App title and local/offline status.
- Primary action: Create New Test.
- Continue Test cards with title, progress, and last saved time.
- Recent results and saved test count.
- Optional online ad slot near the bottom, never above the primary action.

Wireframe:
```text
OMR Mate                       Settings
Offline-ready | Data stays here

[ + Create New Test ]

Continue your test
[ SSC CGL Mock 01     64%  Resume ]

Recent results
[ Result card ] [ Result card ]

[ Optional ad slot - online only ]
Home | My Tests | Results | Settings
```

## Screen 2: Create Test
Fields: test name, question count, A-D or A-E options, marks per correct answer, negative marks per wrong answer, optional duration. Show maximum marks and marking summary before creation.

```text
Create Test
Test name [_____________________]
Questions [25] [50] [100] [Custom]
Options   ( ) A-D  (x) A-E
Correct marks       [+2 v]
Negative marks      [-0.5 v]
Duration            [No timer v]
Maximum marks: 200
[ Create OMR Sheet ]
```

## Screen 3: Active OMR
- Header: test title, question index, progress, save status.
- Large touch targets for A/B/C/D/E.
- One selected answer per question; allow clear answer.
- Mark/unmark for review.
- Palette states: current, answered, unanswered, review.
- Previous, Next, and Review & Submit.
- Save changes locally and show Saving, Saved, or Save failed.

```text
SSC CGL Mock 01                  Saved
Question 12 of 100              +2 / -0.5
Choose your answer
[ A ] [ B ] [ C ] [ D ] [ E ]
[ Clear answer ] [ Mark for review ]
Question palette: [01][02][03][04][05]
                   [06][07][08][09][10]
[ Previous ]                       [ Next ]
Answered 76 | Blank 24 | Review 8
[ Review & Submit ]
```

## Screen 4: Review and submit
Show answered, unanswered, and marked-for-review counts. Offer shortcuts to unanswered/review questions. Warn about blanks and ask for confirmation. Submission creates a response snapshot and locks it by default.

## Screen 5: Answer key and evaluation
Three clear paths:
1. Enter answer key manually (offline).
2. Upload photo/PDF for optional AI extraction (online).
3. Copy OMR answers and an evaluation prompt for an external AI chat.

Before AI upload, explain what file/answer data will be sent and request explicit consent. Preview extracted answers and flag uncertain entries for correction before scoring.

## Screen 6: Result
Show final score, maximum marks, correct/wrong/unattempted counts, positive marks, deductions, attempted-answer accuracy, and question-wise comparison. Mark results provisional while any answer-key entries remain unresolved. Include Copy Result and Export.

## Screen 7: My Tests
Search/sort saved tests. Actions: resume, view result, duplicate, rename, export, delete. Confirm destructive actions.

## Screen 8: Settings
Theme, accessibility, local storage usage, export/import backup, AI provider configuration, ads/privacy details, delete all local data, app version.

## Interaction and accessibility rules
- Target approximately 44 CSS px or larger for touch actions.
- Visible keyboard focus and logical tab order.
- Screen-reader labels announce option selection and palette status.
- Status is communicated through text/icon as well as color.
- Respect text zoom and reduced-motion preferences.
- Handle empty, loading, offline, error, and storage-failure states.

## Ads UX rules
- No ad in the answer grid, question palette, submit confirmation, or score calculation flow.
- Do not load ads while offline.
- Ads must never be required to open saved tests.
- Collapse unavailable ad slots without blocking content.
- Validate provider privacy and consent behavior before launch.
