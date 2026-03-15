import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class FirstTest {

    String URL = "https://jsonplaceholder.typicode.com";

    //Тест-кейс 1. Массив не пустой
    @Test
    public void getPostsArrayNotEmptyTest() {

        RestAssured.given()
                .baseUri(URL)
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    //Тест-кейс 2. Проверка, что у всех постов обязательные поля не пустые
    @Test
    public void allPostsRequiredFieldsNotNullTest() {

        RestAssured.given()
                .baseUri(URL)
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("userId", everyItem(notNullValue())) // каждый userId не null
                .body("id", everyItem(notNullValue()))
                .body("title", everyItem(notNullValue()))
                .body("body", everyItem(notNullValue()));
    }

    //Тест-кейс 3. Проверка типов данных полей у конкретного поста
    @Test
    public void singlePostFieldsTypeTest() {

        RestAssured.given()
                .baseUri(URL)
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("userId", instanceOf(Integer.class))
                .body("id", instanceOf(Integer.class))
                .body("title", instanceOf(String.class)) // поле title — строка
                .body("body", instanceOf(String.class));
    }

    //Тест-кейс 4. Проверка конкретного поста по ID
    @Test
    public void getSpecificPostTest() {

        String expectedTitle = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit";

        String expectedBody = "quia et suscipit\nsuscipit recusandae consequuntur " +
                "expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum " +
                "rerum est autem sunt rem eveniet architecto";

        RestAssured.given()
                .baseUri(URL)
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("userId", equalTo(1))
                .body("id", equalTo(1))
                .body("title", equalTo(expectedTitle))
                .body("body", equalTo(expectedBody));
    }

    //Тест-кейс 5. Проверка 404 на несуществующий пост
    @Test
    public void getNonExistingPostTest() {

        RestAssured.given()
                .baseUri(URL)
                .when()
                .get("/posts/99999")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
    }
}