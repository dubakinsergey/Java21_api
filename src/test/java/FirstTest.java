import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.greaterThan;

public class FirstTest {

    @Test
    public void getUsersTest() {

        RestAssured.given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .log().all() // логируем запрос
                .when()
                .get("/posts")
                .then()
                .log().all() // логируем ответ
                .statusCode(200)
                .body("size()", greaterThan(0));
    }
}