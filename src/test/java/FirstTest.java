import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class FirstTest {

    //Тест-кейс 1. Массив не пустой
    @Test
    public void getPostsArrayNotEmptyTest() {

        RestAssured.given()
                .baseUri("https://jsonplaceholder.typicode.com")
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
                .baseUri("https://jsonplaceholder.typicode.com")
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
                .baseUri("https://jsonplaceholder.typicode.com")
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("userId", instanceOf(Integer.class))
                .body("id", instanceOf(Integer.class))
                .body("title", instanceOf(String.class)) // поле title — строка
                .body("body", instanceOf(String.class));
    }
}