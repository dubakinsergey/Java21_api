package tests;

import client.TestClient;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostTests {

    /**
     * Тест-кейс 1. Создание нового поста (POST)
     * Что проверяем: При отправке корректных данных сервер создаёт пост,
     * возвращает статус 201, присваивает id и эхо-возвращает отправленные поля.
     */
    @Test
    public void createPostTest() {

        String expectedTitle = "Хасл учит POST";
        String expectedBody = "Теперь я умею создавать данные";
        int expectedUserId = 1;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", expectedTitle);
        requestBody.put("body", expectedBody);
        requestBody.put("userId", expectedUserId);

        TestClient.request()
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201) // именно 201, а не 200
                .body("id", notNullValue())
                .body("title", equalTo(expectedTitle))
                .body("body", equalTo(expectedBody))
                .body("userId", equalTo(expectedUserId));
    }

    /**
     * Тест-кейс 2. POST — очень длинный заголовок
     * Что проверяем: Сервер корректно обрабатывает длинные строки (1000 символов)
     * и не обрезает их, не падает с ошибкой 500.
     */
    @Test
    public void createPostWithLongTitleTest() {

        String longTitle = "a".repeat(1000);
        String body = "Нормальное тело";
        int userId = 1;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", longTitle);
        requestBody.put("body", body);
        requestBody.put("userId", userId);

        TestClient.request()
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo(longTitle))
                .body("body", equalTo(body))
                .body("userId", equalTo(userId));
    }

    /**
     * Тест-кейс 3. POST — проверка Content-Type ответа
     * Что проверяем: Сервер возвращает данные именно в формате JSON,
     * а не в XML, HTML или другом формате.
     */
    @Test
    public void createPostCheckContentTypeTest() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Проверка типа");
        requestBody.put("body", "Проверяем, что ответ — JSON");
        requestBody.put("userId", 1);

        TestClient.request()
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON) // проверяем заголовок ответа
                .body("id", notNullValue());
    }

    /**
     * Тест-кейс 4. POST — пустой заголовок
     * Что проверяем: Сервер должен отклонять создание поста с пустым title
     * (статус 400 Bad Request), так как это обязательное поле.
     * Если API пропускает — это баг.
     */
    @Test
    public void createPostWithEmptyTitleTest() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", ""); // пустая строка
        requestBody.put("body", "Тело поста");
        requestBody.put("userId", 1);

        TestClient.request()
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(400); // ожидаем Bad Request
    }

    /**
     * Тест-кейс 5. POST — userId передан строкой
     * Что проверяем: Сервер должен проверять типы данных.
     * Если API ожидает число (userId), то передача строки должна вызывать ошибку 400.
     */
    @Test
    public void createPostWithUserIdAsStringTest() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Хасл");
        requestBody.put("body", "Проверяем тип userId");
        requestBody.put("userId", "один"); // строка, а не число

        TestClient.request()
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(400);
    }

    /**
     * Тест-кейс 6. POST — отсутствует обязательное поле body
     * Что проверяем: Если не отправить обязательное поле (body),
     * сервер должен вернуть ошибку 400 Bad Request.
     */
    @Test
    public void createPostWithoutBodyTest() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Нет тела");
        // поле "body" не отправляем
        requestBody.put("userId", 1);

        TestClient.request()
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(400); // Bad Request
    }

    /**
     * Тест-кейс 7. POST — отправка лишнего поля
     * Что проверяем: Как сервер реагирует на лишние поля.
     * В идеале — 400 Bad Request (отклоняет мусор),
     * но некоторые API игнорируют и отдают 201.
     */
    @Test
    public void createPostWithExtraFieldTest() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Лишнее поле");
        requestBody.put("body", "Тело поста");
        requestBody.put("userId", 1);
        requestBody.put("extraField", "этого поля быть не должно");

        TestClient.request()
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(400); // либо 201 — зависит от API
    }
}