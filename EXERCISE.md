# Exercise: Find and Fix Two Bugs

Do the "Concrete Experience" and "Reflective Observation" steps in `README.md`
first. Come back here once you've actually looked at the Network tab and
attached the backend debugger — this file has hints and, at the bottom, the
full solution. No peeking until you've tried.

## Setup

```bash
# Terminal 1 — backend (remote debug enabled on :5005)
cd backend
# one-time only: generate gradle/wrapper/gradle-wrapper.jar — see backend/README.md
./gradlew bootRun --debug-jvm

# Terminal 2 — frontend
cd frontend
npm install
npm run dev
```

Open the printed Vite URL (usually http://localhost:5173).

## Bug #1 — frontend (task titles show "Untitled")

**Symptom:** every task card shows "Untitled" instead of its real title, even
though you can see the real title somewhere if you dig.

<details>
<summary>Hint 0 — run the tests first</summary>

`npm test` in `frontend/` fails on `App.test.tsx`. Read the assertion output:
it tells you it expected to find "Review pull request" in the document and
didn't. That's the same symptom as the browser, just without needing to click
around — and it's reproducible without a live backend.
</details>

<details>
<summary>Hint 1</summary>

Use the Network tab to look at the raw JSON from `GET /api/tasks`. What is the
field actually called?
</details>

<details>
<summary>Hint 2</summary>

Set a breakpoint in `frontend/src/api.ts` right where the response is parsed,
or in `frontend/src/App.tsx` where tasks are mapped to `<TaskCard>`. Compare
the object's real keys (in the Scope panel) to `frontend/src/types.ts`.
</details>

<details>
<summary>Solution</summary>

The backend sends `title`, but `frontend/src/types.ts` declares the `Task`
interface with a `name` field, and `App.tsx` reads `task.name`. TypeScript
can't catch this because the field comes from an untyped `fetch().json()` —
nothing enforces the interface actually matches the wire data.

Fix: rename `name` → `title` in `types.ts` and the usage in `App.tsx`
(or, more robustly, validate the response shape at the network boundary
instead of trusting a hand-written interface).
</details>

## Bug #2 — backend (completed counter never goes above 1)

**Symptom:** mark 3 tasks as completed — the "Completed" summary still shows
`1` (or `0`), never `3`.

<details>
<summary>Hint 0 — run the tests first</summary>

`./gradlew test` in `backend/` fails on
`summaryCountsEveryCompletedTaskNotJustOne`. The assertion message says it
expected `3` and got something else — confirm what it actually got before
reaching for the debugger.
</details>

<details>
<summary>Hint 1</summary>

This won't throw an exception — there's no stack trace to chase. You need a
breakpoint and to actually watch a variable across loop iterations.
</details>

<details>
<summary>Hint 2</summary>

Attach the debugger (see README "Attaching the backend debugger"), set a
breakpoint inside the loop in `TaskService.getSummary()`, and step through
each iteration. Watch the `completedCount` local variable specifically —
does it *increment*, or does it get *reassigned*?
</details>

<details>
<summary>Solution</summary>

In `backend/.../TaskService.java`, `getSummary()` has:

```java
if (task.isCompleted()) {
    completedCount = 1; // bug: overwrites instead of accumulating
}
```

It should be:

```java
if (task.isCompleted()) {
    completedCount++;
}
```

Stepping through with the debugger makes this obvious in about 10 seconds —
you watch the variable get reset to `1` on every completed task instead of
climbing 1, 2, 3. Reading the code without a debugger, it's easy to skim past
because `= 1` and `++` look similar at a glance, especially in a longer method.
</details>

## Done?

Run `./gradlew test` and `npm test` again — both suites should pass. Then
re-run the app end to end: real titles should render, and the completed count
should track the number of checked boxes exactly. If you're doing this as a
group, compare notes on which tool (failing test, Network tab, or backend
breakpoint) got you to each bug fastest — that's the actual point of the
exercise.
