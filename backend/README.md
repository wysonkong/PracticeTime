# Backend — debug-exercise

Spring Boot 3 / Java 17, in-memory task list, no database required. Built with
Gradle via the wrapper (`./gradlew`), so you don't need Gradle installed
globally.

## ⚠️ one-time setup: generate the wrapper jar

`gradle/wrapper/gradle-wrapper.jar` is a binary and isn't committed in this
exercise repo. Before `./gradlew` will run, generate it once:

- **If you have Gradle installed** (`brew install gradle`, `sdk install
  gradle`, etc.): run `gradle wrapper --gradle-version 8.9` from `backend/`.
  This downloads the jar and you never need to think about it again — commit
  it to your fork and everyone who clones it is fine.
- **If you're opening this in IntelliJ:** just open `backend/build.gradle` as
  a project. IntelliJ ships its own bundled Gradle and will offer to
  generate/use the wrapper automatically the first time you sync.

After that, `./gradlew` (or `gradlew.bat` on Windows) works like any other
wrapper script — no local Gradle install required for anyone else.

## Run normally

```bash
./gradlew bootRun
```

Runs on `http://localhost:8080`. `GET /api/tasks` returns tasks + summary.
`PATCH /api/tasks/{id}/toggle` flips a task's completed flag.

## Run with remote debugging enabled (for the class)

The Spring Boot Gradle plugin has a built-in flag for this — no manual JDWP
arguments needed:

```bash
./gradlew bootRun --debug-jvm
```

This opens a debug socket on port `5005` and suspends startup until a
debugger attaches (so you won't miss the very first request). Then attach
from your IDE:

- **IntelliJ IDEA:** Run → Edit Configurations → `+` → **Remote JVM Debug** →
  host `localhost`, port `5005` → OK → click the Debug (bug) icon next to
  that configuration.
- **VS Code:** open this folder in VS Code with the Java Extension Pack
  installed, then use the `.vscode/launch.json` config included at the repo
  root ("Attach to Spring Boot") and press F5.

Set a breakpoint in `TaskService.getSummary()` and hit `GET /api/tasks` from
the frontend (or `curl localhost:8080/api/tasks`) to trigger it.

## Requirements

- Java 17+
- Nothing else global — `./gradlew` fetches the pinned Gradle version itself
  once the wrapper jar exists (see the one-time setup note above)
