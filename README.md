# Bug Gym

<div align="center">

<img src="./src/main/resources/icons/bug-gym.png" alt="BugGym Logo" width="200"/>
<br/>
<br/>

<img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/JavaFX-21-blue?style=for-the-badge&logo=java&logoColor=white" alt="JavaFX 21"/>
  <img src="https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>
  <img src="https://img.shields.io/badge/License-GPLv3-blue?style=for-the-badge" alt="GPLv3"/>

</div>

---

**Bug Gym** is an offline Java practice desktop app that simulates Moodle-style exam questions with syntax highlighting, automated tests, and beginner-friendly progression. No login, No internet required, no complex IDE setup, just focused learning.

## Screenshots

### Main Interface

![Bug Gym Main Screen](screenshots/main-screen.png)

## Supported platforms

Windows • macOS • Linux

## Installation (GitHub Releases)

1. Open the [Releases](https://github.com/PhilixTheExplorer/bug-gym/releases) page for this repository.
2. Download the installer or archive for your operating system.
3. Install or extract, then launch the application.

### Security Notice (Unsigned Installers)

Bug Gym is an open-source project without paid code-signing certificates. Your OS may show warnings:

- **macOS**: Right-click → Open, then confirm
- **Windows**: Click "More Info" → "Run Anyway"

**If you downloaded from the official GitHub Releases page, it's safe.**

## The Problem

**At KMUTT's School of Information Technology (SIT)**, students take Java programming exams through Moodle:
- Moodle is locked to exam time — **no official practice questions**
- Moodle editor = plain text box (no highlighting, no line numbers)
- jGRASP lets you write code but **gives you zero questions to solve**
- LeetCode / HackerRank feel impossible for beginners (too many concepts at once)
- Beginners need **structured, exam-like practice** — but most options are either too basic (just syntax) or way too hard

**Bug Gym** gives you that missing bridge: realistic practice in a safe, offline space.

## What Bug Gym Provides

**Built for exam practice** - Simulates Moodle-style questions offline  
**Markdown problem descriptions** - Clear, readable questions with hints  
**Automated test execution** - Run tests immediately, see what's wrong  
**Offline-first** - Practice anywhere without internet  
**Super beginner friendly** - Questions start from "Hello World" level  
**Open Content Structure** - All questions written in Markdown files (easy to add / modify via source)  
**Progress tracking** - See what you've completed  
**Intentionally minimal feature set** - Just code, test, learn 

## Who Is This For?

- **Students** learning Java in introductory CS courses
- **Educators** who want to create custom practice problems
- **Self-learners** who need structured practice without Moodle
- **Schools** that want an offline alternative for lab exercises

## Comparison

| Feature | Bug Gym | Moodle | jGRASP | LeetCode | VS Code |
|---------|---------|--------|--------|----------|---------|
| **Syntax highlighting** | ✅ | ❌ | ✅ | ✅ | ✅ |
| **Offline practice** | ✅ | ❌ | ✅ | ❌ | ✅ |
| **Beginner problems** | ✅ | Admin dependent | ❌ | Limited (mostly interview-level) | ❌ |
| **Automated testing** | ✅ | ✅ | ❌ | ✅ | Manual |
| **Simple interface** | ✅ | ✅ | ✅ | 🟨 | ❌ |
| **Custom challenges** | ✅ (Via Source) | Admin only | ❌ | ❌ | ❌ |
| **Exam-style practice** | ✅ | ✅ | ❌ | ❌ | ❌ |

**Bug Gym = jGRASP's simplicity + Moodle's testing + beginner-friendly problem sets**

## Features in Detail

### Code Editor
- **Java syntax highlighting** using RichTextFX
- **Line numbers** for easy reference
- **Keyboard shortcuts** 

### Problem View
- **Markdown support** with tables, code blocks, formatting
- **Hint system** to help when stuck
- **Example inputs/outputs** for clarity

### Test Runner
- **Automated compilation** and execution
- **Input/output comparison** to show differences
- **Clear error messages** when tests fail
- **Progress indicators** showing what passed

### User Experience
- Dark/Light mode
- Adjustable font size
- Resizable layout
- Works offline (practice anywhere)
- Progress tracking

## Building from Source

### Requirements
- Java 21+ ([Download OpenJDK](https://adoptium.net/))
- Maven 3.8+ ([Download Maven](https://maven.apache.org/download.cgi))

### Build & Run
```bash
# Clone repository
git clone https://github.com/PhilixTheExplorer/bug-gym.git
cd bug-gym

# Run application
mvn clean javafx:run

# Build JAR
mvn clean package
# Output: target/bug-gym-1.0.0.jar

# Build native installer (requires platform-specific tools)
# See .github/workflows/release.yml for details
```

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