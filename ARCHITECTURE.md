# Architecture

## Project Structure

```text
bug-gym/
├─ .github/                         # CI/release workflows
├─ screenshots/                     # README/app screenshots
├─ src/
│  └─ main/
│     ├─ java/
│     │  ├─ module-info.java        # Java module dependencies/exports
│     │  └─ dev/philixtheexplorer/buggym/
│     │     ├─ App.java             # JavaFX app lifecycle + main flow wiring
│     │     ├─ Launcher.java        # launch entry point
│     │     ├─ AppBootstrap.java    # dependency composition
│     │     ├─ AppInteractionFactory.java
│     │     ├─ application/         # app-level orchestration/use cases
│     │     ├─ service/             # execution, parsing, persistence, updates
│     │     ├─ model/               # domain models/records
│     │     └─ ui/                  # JavaFX components + UI coordinators
│     └─ resources/
│        ├─ fonts/                  # editor font asset
│        ├─ icons/                  # app icons
│        ├─ styles/                 # JavaFX CSS stylesheets
│        └─ questions/              # markdown question bank + related assets
├─ target/                          # build output
├─ pom.xml                          # Maven build/dependencies/jpackage profiles
├─ README.md
├─ CHANGELOG.md
└─ CONTRIBUTING.md
```

Question content is organized under `src/main/resources/questions/` by category folders (labs, quizzes, DoD sets, and `practice/`).

## Architecture Overview

- Style: layered desktop architecture (JavaFX UI + application/use-case layer + services + domain models).
- Composition: centralized in `AppBootstrap` (creates services, use cases, coordinators, controller).
- Entry flow:
	- `Launcher` -> `App` -> `AppBootstrap.initialize()`.
	- `App` binds UI callbacks to application actions and owns runtime lifecycle.
- Separation of responsibilities:
	- `ui/`: rendering, controls, dialogs, visual state transitions.
	- `application/`: session flow, update-check/use-case execution, app-level coordination.
	- `service/`: question loading/parsing, code compile/run, local persistence, GitHub update lookup.
	- `model/`: question/category/test/result data structures.

## Key Patterns and Conventions

### State Management

- No global state library; state is managed in plain Java objects.
- Primary session state lives in `AppController`:
	- loaded categories/questions
	- current question
	- solved/progress snapshots
- `SessionFlowCoordinator` handles question-selection/navigation flow and persistence timing.
- UI components keep local view state (e.g., sidebar visibility, result panel expand/collapse).

### Data Fetching Strategy

- Local content:
	- `QuestionLoader` recursively reads markdown/resources from classpath (`/questions`).
	- `MarkdownParser` extracts title/hint/starter code/test cases and renders HTML.
- Remote content:
	- `UpdateService` calls GitHub Releases API (`/releases/latest`) via `HttpClient`.
	- update checks run asynchronously through `UpdateCheckUseCase` + `BackgroundTaskRunner`.

### Form Handling

- No form library is used.
- Input handling is event-driven (JavaFX callbacks, key handlers).
- Validation is manual where needed (e.g., selected question checks, empty code checks).

### Routing Structure

- No URL/router concept (desktop app).
- View switching is explicit via `WorkspaceUiCoordinator`:
	- Home page vs Practice workspace
	- Question navigation by tree selection and Prev/Next actions

### Naming and File Conventions

- Packages grouped by technical layer: `application`, `service`, `model`, `ui`.
- Common suffixes:
	- `*UseCase` for async/app actions
	- `*Coordinator` for workflow/UI state transitions
	- `*Factory` for component assembly
	- `*Manager` for persistence utilities
- Models use a mix of classes and Java records (`RunResult`, `TestCase`, `TestResult`).

## Data Flow

### Question loading and display

1. `QuestionLoader` loads markdown/resources into `Question`/`Category`.
2. `AppController` exposes categories/questions to UI.
3. User selects a question in `QuestionTreeView`.
4. `SessionFlowCoordinator` persists current code, sets current question, prepares HTML/code.
5. UI updates:
	 - `WebView` gets rendered question HTML
	 - `CodeEditor` gets starter or saved user code
	 - `ResultsPanel` is cleared

### Run/submit execution

1. `App` triggers `ExecutionUseCase` with current code and question test cases.
2. `BackgroundTaskRunner` executes task off the UI thread.
3. `InProcessCodeExecutionEngine` delegates to `CodeRunner` for compile/run.
4. `RunResult` returns to UI thread callbacks.
5. `ResultsPanel` renders pass/fail details; successful submit marks question solved via `AppController` and persists progress.

### Progress persistence

1. `ProgressManager` stores solved status in `status.properties` and user code files per question.
2. Save/load location: `${user.home}/.buggym/saves`.
3. Progress summary is derived from in-memory question state and shown in UI.

## Feature Organization

- Organization is layer-based, not feature-folder based.
- Feature behavior is composed across layers:
	- Editing: `ui/CodeEditor` + menu settings/callbacks.
	- Question experience: `service/QuestionLoader` + `service/MarkdownParser` + `ui` views.
	- Execution/testing: `application/ExecutionUseCase` + `service/CodeRunner` + `ui/ResultsPanel`.
	- Progress: `application/AppController` + `service/ProgressManager` + UI refresh coordinators.
	- Updates: `application/UpdateCheckUseCase` + `service/UpdateService` + update feedback UI.

## Important Notes

- Code execution is in-process and uses JDK compiler APIs (`javax.tools`); runtime requires a JDK environment.
- `CodeRunner` enforces per-test timeout (5 seconds).
- For run-main mode (questions without test cases), output is still wrapped into a `RunResult`/`TestResult` structure.
- `QuestionLoader` supports recursive discovery of question folders, including nested `practice/` sets.
- Theme switching uses CSS class toggling on the root scene (`light-mode` class).
- Startup performs a silent update check; manual checks show dialogs.