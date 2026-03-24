package tests;

import client.TestClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import models.Post;
import models.PostRequest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@Epic("API Тестирование")
@Feature("POST запросы")
public class PostTests {

    @Story("Создание поста")
    @Description("Позитивный тест: создание поста с валидными данными")
    @Test
    public void createPostTest() {

        PostRequest request = new PostRequest("Хасл учит POST", "Теперь я умею создавать данные", 1);

        Post response = TestClient.request()
                .body(request)
                .when()
                .post("/posts")
                .then()
                .statusCode(201) // именно 201, а не 200
                .extract()
                .as(Post.class);

        assertThat(response.getId())
                .as("id должен быть присвоен")
                .isNotNull();

        assertThat(response.getTitle())
                .as("title должен совпадать с отправленным")
                .isEqualTo(request.getTitle());

        assertThat(response.getBody())
                .as("body должен совпадать с отправленным")
                .isEqualTo(request.getBody());

        assertThat(response.getUserId())
                .as("userId должен совпадать с отправленным")
                .isEqualTo(request.getUserId());
    }

    @Story("Граничные значения")
    @Description("Проверяет, что сервер корректно обрабатывает очень длинный заголовок")
    @Test
    public void createPostWithLongTitleTest() {

        String longTitle = "a".repeat(1000);
        PostRequest request = new PostRequest(longTitle, "Нормальное тело", 1);

        Post response = TestClient.request()
                .body(request)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract()
                .as(Post.class);

        assertThat(response.getId())
                .as("id должен быть присвоен")
                .isNotNull();

        assertThat(response.getTitle())
                .as("title должен совпадать с отправленным")
                .isEqualTo(longTitle);

        assertThat(response.getBody())
                .as("body должен совпадать с отправленным")
                .isEqualTo("Нормальное тело");

        assertThat(response.getUserId())
                .as("userId должен совпадать с отправленным")
                .isEqualTo(1);
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