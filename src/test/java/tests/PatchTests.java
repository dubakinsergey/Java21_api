package tests;

import client.TestClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.PatchRequest;
import models.Post;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API Тестирование")
@Feature("PATCH запросы")
public class PatchTests {

    @Story("Частичное обновление поста")
    @Description("PATCH обновляет только переданные поля, остальные не меняются")
    @Test
    public void updatePostWithPatchTest() {

        int postId = 1;

        PatchRequest request = new PatchRequest();
        request.setTitle("Поменяли только заголовок");

        Post response = TestClient.request()
                .body(request)
                .when()
                .patch("/posts/" + postId)
                .then()
                .statusCode(200)
                .extract()
                .as(Post.class);

        assertThat(response.getTitle())
                .as("title должен измениться")
                .isEqualTo(request.getTitle());
    }

    @Test
    public void patchRequestBuilderTest() {

        PatchRequest request = PatchRequest.builder()
                .title("Новый заголовок")
                .build();

        assertThat(request.getTitle())
                .as("Заголовок должен быть 'Новый заголовок'")
                .isEqualTo("Новый заголовок");

        assertThat(request.getBody())
                .as("Body не должен быть передан")
                .isNull();

        assertThat(request.getUserId())
                .as("UserId не должен быть передан")
                .isNull();
    }
}