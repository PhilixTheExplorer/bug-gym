# The Forge: Inside Bug Gym

Learning to program isn't just about reading documentation—it's about writing code, failing fast, and adapting. Bug Gym strips away the overhead of heavy IDEs and provides a focused, offline forge where you hone your skills. Here's how it's built to keep you in the flow.

## At a Glance

- Offline, exam-style Java practice with structured beginner progression.
- In-memory compile-and-run feedback with detailed test mismatch visibility.
- Local-first progress tracking with no login, account, or cloud dependency.

## The Core Philosophy

Bug Gym exists to solve one problem: creating the perfect training ground for exam-style Java practice. 
- **Zero Distractions:** No project configuration or boilerplate.
- **Immediate Feedback:** Instant in-memory compilation.
- **Transparent Progress:** Everything runs and saves locally, keeping you in complete control.

## The Arena: User Experience

Your workflow is split into two focused spaces:

- **The Dashboard (Home):** A categorized overview of your progress. Rings visualize your achievements across challenges like Labs, Quizzes, and Exams.
- **The Workbench (Practice):** A flexible layout featuring the question description, an advanced code editor, and a live results panel. Jump between problems, toggle sidebars for focus, and iterate quickly.

## The Anvil: A Smart, Lightweight Editor

Bug Gym provides just enough assistance to make you productive. Because barebones editors like jGRASP were famously used in third-year university Java exams, Bug Gym was originally built with that exact spartan rigor—forcing learners to manually track every indent and parenthesis to build discipline. Based on user feedback, it evolved. Today, Bug Gym offers modern ergonomics, but keeps you in complete control.

Powered by RichTextFX, the editor offers:

- **Rich Syntax Highlighting:** Making keywords, strings, annotations, and comments distinct.
- **Optional Ergonomics:** Auto Indent on new lines, Auto Bracket Pairing `() {} []`, and smart-skip so you don't double-type closing braces.
- **Accessibility:** Line numbers and instant font zooming to ease eye strain.
- *The Choice is Yours:* On your first visit, a welcoming dialog explains these new defaults. You can easily toggle these smart behaviors off in the Settings to return to a hardcore, old-school practice environment.

## The Blueprints: Content Rendering

Challenge descriptions aren't just plain text—they are responsive, styled Markdown documents rendered using CommonMark and JavaFX WebView. The engine dynamically parses out:

- **Intelligent Extraction:** Bug Gym pulls the title, hints, starter code stubs, and test cases directly from a single Markdown file.
- **Robust Table parsing:** Automatically grabs markdown tables and maps them into "Input" and "Expected Output" for your tests, complete with inline fallback parsing.

## The Gauntlet: Execution & Feedback

This is where code becomes reality. Bug Gym doesn't spawn external heavy-weight processes; it uses the Java compiler API to run everything in-memory. 

- **Instant Compilation:** Your code is instantly compiled as the `Solution` class.
- **Sandboxed Execution:** We redirect standard inputs and outputs for each test case, enforcing a strict **5-second timeout** to protect against infinite loops.
- **Detailed Intelligence:** A results panel that doesn't just say "Failed." It shows exactly what went wrong. If there's a character-level mismatch, a dedicated Diff Dialog breaks down the expected versus actual output side-by-side.

## The Chronicle: Progress & Privacy

Your achievements are yours alone. Progress isn't locked in the cloud or gated behind an account setup.
- **Local Saves:** Solved flags and all your written code snippets are securely saved to `${user.home}/.buggym/saves`.
- **Persistent State:** Pick up exactly where you left off, instantly.
- Saved on explicit Save Progress action.
- Current code persisted on question switch and app exit.
- Category reset clears solved status and saved code for that category.

## Menus and Keyboard Shortcuts

### File

- Save Progress (`Ctrl+S`)
- Exit (`Ctrl+Q`)

### Edit

- Clear Code (`Ctrl+L`)
- Reset to Starter (`Ctrl+R`)

### View

- Dark Mode toggle (`Ctrl+Shift+D`)
- Home (`Ctrl+1`)
- Practice Workspace (`Ctrl+2`)
- Toggle Sidebar (`Ctrl+B`)
- Zoom In (`Ctrl+=`)
- Zoom Out (`Ctrl+-`)
- Reset Zoom (`Ctrl+0`)

### Settings

- Auto Indent toggle
- Auto Bracket Pairing toggle

### Run

- Run Tests (`F5`)
- Submit Solution (`Ctrl+Enter`)

### Categories

- Jump to the first question of a selected category

### Help

- Show Hint (`Ctrl+H`)
- Keyboard Shortcuts (`Ctrl+Shift+K`)
- Check for Updates
- About Bug Gym

## Submission Flow

When Submit Solution passes all tests:

- current question is marked solved
- sidebar/home progress refreshes
- success dialog appears
- if a next question exists, dialog offers Next Question or Stay Here

## Update Checking

Bug Gym checks the latest release version from GitHub Releases.

Current update UX:

- silent check on startup
- manual check via Help menu
- update available dialog includes clickable releases link
- up-to-date and failure notifications shown for manual checks

## Theming and Visuals

- Dark mode is the current default at startup.
- Light mode can be toggled from View menu.
- Theme updates both JavaFX scene styling and markdown question rendering.

## Platform and Runtime

- Supported OS targets: Windows, macOS, Linux.
- Runtime requires a JDK (not only a JRE) for in-app compilation.
- Build and packaging are Maven-based with `jpackage` profiles per OS.

## Known Scope Boundaries

Current implementation intentionally does not include:

- cloud sync accounts
- online judge integration
- multiplayer/collaborative sessions
- server-side persistence

This keeps the app lightweight and reliable for offline study environments.
