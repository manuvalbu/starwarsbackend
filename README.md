# 🌌 Star Wars API Proxy (SWAPI) – Hexagonal Architecture

This Spring Boot project serves as a **proxy API** to the official [Star Wars API](https://swapi.py4e.com/), applying filters, sorting, and pagination over people and planets data. It follows **Hexagonal Architecture** principles, ensuring separation of concerns, clean boundaries, and ease of testing.

## 🧩 Architecture

This project uses the **Hexagonal (Ports & Adapters) Architecture**:

```
   [ REST Controller ]
           ↓
    [ Application Services ]
           ↓
     [ Domain Model ]
           ↓
  [ Outbound Adapters (SWAPI) ]
```

- **Inbound Adapter**: REST API (Controller)
- **Application Layer**: Business use cases (PeopleService, PlanetsService)
- **Domain Layer**: Core logic & models (Person, Planet, Query, etc.)
- **Outbound Adapter**: Fetches data from SWAPI and caches it

## 🛠 Technologies

- Java 23
- Spring Boot 3.2.5
- Spring Web / Validation / Scheduling
- Swagger (SpringDoc OpenAPI)
- JUnit 5 + MockMvc for testing
- SWAPI (https://swapi.py4e.com/) as external API

## 🚀 Getting Started

### ✅ Requirements

- Java 21+ or Java 23
- Maven

### 🔧 Run the application

```bash
./mvn clean install
./mvn spring-boot:run
```

## 📡 API Endpoints

Base URL: `http://localhost:8080/api/swapi`

| Method | Endpoint           | Description              |
|--------|--------------------|--------------------------|
| GET    | `/people`          | Get all Star Wars people |
| GET    | `/planets`         | Get all planets          |

### ✅ Query Parameters (Optional)

| Name             | Type        | Description                        |
|------------------|-------------|------------------------------------|
| `filterField`    | `String`    | Field to filter by (e.g. name)     |
| `filterValue`    | `String`    | Value to match                     |
| `filterType`     | `enum`      | One of `CONTAINS`, `EQUALS`, etc   |
| `caseSensitive`  | `boolean`   | Case sensitive if `true`           |
| `sortField`      | `String`    | Field to sort by                   |
| `descending`     | `boolean`   | Sort in descending order if `true` |
| `offset`         | `int`       | Pagination offset (default 0)      |
| `limit`          | `int`       | Pagination limit (default 10)      |

### 🔍 Example URLs for Testing

- `http://localhost:8080/api/swapi/people?filterField=name&filterValue=luke&filterType=CONTAINS`
- `http://localhost:8080/api/swapi/planets?filterField=name&filterValue=Vulp&caseSensitive=true&filterType=CONTAINS`
- `http://localhost:8080/api/swapi/planets?sortField=name&descending=true&limit=5`
- `http://localhost:8080/api/swapi/people?offset=5&limit=5`

## 📖 Swagger UI

Accessible at:

```
http://localhost:8080/swagger-ui.html
```


OpenAPI JSON spec:

```
http://localhost:8080/v3/api-docs
```

## 🕒 Scheduled Caching

The application uses a scheduled job to periodically refresh data from the SWAPI and cache it.

- **Default Cron (Every hour)**:
  ```yaml
  cache:
    cron: "0 0 * * * *"
  ```

## 🧪 Tests

- Unit tests using JUnit & Mockito
- Integration tests with MockMvc
- Scheduled task test with `Thread.sleep` verification
- End-to-end tests

Run tests:

```bash
./mvn test
```

## 📦 Project Structure

```bash
.
├── domain
│   ├── model
│   ├── vo
│   └── port
│   │   ├── in  (Use cases interfaces)
│   │   └── out (Data source interfaces)
├── application
│   └── Services
├── infrastructure
│   ├── adapter
│   │   ├── in  (REST)
│   │   └── out (SWAPI)
│   └── config, utils
```

