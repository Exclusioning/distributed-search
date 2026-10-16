# PegSolitaire — Distributed search demo

[![Build Status](https://img.shields.io/github/actions/workflow/status/Exclusioning/distributed-search/maven.yml?branch=main&label=build&style=flat-square)](https://github.com/Exclusioning/distributed-search/actions)
[![Maven Central](https://img.shields.io/badge/maven--artifact-PegSolitaire-orange?style=flat-square)]()
[![License](https://img.shields.io/badge/license-See%20LICENSE-lightgrey?style=flat-square)](LICENSE)

A small Java project that demonstrates search and evaluation strategies for the Peg Solitaire puzzle, with optional components to distribute evaluation work. It includes a playable GUI, search/evaluator modules, and server classes that show how pieces of computation can be separated for distributed processing.

## Why this project is useful

- Demonstrates search strategies and evaluation for board puzzles (Peg Solitaire) in a compact Java codebase.
- Includes a GUI for interactive play and visual debugging of search behavior.
- Contains server modules that illustrate ideas around distributing evaluation / work via an external cache/client (the project depends on Infinispan client).
- Useful as a learning example for search algorithms, basic game architecture, and lightweight distribution patterns in Java.

## Quick links
- Source: src/main/java/com/mycompany/pegsolitaire/
- Build configuration: pom.xml
- License: LICENSE
- Contributing: CONTRIBUTING.md

## Requirements
- Java 24 (project configured with maven.compiler.release = 24)
- Maven 3.6+
- Optional: Infinispan server or other Hot Rod-compatible server if you want to run the distribution components

## Build (from a fresh clone)
```bash
git clone https://github.com/Exclusioning/distributed-search.git
cd distributed-search

# Build the project
mvn clean package
```

After a successful build the main jar will be under:
target/PegSolitaire-1.0-SNAPSHOT.jar

## Run — examples

Run the main application (the project sets the main class to `com.mycompany.pegsolitaire.PegSolitaire`):
```bash
java -cp target/PegSolitaire-1.0-SNAPSHOT.jar com.mycompany.pegsolitaire.PegSolitaire
```

Run the GUI directly (if you prefer to run a specific UI entry):
```bash
java -cp target/PegSolitaire-1.0-SNAPSHOT.jar com.mycompany.pegsolitaire.GameSwing
```

Run server components (used to explore distributed evaluation patterns):
```bash
# start an evaluation server
java -cp target/PegSolitaire-1.0-SNAPSHOT.jar com.mycompany.pegsolitaire.ServerEvaluation

# start a distribution coordinator
java -cp target/PegSolitaire-1.0-SNAPSHOT.jar com.mycompany.pegsolitaire.ServerDistribution
```

Notes:
- The server components reference an Infinispan Hot Rod client dependency (see `pom.xml`). If you run distributed components, ensure a Hot Rod server is reachable and configured as required by your environment.
- If you encounter "no main manifest" errors, run via `mvn exec:java -Dexec.mainClass=com.mycompany.pegsolitaire.PegSolitaire` (requires the exec plugin configured locally).

## Configuration
- JVM options and environment variables are not required for the basic GUI and local runs.
- For distribution features, configure the Infinispan/Hot Rod connection according to your environment. Look into `ServerEvaluation` and `ServerDistribution` classes for the exact connection details and configuration points.

## Project layout
```
pom.xml                         # Maven build file (Java 24, dependencies incl. Infinispan)
src/
  main/
    java/
      com.mycompany.pegsolitaire/
        Board.java              # Board representation & rules
        Move.java               # Move representation
        Evaluator.java          # Search/evaluation logic
        PegSolitaire.java       # Application entry point (main)
        GameGui.java / GameSwing.java  # GUI classes (interactive play)
        ServerEvaluation.java   # Server component for evaluation
        ServerDistribution.java # Coordinator / distribution example
target/                         # Build output (ignored in VCS)
README.md                       # (you are here)
LICENSE                         # Repository license
CONTRIBUTING.md                 # Contribution guidelines (link target)
```

How it fits together:
- The core model is in Board and Move. Evaluator implements search/evaluation algorithms that operate over Board states.
- PegSolitaire (main) wires up UI or CLI and uses Evaluator to compute moves/solutions.
- ServerEvaluation and ServerDistribution illustrate how evaluation work can be offloaded or coordinated (they depend on Infinispan client libraries declared in pom.xml).

## Notable dependencies
- org.infinispan:infinispan-client-hotrod — client for distributed caching / remote execution patterns
- com.epam.jdi:jdi-uitest-gui — GUI/test utilities referenced in pom.xml
- Build: Maven (pom.xml sets project metadata and compiler release)

## Where to get help
- Open an issue: https://github.com/Exclusioning/distributed-search/issues
- Read the code under src/main/java/com/mycompany/pegsolitaire/ for usage patterns and examples.
- For Infinispan-specific questions, consult the Infinispan docs: https://infinispan.org/docs/

## Who maintains this repository
Maintained by: @Exclusioning

If you'd like to contribute, please:
- Read the CONTRIBUTING guidelines: CONTRIBUTING.md
- Open issues for bugs or feature requests
- Submit pull requests against the `main` branch with a clear summary and tests/examples where appropriate

## Contributing
Please follow the repository contribution guidelines in CONTRIBUTING.md (relative link). Keep changes focused and add tests or a short example application run when adding features.

## License
See the LICENSE file in the repository root.

## Small troubleshooting
- If build fails due to Java version, ensure your JAVA_HOME points to a JDK 24 installation or change `maven.compiler.release` in `pom.xml`.
- If the GUI won't start on a headless server, run it on a desktop or use the evaluator/CLI classes to run headless searches.
