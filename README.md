# Java21_api

Проект автоматизированного тестирования REST API на Java 21 с использованием RestAssured, TestNG, Owner, Allure и Lombok.

## 🚀 Стек технологий

| Компонент | Инструмент |
|-----------|------------|
| **Язык** | Java 21 |
| **Сборка** | Maven |
| **Тестирование** | TestNG |
| **HTTP-клиент** | RestAssured |
| **Проверки** | AssertJ |
| **Конфигурация** | Owner |
| **Отчёты** | Allure |
| **POJO** | Lombok |
| **VCS** | Git + GitHub |

## 📁 Структура проекта
src/test/java/
├── client/ # TestClient — обёртка над RestAssured
├── config/ # Owner-конфиги (чтение .properties)
├── models/ # POJO с Lombok для запросов и ответов
│ ├── Post.java # ответ (GET, POST, PUT, PATCH)
│ ├── PostRequest.java # запрос (POST)
│ └── PutRequest.java # запрос (PUT, PATCH)
├── tests/ # Тесты, разбитые по HTTP-методам
│ ├── GetTests.java # GET /posts, /posts/1, проверка полей, 404
│ ├── PostTests.java # POST /posts (позитивные + негативные)
│ ├── PutTests.java # PUT /posts/{id} — полное обновление
│ ├── PatchTests.java # PATCH /posts/{id} — частичное обновление
│ └── DeleteTests.java # DELETE /posts/{id} — удаление
└── resources/
└── config.properties # настройки (URL, таймауты)


## ⚙️ Запуск тестов

### Локальный запуск (Maven)

```bash
mvn clean test

Запуск конкретного класса

mvn clean test -Dtest=GetTests
mvn clean test -Dtest=PostTests
mvn clean test -Dtest=PutTests
mvn clean test -Dtest=PatchTests
mvn clean test -Dtest=DeleteTests

Запуск конкретного теста

mvn clean test -Dtest=GetTests#getSpecificPostTest

📊 Allure отчёт

Генерация и открытие отчёта
# Сгенерировать отчёт и открыть в браузере
allure serve target/allure-results

Альтернативный способ
# Сначала сгенерировать
allure generate target/allure-results --clean -o allure-report

# Потом открыть
allure open allure-report
В отчёте:

📈 Dashboard — общая статистика

🧩 Behaviors — группировка по Epic/Feature/Story

📋 Suites — структура по классам

📊 Graphs — графики времени выполнения

🔧 Конфигурация
Настройки проекта находятся в файле:

src/test/resources/config.properties
Пример содержимого:

properties
base.url=https://jsonplaceholder.typicode.com
timeout=5000

🧪 Что покрыто тестами
Метод	Эндпоинт	Что проверяется
GET	/posts	Непустой массив, структура, поля не null
GET	/posts/1	Типы данных, конкретные значения
GET	/posts/99999	404 Not Found
POST	/posts	Создание поста, длинный заголовок, Content-Type
POST	/posts	Негативные сценарии (пустой title, без body, лишние поля)
PUT	/posts/1	Полное обновление всех полей
PATCH	/posts/1	Частичное обновление (только заголовок)
DELETE	/posts/1	Удаление поста

Особенность: Тесты адаптированы под реальное поведение JSONPlaceholder:

Негативные POST-тесты ожидают 201 (API не валидирует)

PUT и PATCH возвращают обновлённые данные

DELETE возвращает 200, но реального удаления нет

📦 POJO с Lombok
PostRequest (для POST-запросов)
java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String body;
    private int userId;
}
PutRequest (для PUT/PATCH-запросов)
java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutRequest {
    private int id;
    private String title;
    private String body;
    private int userId;
}
Post (для ответов)
java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private int id;
    private String title;
    private String body;
    private int userId;
}

✅ Преимущества подхода

POJO + Lombok — типизированные запросы/ответы, минимум кода, автодополнение в IDE

TestClient — единая точка входа, убрано дублирование

Owner — вынос конфигов, поддержка окружений

Allure — красивые отчёты с группировкой

Разделение по классам — чистая структура, удобный запуск

👤 Автор
Сергей Дубакин
GitHub: dubakinsergey
