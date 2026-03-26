package tests_DataProvider;

import client.TestClient;
import data.GetDataProvider;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import models.Post;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetTestsWithDataProvider {

    @Test(dataProvider = "validPostIds", dataProviderClass = GetDataProvider.class)
    @Story("Получение поста по ID")
    @Description("Проверяет, что GET /posts/{id} возвращает корректный пост")
    public void getPostByIdTest(int postId, String description) {

        Allure.addAttachment("Сценарий", description);

        Post post = TestClient.request()
                .when()
                .get("/posts/" + postId)
                .then()
                .statusCode(200)
                .extract()
                .as(Post.class);

        assertThat(post.getId())
                .as("id должен быть " + postId + " для сценария: " + description)
                .isEqualTo(postId);

        assertThat(post.getTitle())
                .as("title не должен быть пустым для сценария: " + description)
                .isNotEmpty();

        assertThat(post.getBody())
                .as("body не должен быть пустым для сценария: " + description)
                .isNotEmpty();

        assertThat(post.getUserId())
                .as("userId не должен быть 0 для сценария: " + description)
                .isNotZero();
    }

    @Test(dataProvider = "invalidPostIds", dataProviderClass = GetDataProvider.class)
    @Story("Негативные сценарии")
    @Description("Проверяет, что GET /posts/{id} с невалидным id возвращает 404")
    public void getPostByInvalidIdTest(int postId, String description, int expectedStatus) {

        Allure.addAttachment("Сценарий", description);

        TestClient.request()
                .when()
                .get("/posts/" + postId)
                .then()
                .statusCode(expectedStatus);
    }

    @Test(dataProvider = "postIdsInList", dataProviderClass = GetDataProvider.class)
    @Story("Проверка наличия постов в списке")
    @Description("Проверяет, что пост с определённым ID присутствует в общем списке")
    public void postExistsInListTest(int expectedId, String description) {

        Allure.addAttachment("Сценарий", description);

        List<Post> posts = TestClient.request()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", Post.class);

        boolean exists = posts.stream().anyMatch(post -> post.getId() == expectedId);

        assertThat(exists)
                .as("Пост с id=" + expectedId + " должен существовать в списке")
                .isTrue();
    }
}