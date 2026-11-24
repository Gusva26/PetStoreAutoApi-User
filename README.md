# 🐾 PetStore API Automation — Guía de Ejecución

<div align="center">

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Serenity BDD](https://img.shields.io/badge/Serenity%20BDD-3.1.20-blue?style=for-the-badge&logo=cucumber&logoColor=white)
![Cucumber](https://img.shields.io/badge/Cucumber-BDD-23D96C?style=for-the-badge&logo=cucumber&logoColor=white)
![RestAssured](https://img.shields.io/badge/RestAssured-Screenplay-green?style=for-the-badge)
![Maven](https://img.shields.io/badge/Apache%20Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

**Framework de automatización de pruebas para el módulo de Usuarios de Swagger PetStore API aplicando BDD y el patrón Screenplay.**

[Requisitos](#-requisitos-previos) • [Estructura](#-arquitectura-y-estructura) • [Ejecución](#-guía-de-ejecución) • [Reportes](#-reporte-de-resultados-serenity-bdd) • [Matriz de Pruebas](#-matriz-de-escenarios)

</div>

---

## 📌 Descripción del Proyecto

Este proyecto automatiza las pruebas funcionales de servicios REST para la API pública de **[Swagger PetStore v2](https://petstore.swagger.io/)**, específicamente el ciclo de vida de usuarios (`/user`):
- **Creación de usuario** (`POST /user`)
- **Consulta de usuario registrado** (`GET /user/{username}`)
- **Actualización de datos de usuario** (`PUT /user/{username}`)
- **Consulta de usuario inexistente** (`GET /user/{username}` con manejo de error 404)

Está construido bajo el **Patrón Screenplay**, garantizando alta legibilidad, mantenibilidad y escalabilidad, integrado con **Cucumber** para especificaciones en lenguaje Gherkin y **Serenity BDD** para la generación de reportes vivos.

---

## ⚙️ Requisitos Previos

Antes de ejecutar las pruebas, asegúrate de contar con:

| Requisito | Versión mínima | Comando de verificación |
| :--- | :--- | :--- |
| **Java JDK** | 17 LTS o superior | `java -version` |
| **Apache Maven** | 3.8.0 o superior | `mvn -version` |
| **Git** | 2.x | `git --version` |
| **Conexión a Internet** | Activa | Acceso a `https://petstore.swagger.io/v2` |

---

## 🏗️ Arquitectura y Estructura

El proyecto sigue las directrices del patrón **Screenplay**:

```text
PetStoreAutoApi/
├── pom.xml                                  # Dependencias (Serenity, Cucumber, RestAssured, JUnit)
└── src/
    ├── main/java/org/example/
    │   ├── Models/
    │   │   └── UserModel.java               # POJO para la serialización/deserialización de payloads
    │   ├── Questions/
    │   │   ├── ResponseCode.java            # Validaciones sobre el código de estado HTTP
    │   │   └── ResponseField.java           # Validaciones sobre atributos específicos del response body
    │   └── Tasks/
    │       ├── PostUser.java                # Tarea para enviar peticiones POST (/user)
    │       ├── GetUser.java                 # Tarea para consultar usuarios GET (/user/{username})
    │       └── PutUser.java                 # Tarea para actualizar información PUT (/user/{username})
    └── test/
        ├── java/org/example/
        │   ├── RunnerTest.java              # Test Runner principal con CucumberWithSerenity
        │   └── StepDefinitions/
        │       ├── ParameterDefinition.java # Configuración de parámetros personalizados (actor)
        │       └── UserStepDefinition.java  # Mapeo de pasos Gherkin hacia Tasks y Questions
        └── resources/
            └── features/
                └── User.feature             # Escenarios de prueba BDD en Gherkin
```

---

## 🚀 Guía de Ejecución

### 1. Ejecutar toda la suite de pruebas

Para compilar, ejecutar todos los escenarios y generar el reporte agregado de Serenity:

```bash
mvn clean verify
```

> **Nota:** El plugin `maven-failsafe-plugin` está configurado para ejecutar automáticamente clases bajo el patrón `*Test.java`.

---

### 2. Ejecutar por Tags de Cucumber

Puedes segmentar la ejecución mediante tags pasando la propiedad `-Dcucumber.filter.tags`:

#### 🏷️ Ejecutar todo el módulo de Usuarios
```bash
mvn clean verify -Dcucumber.filter.tags="@UserModule"
```

#### 🟢 Ejecutar solo escenarios Positivos
```bash
mvn clean verify -Dcucumber.filter.tags="@Positivo"
```

#### 🔴 Ejecutar solo escenarios Negativos
```bash
mvn clean verify -Dcucumber.filter.tags="@Negativo"
```

#### 🎯 Ejecutar un escenario específico
```bash
# Solo creación de usuario
mvn clean verify -Dcucumber.filter.tags="@CrearUsuario"

# Solo consulta de usuario existente
mvn clean verify -Dcucumber.filter.tags="@ConsultarUsuario"

# Solo actualización de usuario
mvn clean verify -Dcucumber.filter.tags="@ActualizarUsuario"

# Solo validación de usuario inexistente (404)
mvn clean verify -Dcucumber.filter.tags="@ConsultarUsuarioInexistente"
```

#### 🔀 Combinación de Tags (Lógica Booleana)
```bash
# Ejecutar positivos excluyendo la actualización
mvn clean verify -Dcucumber.filter.tags="@Positivo and not @ActualizarUsuario"

# Ejecutar creación o consulta
mvn clean verify -Dcucumber.filter.tags="@CrearUsuario or @ConsultarUsuario"
```

---

### 3. Ejecutar desde tu IDE (IntelliJ IDEA / Eclipse)

1. Navega a:  
   `src/test/java/org/example/RunnerTest.java`
2. Haz clic derecho sobre la clase o el método y selecciona **Run 'RunnerTest'**.
3. Si deseas cambiar los tags desde el IDE, edita la anotación `@CucumberOptions` en [RunnerTest.java](file:///c:/Users/Gusva/Desktop/practicas/PetStoreAutoApi/src/test/java/org/example/RunnerTest.java#L8-L14):
   ```java
   @CucumberOptions(
           features = "src/test/resources/features",
           glue = "org.example",
           tags = "@UserModule"
   )
   ```

---

## 📊 Reporte de Resultados (Serenity BDD)

Serenity genera automáticamente un reporte visual HTML interactivo con el detalle paso a paso de cada interacción HTTP (requests, headers, bodies, response codes y tiempos).

### 📍 Ubicación del reporte:
```text
target/site/serenity/index.html
```

### 🌐 Cómo visualizar el reporte:

#### En Windows (PowerShell):
```powershell
Start-Process target/site/serenity/index.html
```

#### En Linux / macOS:
```bash
open target/site/serenity/index.html   # macOS
xdg-open target/site/serenity/index.html # Linux
```

#### Regenerar reporte manualmente (sin re-ejecutar pruebas):
```bash
mvn serenity:aggregate
```

---

## 🧪 Matriz de Escenarios

| Tag | Escenario | Método | Endpoint | Status Esperado |
| :--- | :--- | :---: | :--- | :---: |
| `@CrearUsuario` `@Positivo` | Crear un usuario exitosamente (Scenario Outline) | `POST` | `/user` | `200 OK` |
| `@ConsultarUsuario` `@Positivo` | Consultar usuario registrado previamente | `GET` | `/user/{username}` | `200 OK` |
| `@ActualizarUsuario` `@Positivo` | Actualizar email y teléfono de usuario | `PUT` | `/user/{username}` | `200 OK` |
| `@ConsultarUsuarioInexistente` `@Negativo` | Consultar usuario que no existe | `GET` | `/user/{username}` | `404 Not Found` |

---

## 💡 Características Clave de la Implementación

1. **Datos Dinámicos:** En los escenarios de consulta y actualización, se genera un usuario con ID y username dinámico (`System.currentTimeMillis()`) para evitar colisiones y falsos positivos en el entorno compartido de PetStore.
2. **Actor Memory:** Se utiliza `actor.remember("user", ...)` y `actor.recall("user")` para transferir el contexto entre pasos Given, When y Then de forma desacoplada y limpia.
3. **RestAssured Screenplay:** Se emplean las habilidades nativas `CallAnApi.at(...)`, `Post.to(...)`, `Get.resource(...)` y `Put.to(...)`, garantizando que cada llamada HTTP quede registrada detalladamente en el reporte de Serenity.

---

## 👥 Autor
Desarrollado para prácticas de automatización de pruebas API con Serenity BDD y Screenplay.
