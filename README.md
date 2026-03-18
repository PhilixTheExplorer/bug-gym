# Bug Gym

<div align="center">

<img src="./src/main/resources/icons/bug-gym.png" alt="Bug Gym Logo" width="200"/>
<br/>
<br/>

<img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/JavaFX-21-blue?style=for-the-badge&logo=java&logoColor=white" alt="JavaFX 21"/>
  <img src="https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>
  <img src="https://img.shields.io/badge/License-GPLv3-blue?style=for-the-badge" alt="GPLv3"/>

</div>

---

**Bug Gym** is a lightweight, offline Java practice desktop app that simulates Moodle-style exam questions with syntax highlighting, automated tests, and beginner-friendly progression. No logins, no internet, and no complex IDE setups required. Just open the app and start practicing.

At university (specifically KMUTT's SIT), students often take programming exams in stripped-down environments like Moodle or jGRASP. This creates an unfair gap for beginners:
- **Moodle** is locked to exam time and its editor is a plain text box.
- **jGRASP** lets you code but gives you zero practice questions.
- **LeetCode / HackerRank** are overwhelming for beginners just trying to understand arrays.

**Bug Gym bridges that gap.** It gives you realistic practice in a safe, offline space.  
**Bug Gym = jGRASP's simplicity + Moodle's instant testing + beginner-friendly problem sets.**

## Screenshots

### Main Interface

![Bug Gym Main Screen](screenshots/main-screen.png)

## Overview

Bug Gym runs purely locally on **Windows, macOS, and Linux**. No accounts, no internet, no cloud dependencies—just focused learning.

## Why Bug Gym

- **Exam-Realistic Practice:** Moodle-style Java questions designed for beginner-friendly progression.
- **Intentionally Started Like jGRASP:** The editor began in a strict, minimal style (manual indentation/brackets) to match third-year exam habits, then evolved with *toggleable* Auto Indent and Auto Bracket Pairing based on learner feedback.
- **Zero Friction:** Offline-first desktop workflow with no login, no internet, and no complex IDE setup.
- **Instant Validation:** Built-in automated tests with clear pass/fail status and mismatch feedback.
- **Content That Teaches:** Markdown-based question packs with hints, examples, and starter code.
- **Visible Progress:** Local progress tracking so you can see growth over time.

For the full walkthrough, see [FEATURES.md](FEATURES.md).

## Quick Start

### Install from Releases

1. Head over to the [Releases](https://github.com/PhilixTheExplorer/bug-gym/releases) page.
2. Download the installer for your OS.
3. Install, launch, and start coding.

### Security Notice (Unsigned Installers)

Bug Gym is an open-source project without paid code-signing certificates. Your OS may show warnings:

- **macOS**: Go to System Settings → Privacy & Security → allow Bug Gym to "Open Anyway"
- **Windows**: Click "More Info" → "Run Anyway"

**If you downloaded from the official GitHub Releases page, it's safe.**

## Build From Source

### Requirements

- Java 21+ ([Download OpenJDK](https://adoptium.net/))
- Maven 3.8+ ([Download Maven](https://maven.apache.org/download.cgi))

### Run

```bash
git clone https://github.com/PhilixTheExplorer/bug-gym.git
cd bug-gym
mvn clean javafx:run
```

### Package

```bash
mvn clean package
```

### VS Code (Optional)

If you use VS Code, both are supported:

- Java General profile
- Run and debug directly in VS Code

Recommended extensions:

- Extension Pack for Java
- Debugger for Java

Launch/debug main class:

- `dev.philixtheexplorer.buggym.Launcher`

## Documentation

- [FEATURES.md](FEATURES.md) : full feature breakdown
- [ARCHITECTURE.md](ARCHITECTURE.md) : project structure and design
- [CONTRIBUTING.md](CONTRIBUTING.md) : contribution workflow and standards
- [CHANGELOG.md](CHANGELOG.md) : release history

## Why This Started

Bug Gym began as a personal project, a gift for someone special to make learning Java feel less scary, more fun, and a little bit magical.

It was built to turn confusion into confidence, and to show that sometimes the best help is a tool made just for them.

If it helps others along the way, that's even better.

## Technology Stack

- **Java 21** - Modern Java features
- **JavaFX 21** - Cross-platform desktop UI
- **RichTextFX** - Code editor with syntax highlighting  
- **CommonMark** - Markdown parsing for problem descriptions
- **jpackage** - Native installers for each platform
- **Maven** - Build automation
- **GitHub Actions** - Automated releases

## Versioning

Bug Gym follows [Semantic Versioning](https://semver.org/):
- **MAJOR**: Breaking changes
- **MINOR**: New features, backward compatible
- **PATCH**: Bug fixes

## License

This project is licensed under the **GNU General Public License v3.0**.

You are free to use, modify, and distribute this software, but any modifications must also be open source under GPL v3.

See [LICENSE](LICENSE) for full details.

## Contributing

Contributions are welcome and should be small, focused, and well-tested. This project is primarily maintained as a learning and personal project.

- Fork the repository and create a feature branch
- Keep changes scoped to a single purpose
- Update or add tests when appropriate
- Open a pull request with a clear description of the change 

See [CONTRIBUTING.md](CONTRIBUTING.md) for detailed guidelines.

## Support

- 🐛 **Bug Reports**: [Open an issue](https://github.com/PhilixTheExplorer/bug-gym/issues)
- 💡 **Idea**: [Start a discussion](https://github.com/PhilixTheExplorer/bug-gym/discussions)
- ❓ **Questions**: [Discussions Q&A](https://github.com/PhilixTheExplorer/bug-gym/discussions/categories/q-a)

## Acknowledgments

Built with:
- [JavaFX](https://openjfx.io/) - Application framework
- [RichTextFX](https://github.com/FXMisc/RichTextFX) - Code editor component
- [CommonMark Java](https://github.com/commonmark/commonmark-java) - Markdown parsing

Inspired by the struggle of learning Java without the right tools.

---

<div align="center">

**Made with ❤️ for Java beginners everywhere**

*Especially for one person who inspired it all*

</div>