# Changelog

All notable changes to BugGym will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.2.1] - 2026-07-01

### Added
- GitHub issue templates for bug reports and feature requests.
- GitHub pull request template.
- Additional edge-case and boundary examples for DoD CS23 and DoD CS25 question sets.

### Changed
- Updated README with a demo link and refreshed main screen screenshot.
- Refined `25_DoD_CS25_1` question wording and `VehicleTester` formatting.

### Fixed
- Aligned `25_DoD_CS25_1` Question 5 starter code and markdown with the UML.
- Corrected `240126_Lab2` Question 3 markdown wording.

## [1.2.0] - 2026-03-19

### Added
- New structured OOP practice track under `practice/14-oop-progression` with gradual concept progression.
- New OOP core-concepts track under `practice/15-oop-core-concepts` focused on encapsulation, overloading, static/final, abstract classes, interfaces, overriding, and ArrayList-based collaboration.
- Matching solution sets for new practice tracks (`Solution1` to `Solution8` where applicable).
- Additional OOP questions emphasizing ArrayList add/remove flows, composition, and aggregation patterns.

### Changed
- Starter-code extraction strategy standardized around explicit `### Starter Code` sections.
- OOP starter templates in new practice sets simplified to minimal driver/tester-first scaffolding for stronger learner implementation practice.
- Updated `practice/README.md` index to include new OOP modules and question links.
- Updated `pom.xml` project version to `1.2.0`.

### Fixed
- Dynamic code compilation now detects and loads the primary class name instead of forcing class renaming, preventing `cannot find symbol` errors for self-referencing driver classes (for example, `Tester t = new Tester();`).
- Improved markdown question formatting consistency across multiple files (including input/result table normalization and starter-code blocks).

## [1.1.0] - 2026-03-19

### Added
- Home-focused landing experience (`HomePageView`) with clearer navigation into practice workflow.
- Auto Indent and Auto Bracket Pairing support in the code editor.
- Category reset action with confirmation dialog.
- First-launch tip dialog explaining the new editor defaults and where to toggle them.
- Major expansion of practice content (`practice/07` to `practice/13` sets, plus updated practice README).
- New documentation set: `FEATURES.md` and `ARCHITECTURE.md`.
- Expanded `CONTRIBUTING.md` with detailed contributor workflow and VS Code guidance.

### Changed
- Editor defaults now enable Auto Indent and Auto Bracket Pairing by default, while remaining toggleable in Settings.
- Improved application structure and coordination flow for more maintainable behavior across navigation, submission feedback, and update checks.
- Improved execution and background task handling for more reliable run/test workflows.
- Refined startup flow and shared UI service organization.
- Improved editor and UI polish (including line number styling/alignment and related CSS updates).
- Reworked `README.md` for clearer product overview, stronger project story, and streamlined feature snapshot.
- Updated module dependencies to include Java Preferences support (`java.prefs`) and related module cleanup.
- Updated `pom.xml` project version to `1.1.0`.

### Fixed
- Improved output normalization and related result rendering consistency in test feedback paths.

## [1.0.1] - 2026-02-06

### Added
- Keyboard shortcuts for common actions in the app.
- Option to run user code even when no test cases are defined.
- JetBrains Mono font for the code editor.
- Silent startup update check flow.

### Changed
- Updated `README.md` with clearer installation steps and a security notice.
- Updated `pom.xml` project version to `1.0.1`.

### Fixed
- Corrected content in `25_DoD_CS25_1` question set (`Question2.md` and `README.md`).

## [1.0.0] - 2026-01-25

### Added
- Initial release of Bug Gym.
- **Core Application**:
    - JavaFX-based desktop application.
    - Code editor with syntax highlighting (RichTextFX).
    - Configurable for light/dark themes (implied by styles).
- **Problem Solving Features**:
    - Markdown-based problem description viewer.
    - Automated local test runner.
    - Sample input/output comparison.
- **Content**:
    - Embedded comprehensive set of questions:
        - Labs (1-7)
        - Quizzes (1-4)
        - DoD (Do or Die) exam practice sets.
        - CS23/CS25 course specific materials.
- **Build & Distribute**:
    - Maven build configuration.
    - `jpackage` support for creating native installers (MSI, DEB, DMG).
