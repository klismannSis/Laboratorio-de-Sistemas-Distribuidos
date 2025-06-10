# Proyecto Empresa

## Descripción general
Este proyecto es una aplicación JavaFX estructurada usando Maven. Sirve como plantilla para construir aplicaciones Java con una interfaz gráfica de usuario.

## Estructura del proyecto
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

## Instrucciones de configuración
1. **Clona el repositorio**: 
   ```bash
   git clone <repository-url>
   cd Empresa
   ```

2. **Compila el proyecto**: 
   Usa Maven para compilar el proyecto.
   ```bash
   mvn clean install
   ```

3. **Ejecuta la aplicación**: 
   Después de compilar, puedes ejecutar la aplicación con:
   ```bash
   mvn javafx:run
   ```

## Uso
- La lógica principal de la aplicación se encuentra en `App.java`.
- La interfaz de usuario está definida en `mainview.fxml`.
- Los casos de prueba de la aplicación están en `AppTest.java`.

## Dependencias
Este proyecto utiliza Maven para la gestión de dependencias. Asegúrate de tener Maven instalado para gestionar correctamente las dependencias del proyecto.

## Licencia
Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.

## Ejecución gráfica con Java Swing
El proyecto también cuenta con una ejecución gráfica utilizando Java Swing. Puedes encontrar la clase principal en la siguiente ruta:

```
main.java.com.miempresa.jdbcapp.ui.MainApp.java
```