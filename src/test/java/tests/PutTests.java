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
}