package tests;

import client.TestClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.Post;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("API Тестирование")
@Feature("GET запросы")
public class GetTests {

    @Test
    @Story("Получение списка постов")
    @Description("Проверяет, что GET /posts возвращает непустой массив")
    public void getPostsArrayNotEmptyTest() {

        List<Post> posts = TestClient.request()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", Post.class);

        assertThat(posts)
                .as("Список постов не должен быть пустым")
                .isNotEmpty();
    }

    @Story("Проверка структуры постов")
    @Description("Проверяет, что у всех постов обязательные поля не null")
    @Test
    public void allPostsRequiredFieldsNotNullTest() {

        List<Post> posts = TestClient.request()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", Post.class);

        assertThat(posts)
                .as("У всех постов должен быть userId")
                .allMatch(post -> post.getUserId() != 0);

        assertThat(posts)
                .as("У всех постов должен быть id")
                .allMatch(post -> post.getId() != 0);

        assertThat(posts)
                .as("У всех постов должен быть title")
                .allMatch(post -> post.getTitle() != null);

        assertThat(posts)
                .as("У всех постов должен быть body")
                .allMatch(post -> post.getBody() != null);
    }

    @Story("Проверка типов данных")
    @Description("Проверяет, что userId и id — числа, title и body — строки")
    @Test
    public void singlePostFieldsTypeTest() {

        Post post = TestClient.request()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .as(Post.class);

        assertThat(post.getUserId())
                .as("userId должен быть Integer")
                .isInstanceOf(Integer.class);

        assertThat(post.getId())
                .as("id должен быть Integer")
                .isInstanceOf(Integer.class);

        assertThat(post.getTitle())
                .as("title должен быть String")
                .isInstanceOf(String.class);

        assertThat(post.getBody())
                .as("body должен быть String")
                .isInstanceOf(String.class);
    }

    @Story("Проверка конкретного поста")
    @Description("Проверяет, что пост с id=1 содержит ожидаемые значения")
    @Test
    public void getSpecificPostTest() {

        String expectedTitle = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit";

        String expectedBody = "quia et suscipit\nsuscipit recusandae consequuntur " +
                "expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum " +
                "rerum est autem sunt rem eveniet architecto";

        Post post = TestClient.request()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .as(Post.class);

        assertThat(post.getUserId())
                .as("userId не равен 1")
                .isEqualTo(1);

        assertThat(post.getId())
                .as("id не равен 1")
                .isEqualTo(1);

        assertThat(post.getTitle())
                .as("title не равен expectedTitle")
                .isEqualTo(expectedTitle);

        assertThat(post.getBody())
                .as("body не равен expectedBody")
                .contains(expectedBody);
    }

    @Story("Негативный сценарий")
    @Description("Проверяет, что запрос несуществующего поста возвращает 404")
    @Test
    public void getNonExistingPostTest() {

        TestClient.request()
                .get("/posts/99999")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
    }
}