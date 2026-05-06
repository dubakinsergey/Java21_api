package tests;

import client.TestClient;
import factory.PostFactory;
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
        PostRequest request = PostRequest.builder()
                .title(longTitle)
                .body("Нормальное тело")
                .userId(1)
                .build();


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

        PostRequest request = new PostRequest("Проверка типа", "Проверяем, что ответ — JSON", 1);

        Post response = TestClient.request()
                .body(request)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON) // проверяем заголовок ответа
                .extract()
                .as(Post.class);

        assertThat(response.getId())
                .as("id должен быть присвоен")
                .isNotNull();
    }

    @Story("Негативные сценарии")
    @Description("Сервер должен отклонять создание поста с пустым заголовком")
    @Test
    public void createPostWithEmptyTitleTest() {

        PostRequest request = new PostRequest("", "Тело поста", 1);

        // JSONPlaceholder не валидирует, поэтому ожидаем 201
        // В реальном API было бы 400
        TestClient.request()
                .body(request)
                .when()
                .post("/posts")
                .then()
                .statusCode(201);  // JSONPlaceholder создаёт даже с пустым title
    }

    @Story("Негативные сценарии")
    @Description("Сервер должен проверять типы данных — userId должен быть числом")
    @Test
    public void createPostWithUserIdAsStringTest() {

        // Для передачи строки в userId используем Map, так как в POJO поле int
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Хасл");
        requestBody.put("body", "Проверяем тип userId");
        requestBody.put("userId", "один");

        // JSONPlaceholder не валидирует типы, поэтому ожидаем 201
        // В реальном API было бы 400
        TestClient.request()
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201);  // JSONPlaceholder игнорирует тип
    }

    @Story("Негативные сценарии")
    @Description("Сервер должен отклонять запрос без обязательного поля body")
    @Test
    public void createPostWithoutBodyTest() {

        // Создаём запрос без body (null)
        PostRequest request = new PostRequest("Нет тела", null, 1);

        TestClient.request()
                .body(request)
                .when()
                .post("/posts")
                .then()
                .statusCode(201);  // JSONPlaceholder создаёт даже без body
    }

    @Story("Негативные сценарии")
    @Description("Проверяет, как сервер реагирует на лишние поля в запросе")
    @Test
    public void createPostWithExtraFieldTest() {

        // Для лишнего поля используем Map
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
                .statusCode(201);  // JSONPlaceholder игнорирует лишние поля
    }

    @Test
    public void builderTest() {

        PostRequest request = PostRequest.builder()
                .title("Builder тест")
                .body("Создано через билдер")
                .userId(42)
                .build();

        assertThat(request.getTitle())
                .as("Заголовок должен быть 'Builder тест'")
                .isEqualTo("Builder тест");

        assertThat(request.getBody())
                .as("Тело должно быть 'Создано через билдер'")
                .isEqualTo("Создано через билдер");

        assertThat(request.getUserId())
                .as("userId должен быть 42")
                .isEqualTo(42);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void builderValidationTest() {

        PostRequest.builder()
                .title("")  // пустой заголовок — триггерит валидацию
                .body("Тело")
                .userId(1)
                .build();
    }

    @Test
    public void factoryValidPostTest() {

        PostRequest request = PostFactory.validPost();

        assertThat(request.getTitle())
                .as("Заголовок должен быть 'Обычный заголовок'")
                .isEqualTo("Обычный заголовок");

        assertThat(request.getBody())
                .as("Тело должно быть 'Обычное тело поста'")
                .isEqualTo("Обычное тело поста");

        assertThat(request.getUserId())
                .as("userId должен быть 1")
                .isEqualTo(1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void postWithEmptyTitleTest() {

        PostRequest request = PostFactory.postWithEmptyTitle();
    }

    @Test
    public void postWithLongTitleTest() {
        PostRequest request = PostFactory.postWithLongTitle();

        assertThat(request.getTitle())
                .as("Длина заголовка должна быть 1000")
                .hasSize(1000);

        assertThat(request.getBody())
                .as("Тело должно быть 'Тело с длинным заголовком'")
                .isEqualTo("Тело с длинным заголовком");

        assertThat(request.getUserId())
                .as("userId должен быть 3")
                .isEqualTo(3);
    }
}