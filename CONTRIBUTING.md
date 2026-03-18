# Contributing to BugGym

Thank you for contributing to BugGym.

This project exists to help beginners practice Java in an exam-like environment, so contributions should prioritize clarity, stability, and student-friendly behavior.

## Principles

- Keep changes focused and easy to review.
- Preserve beginner-friendly UX.
- Prefer simple, readable code over clever code.
- Avoid unrelated refactors in the same pull request.

## Ways to Contribute

- Report bugs with clear reproduction steps.
- Propose UX improvements with before and after behavior.
- Add or improve practice questions under `src/main/resources/questions`.
- Improve docs in `README.md`, `ARCHITECTURE.md`, and this file.
- Fix code issues in small, isolated pull requests.

## Development Setup

### Requirements

- Java 21+
- Maven 3.8+
- Git

### Clone and Run

```bash
git clone https://github.com/PhilixTheExplorer/bug-gym.git
cd bug-gym
mvn clean javafx:run
```

### Build

```bash
mvn clean package
```

### Optional: VS Code Setup

Using VS Code is fully supported for this project.

- The Java General profile is fine.
- Debugging from VS Code is also fine.
- Recommended extensions:
	- Extension Pack for Java
	- Debugger for Java

Run and debug options in VS Code should point to the launcher main class:

- `dev.philixtheexplorer.buggym.Launcher`

## Project Layout

- `src/main/java` : application source code
- `src/main/resources/questions` : question sets, prompts, starter code, solutions
- `src/main/resources/styles` : JavaFX stylesheets
- `src/main/resources/icons` and `src/main/resources/fonts` : app assets

Read `ARCHITECTURE.md` before making structural changes.

## Contribution Workflow

1. Fork the repository.
2. Create a branch from `main`.
3. Make a focused change.
4. Run the app and validate behavior.
5. Commit with a clear message.
6. Open a pull request with context and screenshots when UI is affected.

## Branch and Commit Naming

Recommended branch names:

- `feat/short-description`
- `fix/short-description`
- `docs/short-description`

Recommended commit style:

- `feat: add first-run editor tips dialog`
- `fix: prevent null selection when opening category`
- `docs: clarify setup steps for contributors`

## Coding Guidelines

- Target Java 21.
- Keep methods small and intent-revealing.
- Reuse existing coordinators and factories where possible.
- Do not introduce heavy dependencies for small tasks.
- Keep UI strings user-friendly and concise.

When editing existing classes:

- Follow current naming and formatting style.
- Do not mix feature work with broad formatting-only edits.
- Do not remove or change user progress behavior unless required.

## UI and UX Guidelines

- Respect current menu structure and shortcuts.
- Keep dialogs short and actionable.
- Prefer explicit labels over jargon.
- Ensure changes work in both dark and light mode.

If you change visual behavior, include screenshots in the pull request.

## Question Content Contributions

Question packs are under `src/main/resources/questions`.

For each pack:

- Keep naming conventions consistent with existing folders.
- Include clear prompt text and expected behavior.
- Ensure starter code compiles unless intentionally incomplete for the exercise.
- Ensure provided solutions match expected output and style goals.

Content should be appropriate for introductory Java learners.

## Testing Expectations

Before opening a pull request:

- Launch the app with `mvn clean javafx:run`.
- Verify the changed flow manually.
- Run `mvn clean package` for build-impacting changes.

If your change affects:

- Menu or settings: verify toggles and persistence.
- Question loading: verify questions render and run.
- Editor behavior: verify typing, formatting, and submission flow.

## Pull Request Checklist

- Change is scoped to one purpose.
- Code and docs are updated together when needed.
- No unrelated files are modified.
- Screenshots included for UI changes.
- PR description explains what changed and why.

## Reporting Bugs

Include the following:

- Environment: OS, Java version, and BugGym version.
- Steps to reproduce.
- Expected behavior.
- Actual behavior.
- Screenshots or logs if available.

## Security and Safety

- Do not submit secrets, tokens, or private data.
- Do not include copyrighted question content without permission.
- Keep dependencies up to date and minimal.

## License

By contributing, you agree that your contributions are licensed under the project license:

- GNU General Public License v3.0