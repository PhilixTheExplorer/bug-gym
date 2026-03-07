# Changelog

All notable changes to BugGym will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
