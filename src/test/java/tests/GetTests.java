package tests;

import client.TestClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

@Epic("API Тестирование")
@Feature("GET запросы")
public class GetTests {

    @Story("Получение списка постов")
    @Description("Проверяет, что GET /posts возвращает непустой массив")
    @Test
    public void getPostsArrayNotEmptyTest() {

        TestClient.request()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Story("Проверка структуры постов")
    @Description("Проверяет, что у всех постов обязательные поля не null")
    @Test
    public void allPostsRequiredFieldsNotNullTest() {

        TestClient.request()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("userId", everyItem(notNullValue())) // каждый userId не null
                .body("id", everyItem(notNullValue()))
                .body("title", everyItem(notNullValue()))
                .body("body", everyItem(notNullValue()));
    }

    @Story("Проверка типов данных")
    @Description("Проверяет, что userId и id — числа, title и body — строки")
    @Test
    public void singlePostFieldsTypeTest() {

        TestClient.request()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("userId", instanceOf(Integer.class))
                .body("id", instanceOf(Integer.class))
                .body("title", instanceOf(String.class)) // поле title — строка
                .body("body", instanceOf(String.class));
    }

    @Story("Проверка конкретного поста")
    @Description("Проверяет, что пост с id=1 содержит ожидаемые значения")
    @Test
    public void getSpecificPostTest() {

        String expectedTitle = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit";

        String expectedBody = "quia et suscipit\nsuscipit recusandae consequuntur " +
                "expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum " +
                "rerum est autem sunt rem eveniet architecto";

        TestClient.request()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("userId", equalTo(1))
                .body("id", equalTo(1))
                .body("title", equalTo(expectedTitle))
                .body("body", equalTo(expectedBody));
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