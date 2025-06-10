# Empresa Project

## Overview
This project is a JavaFX application structured using Maven. It serves as a template for building Java applications with a graphical user interface.

## Project Structure
```
Empresa
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── miempresa
│   │   │           └── jdbcapp
│   │   │               └── view
│   │   │                   └── App.java
│   │   └── resources
│   │       └── com
│   │           └── miempresa
│   │               └── jdbcapp
│   │                   └── view
│   │                       └── mainview.fxml
│   └── test
│       └── java
│           └── com
│               └── miempresa
│                   └── jdbcapp
│                       └── view
│                           └── AppTest.java
├── pom.xml
└── README.md
```

## Setup Instructions
1. **Clone the repository**: 
   ```bash
   git clone <repository-url>
   cd Empresa
   ```

2. **Build the project**: 
   Use Maven to build the project.
   ```bash
   mvn clean install
   ```

3. **Run the application**: 
   After building, you can run the application using:
   ```bash
   mvn javafx:run
   ```

## Usage
- The main application logic is contained in `App.java`.
- The user interface is defined in `mainview.fxml`.
- Test cases for the application can be found in `AppTest.java`.

## Dependencies
This project uses Maven for dependency management. Ensure you have Maven installed to manage the project's dependencies effectively.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.