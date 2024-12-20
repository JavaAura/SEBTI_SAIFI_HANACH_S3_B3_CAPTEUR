# 🚀 IoT Monitoring System

## 📂 Project Structure

### 📁 Layers

- Controller Layer: Handles HTTP requests and responses.
- Service Layer: Contains business logic.
- Repository Layer: Communicates with the database.
- Mapper Layer: Transforms entities to DTOs and vice versa.
- Entity Layer: Represents database models.

```bash
🗂️ Key Directories

src/
├── main/
│   ├── java/com/capt/capteurs
│   │   ├── controller/       # Controllers for each feature
│   │   ├── service/          # Business logic
│   │   ├── repository/       # MongoDB repositories
│   │   ├── model/            # Entities
│   │   ├── dto/              # Request and Response DTOs
│   │   └── mapper/           # Mapping logic
│   └── resources/
│       ├── application.yml   # Configuration file
│       └── static/           # Static resources
└── test/
    ├── java/com/capt/capteurs # Unit and integration tests
```

## 🗃️ Main Entities

#### Device

- Attributes: name, type (e.g., temperature, humidity), status (ACTIVE/INACTIVE), lastCommunication

- Relations: Belongs to a Zone, has multiple Measurements and Alerts.

#### Measurement

- Attributes: timestamp, value

#### Alert

- Attributes: severity (NORMAL, LOW, MEDIUM, HIGH, CRITICAL), message, timestamp

#### Zone

- Attributes: name, type, location

- Relations: Contains multiple Devices.

#### User & Role

##### User Attributes: login, password, active, roles

##### Role Attributes: name, level

### 🔗 Relationships

- A Zone can contain multiple Devices.

- A Device can have multiple Measurements and Alerts.

### 🔥 Device Types and Alert Thresholds

##### Temperature Sensors

- Severity

- Range

- Message
```bash
CRITICAL

> 40°C or < -10°C

"Risque immédiat pour les équipements."
```
```bash
HIGH

35-40°C or -5°C to -10°C

"Action rapide nécessaire."
```
```bash
MEDIUM

30-35°C or 0°C to -5°C

"Situation à surveiller."
```
```bash
LOW

25-30°C

"Légère déviation."
```
```bash
NORMAL

20-25°C

"Plage optimale."
```
##### Humidity Sensors

- Severity

- Range

- Message

```bash
CRITICAL

> 90% or < 20%

"Risque de dommages matériels."
```
```bash
HIGH

80-90% or 20-30%

"Conditions défavorables."
```
```bash
MEDIUM

70-80% or 30-40%

"Situation à surveiller."
```
```bash
LOW

65-70% or 40-45%

"Légère déviation."
```
```bash
NORMAL

45-65%

"Plage optimale."
```
### 🌟 Features

- Device Management
- List devices with pagination (/api/user/devices/**)
- Add a device (Admin only) (/api/admin/devices/**)
- Zone Management
- View zones (/api/user/zones/**)
- Add a zone (Admin only) (/api/admin/zones/**) 
- Measurement Management
- View measurements (/api/user/measurements/**)
- Add a measurement (/api/user/measurements/**)
- Export measurements (/api/admin/measurements/export)
- Alert Management
- View alerts (/api/user/alerts/**)
- User Management
- Authentication (/api/auth/login)
- Register new users (/api/auth/register)
- Manage roles (Admin only) (/api/admin/users/{id}/roles)

### 🔒 Security

- Stateless authentication with JWT.
- Role-based access control:
```bash 
/api/admin/* requires ADMIN role.
```
```bash
/api/user/* requires USER role.
``` 
- Password encryption with BCryptPasswordEncoder.

### 🐳 Docker

Dockerfile for containerization.

Build and push Docker images to DockerHub via Jenkins.

### 🛠️ Quality Assurance

SonarLint: Local code quality checks.

Spring Actuator: Monitoring and health checks.

GitHub Actions:

Unit tests.

Syntax validation.

Dependency vulnerability scans.

Style and conventions.

Compilation checks.

Jenkins Pipeline:

Builds Docker images.

Deploys images.

### 📦 Technologies

Framework: Spring Boot

Database: MongoDB

API Style: RESTful

Validation: Bean Validation

Exception Handling: @ControllerAdvice

Monitoring: Spring Actuator