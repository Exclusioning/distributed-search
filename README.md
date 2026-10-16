# PegSolitaire — Distributed search demo

[![Maven Central](https://img.shields.io/badge/maven--artifact-PegSolitaire-orange?style=flat-square)]()

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
```

## Testing & Code Coverage

This project uses **JUnit 5** and **Mockito** for unit testing, with code coverage tracked via **JaCoCo**. 

### Coverage Summary (69% Overall)
* **Core Logic (`Board` / `Move`):** ~99% - 100% instruction coverage.
* **UI & Distributed Components:** Partially tested.

| Package / Class | Instruction Coverage | Branch Coverage | Status |
| :--- | :---: | :---: | :---: |
| `com.mycompany.pegsolitaire.Move` | **100%** | N/A | Fully Tested |
| `com.mycompany.pegsolitaire.Board` | **99%** | **91%** | Fully Tested |
| `com.mycompany.pegsolitaire.Evaluator` | **76%** | **43%** | Good |
| `com.mycompany.pegsolitaire.GameGui` | **45%** | **50%** | Partial |

### How to Run Tests Locally
To execute the test suite and generate a fresh JaCoCo coverage report locally, run:

```cmd
mvn clean test jacoco:report

How it fits together:
- The core model is in Board and Move. Evaluator implements search/evaluation algorithms that operate over Board states.
- PegSolitaire (main) wires up UI or CLI and uses Evaluator to compute moves/solutions.
- ServerEvaluation and ServerDistribution illustrate how evaluation work can be offloaded or coordinated (they depend on Infinispan client libraries declared in pom.xml).

## Notable dependencies
- org.infinispan:infinispan-client-hotrod — client for distributed caching / remote execution patterns
- com.epam.jdi:jdi-uitest-gui — GUI/test utilities referenced in pom.xml
- Build: Maven (pom.xml sets project metadata and compiler release)

## Who maintains this repository
Maintained by: @Exclusioning

