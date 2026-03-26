package tests_DataProvider;

import client.TestClient;
import data.PostDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import models.Post;
import models.PostRequest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTestsWithDataProvider {

    // ==================== HAPPY PATH ====================

    @Test(dataProvider = "happyPathPostData", dataProviderClass = PostDataProvider.class)
    @Story("Создание поста (happy path)")
    @Description("Проверяет создание поста с валидными данными")
    public void createPostHappyPathTest(String title, String body, int userId, String description) {

        io.qameta.allure.Allure.addAttachment("Сценарий", description);

        PostRequest request = new PostRequest(title, body, userId);

        Post response = TestClient.request()
                .body(request)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract()
                .as(Post.class);

        assertThat(response.getId())
                .as("id должен быть присвоен для сценария: " + description)
                .isNotNull();

        assertThat(response.getTitle())
                .as("title должен совпадать для сценария: " + description)
                .isEqualTo(title);

        assertThat(response.getBody())
                .as("body должен совпадать для сценария: " + description)
                .isEqualTo(body);

        assertThat(response.getUserId())
                .as("userId должен совпадать для сценария: " + description)
                .isEqualTo(userId);
    }

    // ==================== BOUNDARY ====================

    @Test(dataProvider = "boundaryPostData", dataProviderClass = PostDataProvider.class)
    @Story("Граничные значения")
    @Description("Проверяет граничные значения (пустые строки, null, длинные строки)")
    public void createPostBoundaryTest(String title, String body, int userId, String description, int expectedStatus) {

        io.qameta.allure.Allure.addAttachment("Граничный сценарий", description);

        PostRequest request = new PostRequest(title, body, userId);

        // Если ожидаем успешный статус — проверяем тело ответа
        if (expectedStatus == 201) {
            Post response = TestClient.request()
                    .body(request)
                    .when()
                    .post("/posts")
                    .then()
                    .statusCode(expectedStatus)
                    .extract()
                    .as(Post.class);

            assertThat(response.getId())
                    .as("id должен быть присвоен для сценария: " + description)
                    .isNotNull();

            assertThat(response.getTitle())
                    .as("title должен совпадать для сценария: " + description)
                    .isEqualTo(title == null ? null : title);

            assertThat(response.getBody())
                    .as("body должен совпадать для сценария: " + description)
                    .isEqualTo(body == null ? null : body);

            assertThat(response.getUserId())
                    .as("userId должен совпадать для сценария: " + description)
                    .isEqualTo(userId);
        } else {
            // Если ожидаем ошибку — только проверяем статус
            TestClient.request()
                    .body(request)
                    .when()
                    .post("/posts")
                    .then()
                    .statusCode(expectedStatus);
        }
    }

    // ==================== NEGATIVE ====================

    @Test(dataProvider = "negativePostData", dataProviderClass = PostDataProvider.class)
    @Story("Негативные сценарии")
    @Description("Проверяет, что сервер отклоняет некорректные запросы")
    public void createPostNegativeTest(String title, String body, int userId, String description, int expectedStatus) {

        io.qameta.allure.Allure.addAttachment("Негативный сценарий", description);

        PostRequest request = new PostRequest(title, body, userId);

        TestClient.request()
                .body(request)
                .when()
                .post("/posts")
                .then()
                .statusCode(expectedStatus);
    }
}