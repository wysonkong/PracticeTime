# Debugging Full-Stack Apps

**Stack:** React + TypeScript (Vite) frontend, Java Spring Boot backend
**Tools covered:** Chrome DevTools (Sources breakpoints, ⌘O quick-open, Network tab), attaching a JVM remote debugger from your IDE (IntelliJ / VS Code), and reading/running a failing test suite (JUnit 5 + Vitest)

This class is structured around **Kolb's Experiential Learning Cycle**: Concrete
Experience → Reflective Observation → Abstract Conceptualization → Active
Experimentation. Instead of lecturing about debugging tools, you *hit a real bug*,
*reflect on what you saw*, *learn the underlying model of how the tools work*,
then *apply it* to a second bug on your own.

There are two intentionally broken things in this repo — one in the backend,
one in the frontend — and each has a failing automated test alongside it. You
will not fix them by reading the code top to bottom; you're meant to find
them with a debugger (or a failing test's assertion message), on purpose.

---

## 1. Concrete Experience — "run it and watch it lie to you"

1. Start the backend:`./gradlew bootRun` from `backend/`.
2. Start the frontend: `cd frontend && npm install && npm run dev`.
3. Open the app in Chrome. You'll see a task list with a "Completed: N" counter.
4. Mark several tasks as completed. Watch the counter.


## 2. Reflective Observation — "look at the evidence before touching code"

This is where you use the browser and the debugger to gather facts instead of
guessing from the source.

### Chrome Network tab
1. Open DevTools (⌥⌘I / F12) → **Network** tab.
2. Reload the page, click the request to `/api/tasks`.
3. Look at the **Response** sub-tab. Compare the JSON field names to what the
   UI is displaying. This tells you whether the backend is sending correct
   data that the frontend is mangling, or bad data to begin with.

### Run the test suites
Both bugs have a failing test waiting for you — this is often faster than a
manual repro, and it's usually the first thing you'd do at a real job before
reaching for a debugger at all.

```bash
# backend
cd backend && ./gradlew test

# frontend
cd frontend && yarn test
```

Read the failure output closely — `summaryCountsEveryCompletedTaskNotJustOne`
and the two `App.test.tsx` cases print exactly what was expected vs. what
actually came back. That's a second, code-level form of the same evidence the
Network tab gives you in the browser: a concrete expected-vs-actual mismatch,
before you've read a line of implementation.

A failing test is also just another place to put a breakpoint. Instead of
running the whole app to reproduce a bug by hand, you can run a single test
under the debugger and land exactly at the failing assertion:
- **IntelliJ:** right-click the test method (or the `▶` gutter icon) →
  **Debug 'testName()'**. Set a breakpoint inside `getSummary()` first.

### Chrome Sources tab + breakpoints
1. Switch to the **Sources** tab.
2. Press **⌘O** (Cmd+O on Mac, Ctrl+O on Windows/Linux) — this is "Quick Open
   File." Type `api.ts` (or `App.tsx`) to jump straight to it without hunting
   through the file tree.
3. Click a line number next to where the response is turned into UI state to
   set a breakpoint. Reload the page.
4. When execution pauses, hover variables or use the **Scope**/**Watch** panel
   to inspect the actual object shape coming back from `fetch`.

### Attaching the backend (Java) debugger
The Spring Boot Gradle plugin can start the app with remote debugging enabled
on port `5005` via a single flag: `./gradlew bootRun --debug-jvm` (see
`backend/README.md` for details). To attach:

- **IntelliJ:** Run → Edit Configurations → **+ → Remote JVM Debug** → host
  `localhost`, port `5005` → Debug. Set a breakpoint in
  `TaskService.java` on the line that builds the completed count, then trigger
  a request from the frontend.

Step through line-by-line and watch the local variable that's supposed to be accumulating the completed count.
Note the exact line where its value stops matching your expectation.

## 3. Abstract Conceptualization — "why did that work?"

Now connect what you just did to the underlying model:

- **A breakpoint doesn't stop your app** — it pauses the *thread/process* at
  that exact instruction, before it runs, so you can inspect memory state
  (variables, call stack) that would otherwise vanish in microseconds.
- **The Network tab shows you the contract** between frontend and backend —
  the actual bytes on the wire — independent of what either side's code
  *claims* about that contract in a TypeScript interface or a Java DTO. When
  frontend and backend disagree, this is your source of truth.
- **Remote (attach) debugging** works because the JVM can expose a debug
  protocol (JDWP) on a socket. Your IDE is just another JDWP client — the same
  mechanism works whether the JVM is on your laptop, in a Docker container, or
  on a remote server, as long as the port is reachable.
- **Quiet logic bugs vs. loud crashes:** an exception with a stack trace tells
  you where things broke. A silently-wrong value (like an `=` overwrite where
  you meant `+=`) tells you nothing until you step through and watch a
  variable diverge from what you expected — which is why breakpoints +
  watches matter even when nothing is throwing.
- **Call stack navigation:** the call stack pane shows you *who called this
  code*. When a value is wrong, walking up the stack from the breakpoint often
  finds the actual source (a bad argument passed in) rather than the symptom
  (where the bad value is used).

## 4. Active Experimentation — fix it yourself

Now open `EXERCISE.md` and fix both bugs using the workflow above (not by
skimming the diff). Confirm your fix two ways: `./gradlew test` and `yarn test`
both go green, and re-running the concrete experience from Step 1 shows
correct behavior — titles render, and the completed counter increments
correctly as you check more boxes.
