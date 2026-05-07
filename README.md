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
- **Lombok** — сокращение кода в POJO
- **Jackson** — работа с JSON
- **Git / GitHub** — контроль версий

## 📁 Структура проекта
src/test/java/
├── client/ # TestClient — обёртка над RestAssured
├── config/ # Owner-конфиги (чтение .properties)
├── models/ # POJO для запросов и ответов
│ ├── Post.java # Ответ сервера (с id)
│ ├── PostRequest.java # Запрос на создание (Builder, валидация)
│ ├── PutRequest.java # Запрос на обновление (Builder, валидация)
│ └── PatchRequest.java # Запрос на частичное обновление (Builder)
├── factory/ # Factory для создания тестовых данных
│ └── PostFactory.java # validPost(), postWithEmptyTitle(), postWithLongTitle()
├── tests/ # Старые тесты (поддерживаются)
│ ├── GetTests.java
│ ├── PostTests.java
│ ├── PutTests.java
│ ├── PatchTests.java
│ └── DeleteTests.java
├── tests_DataProvider/ # Параметризованные тесты
│ ├── GetTestsWithDataProvider.java
│ ├── PostTestsWithDataProvider.java
│ └── PutPatchDeleteTestsWithDataProvider.java
├── data/ # DataProvider для параметризации
│ ├── GetDataProvider.java
│ ├── PostDataProvider.java
│ └── PutPatchDeleteDataProvider.java
└── resources/
├── config.properties # настройки (URL, таймауты)
└── schemas/ # JSON Schema для валидации ответов
├── post-schema.json
└── posts-list-schema.json


## ⚙️ Запуск тестов

### Локальный запуск (Maven)

```bash
mvn clean test
Генерация и открытие Allure-отчёта
После выполнения тестов:

bash
# Сгенерировать отчёт и открыть в браузере
allure serve target/allure-results
Альтернативный способ (сначала сгенерировать, потом открыть):

bash
allure generate target/allure-results --clean -o allure-report
allure open allure-report

🔧 Конфигурация
Настройки проекта находятся в файле:

src/test/resources/config.properties
Пример содержимого:

properties
# Базовый URL API
base.url=https://jsonplaceholder.typicode.com

# Таймаут в миллисекундах
timeout=5000
🏗️ Паттерны проектирования
Builder
Используется для создания объектов PostRequest, PutRequest, PatchRequest:

PostRequest request = PostRequest.builder()
        .title("Заголовок")
        .body("Тело")
        .userId(1)
        .build();

Преимущества:

Читаемый код

Валидация данных перед созданием объекта

Нельзя перепутать порядок параметров

Factory
Используется для создания типовых объектов:

PostRequest request = PostFactory.validPost();
PostRequest request = PostFactory.postWithEmptyTitle();
PostRequest request = PostFactory.postWithLongTitle();
Преимущества:

Централизованное создание объектов

Уменьшение дублирования в тестах

Лёгкая поддержка

🧪 Что покрыто тестами

GET /posts
Получение списка постов

Получение поста по ID

Проверка структуры и типов данных

Негативные сценарии (404)

JSON Schema валидация

POST /posts
Создание поста с валидными данными (happy path)

Граничные значения (пустые строки, null, длинные строки)

Негативные сценарии (пустой заголовок, неверные типы)

PUT /posts/{id}
Полное обновление поста

Негативные сценарии (несуществующий пост)

PATCH /posts/{id}
Частичное обновление поста

DELETE /posts/{id}
Удаление поста

Все тесты параметризованы через DataProvider и используют единый TestClient.

📊 Allure отчёт
После запуска тестов отчёт доступен в браузере:

bash
allure serve target/allure-results
В отчёте представлены:

Общая статистика прохождения тестов

Пошаговое выполнение тестов

Вложения (запросы, ответы, описания сценариев)

Графики времени выполнения

🧪 JSON Schema валидация

Проект использует JSON Schema для проверки структуры ответов:

post-schema.json — проверка одного поста

posts-list-schema.json — проверка массива постов

Схемы обеспечивают:

Наличие всех обязательных полей

Корректные типы данных

Отсутствие лишних полей

📦 Основные зависимости

Зависимость	Версия	Назначение
RestAssured	5.5.0	HTTP-клиент для API
TestNG	7.10.2	Фреймворк для тестирования
AssertJ	3.26.3	Читаемые проверки
Jackson	2.18.0	Работа с JSON
Lombok	1.18.36	Сокращение кода в POJO
Owner	1.0.12	Управление конфигами
Allure	2.29.0	Генерация отчётов
JSON Schema Validator	5.5.0	Валидация структуры ответов

👤 Автор
Сергей Дубакин
GitHub: dubakinsergey
