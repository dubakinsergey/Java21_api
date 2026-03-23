# Java21_api

Проект автоматизированного тестирования REST API на Java 21 с использованием RestAssured, TestNG, Owner и Allure.

## 🚀 Стек технологий

- **Java 21**
- **Maven** — сборка проекта
- **RestAssured** — отправка HTTP-запросов
- **TestNG** — запуск тестов
- **AssertJ / Hamcrest** — проверки (assertions)
- **Owner** — управление конфигурацией (URL, таймауты)
- **Allure** — генерация отчётов
- **Git / GitHub** — контроль версий

## 📁 Структура проекта
src/test/java/
├── client/ # TestClient — обёртка над RestAssured
├── config/ # Owner-конфиги (чтение .properties)
├── tests/ # Тесты, разбитые по HTTP-методам
│ ├── GetTests.java
│ ├── PostTests.java
│ ├── PutTests.java
│ ├── PatchTests.java
│ └── DeleteTests.java
└── resources/
└── config.properties # настройки (URL, таймауты)

## ⚙️ Запуск тестов

### Локальный запуск (Maven)

```bash
mvn clean test

Генерация и открытие Allure-отчёта
После выполнения тестов:

# Сгенерировать отчёт и открыть в браузере
allure serve target/allure-results

Альтернативный способ (сначала сгенерировать, потом открыть):
allure generate target/allure-results --clean -o allure-report
allure open allure-report

🔧 Конфигурация
Настройки проекта находятся в файле:
src/test/resources/config.properties

Пример содержимого:
base.url=https://jsonplaceholder.typicode.com
timeout=5000

📦 Основные зависимости
Зависимость	Версия	Назначение
RestAssured	5.5.0	HTTP-клиент для API
TestNG	7.10.2	Фреймворк для тестирования
Owner	1.0.12	Управление конфигами
Allure	2.29.0	Генерация отчётов

🧪 Что покрыто тестами
GET /posts — чтение списка и конкретных постов, проверка структуры, типов данных, негативные сценарии (404)

POST /posts — создание постов, граничные значения, проверка Content-Type

PUT /posts/{id} — полное обновление поста

PATCH /posts/{id} — частичное обновление

DELETE /posts/{id} — удаление поста

Все тесты разделены по классам и используют единый TestClient.

👤 Автор
Сергей Дубакин
GitHub: dubakinsergey
