package tests;

import client.TestClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("API Тестирование")
@Feature("POST запросы")
public class PostTests {

    @Story("Создание поста")
    @Description("Позитивный тест: создание поста с валидными данными")
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

    @Story("Граничные значения")
    @Description("Проверяет, что сервер корректно обрабатывает очень длинный заголовок")
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

    @Story("Проверка заголовков")
    @Description("Проверяет, что ответ приходит в формате JSON")
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

    @Story("Негативные сценарии")
    @Description("Сервер должен отклонять создание поста с пустым заголовком")
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

    @Story("Негативные сценарии")
    @Description("Сервер должен проверять типы данных — userId должен быть числом")
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

    @Story("Негативные сценарии")
    @Description("Сервер должен отклонять запрос без обязательного поля body")
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

    @Story("Негативные сценарии")
    @Description("Проверяет, как сервер реагирует на лишние поля в запросе")
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