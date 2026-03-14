import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

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

    //Тест-кейс 2. Проверка полей первого поста
    @Test
    public void getFirstPostFieldsTest() {

        RestAssured.given()
                .baseUri("https://jsonplaceholder.typicode.com/")
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("[0].userId", notNullValue())
                .body("[0].id", notNullValue())
                .body("[0].title", notNullValue())
                .body("[0].body", notNullValue());
    }
}