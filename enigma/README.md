# Enigma M3 Engine | Historical Crypto Simulator

A high-fidelity digital reconstruction of the Wehrmacht Enigma M3 cipher machine. Developed as a historical research project for ITMO University.

## 🛠 Engineering Overview

This isn't just a "text scrambler." The core logic is a stateful simulation of the original 1930s mechanical hardware. The encryption process follows the exact electrical path of the physical M3 model, implementing:

*   **Rotors (I-V):** Full permutation tables based on historical wiring.
*   **Steckerbrett (Plugboard):** Primary substitution layer before the signal enters the rotor bank.
*   **Umkehrwalze (Reflector):** Model B implementation, ensuring the reciprocal nature of the cipher (Encryption = Decryption).
*   **Mechanical Stepping:** Accurate pawl-and-ratchet mechanism simulation (double-stepping logic included).

## 🚀 Key Features

### 1. Real-Time Signal Trace (The "X-Ray")
The killer feature of this simulator is the **Signal Trace Logger**. It provides a step-by-step breakdown of every single keypress:
`Input -> Plugboard -> Rotor R -> Rotor M -> Rotor L -> Reflector -> Back-propagation -> Output`
Essential for debugging and understanding the inner workings of the Enigma.

### 2. High-Fidelity UI
*   Modern Dark Interface built with JavaFX.
*   Interactive lampboard and keyboard.
*   Visual rotor state indicators.

### 3. Production-Ready Builds
*   Bundled with a custom JRE (no Java installation required for end-users).
*   Available as a Windows Installer and Portable package.

## 💻 Tech Stack
*   **Language:** Java 17+
*   **UI Framework:** JavaFX
*   **Build System:** Maven
*   **Distribution:** jpackage (Native platform binaries)

## 📦 Getting Started

### For Users
1. Head over to the [Releases](../../releases) section.
2. Download `Enigma_Setup.exe` or the Portable ZIP.
3. Run and start encrypting.

### For Developers (Build from source)
```bash
# Clone the repository
git clone https://github.com/Nemati213/Projects.git

# Navigate to the engine folder
cd enigma

# Build the fat JAR
mvn clean package

# Run the app
java -jar target/enigma.jar
```

## 📜 Project Context
This project was built to demonstrate how 20th-century mechanical logic translates into modern object-oriented code. It focuses on the intersection of **Computer Science** and **History**, providing a hands-on tool for cryptology students.

## 👥 Development Team

*   **[Nemati213](https://github.com/Nemati213)** — Achitecture/Core Engine Development
*   **[Roland142](https://github.com/Roland142)** — UI/Research

**Institution:** ITMO University**Institution:** ITMO University
