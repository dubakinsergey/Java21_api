package tests;

import client.TestClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.Post;
import models.PutRequest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API Тестирование")
@Feature("PUT запросы")
public class PutTests {

    @Story("Полное обновление поста")
    @Description("PUT полностью заменяет существующий пост")
    @Test
    public void updatePostWithPutTest() {

        int postId = 1;
        PutRequest request = new PutRequest(postId, "Обновлённый заголовок", "Обновлённое тело поста", 777);

        Post response = TestClient.request()
                .body(request)
                .when()
                .put("/posts/" + postId)
                .then()
                .statusCode(200)
                .extract()
                .as(Post.class);

        assertThat(response.getId())
                .as("id должен быть %d", postId)
                .isEqualTo(postId);

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

    @Test
    @Story("Негативные сценарии")
    @Description("Проверяет, что PUT несуществующего поста возвращает ошибку")
    public void updateNonExistingPostTest() {
        int postId = 99999;
        PutRequest request = new PutRequest(postId, "Тест", "Тест", 1);

        TestClient.request()
                .body(request)
                .when()
                .put("/posts/" + postId)
                .then()
                .statusCode(500);  // JSONPlaceholder баг: должен быть 404, но возвращает 500
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void putRequestBuilderValidationTest() {

        PutRequest.builder()
                .title("")  // пустой заголовок — должно упасть
                .body("Тело")
                .userId(1)
                .id(1)
                .build();
    }

    @Test
    public void putRequestBuilderTest() {
        PutRequest request = PutRequest.builder()
                .id(1)
                .title("Builder PUT тест")
                .body("Тело PUT запроса")
                .userId(10)
                .build();

        assertThat(request.getId())
                .as("id должен быть 1")
                .isEqualTo(1);

        assertThat(request.getTitle())
                .as("Заголовок должен быть 'Builder PUT тест'")
                .isEqualTo("Builder PUT тест");

        assertThat(request.getBody())
                .as("Тело должно быть 'Тело PUT запроса'")
                .isEqualTo("Тело PUT запроса");

        assertThat(request.getUserId())
                .as("userId должен быть 10")
                .isEqualTo(10);
    }
}