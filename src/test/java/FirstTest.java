import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

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

    //Тест-кейс 6. Создание нового поста (POST)
    @Test
    public void createPostTest() {

        String expectedTitle = "Хасл учит POST";
        String expectedBody = "Теперь я умею создавать данные";
        int expectedUserId = 1;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", expectedTitle);
        requestBody.put("body", expectedBody);
        requestBody.put("userId", expectedUserId);

        RestAssured.given()
                .baseUri(URL)
                .contentType(ContentType.JSON) // говорим серверу, что шлём JSON
                .body(requestBody) // RestAssured сам превратит Map в JSON
                .when()
                .post("/posts")
                .then()
                .statusCode(201) // именно 201, а не 200
                .body("id", notNullValue())
                .body("title", equalTo(expectedTitle))
                .body("body", equalTo(expectedBody))
                .body("userId", equalTo(expectedUserId));
    }

    // Тест-кейс 7. POST — очень длинный заголовок
    @Test
    public void createPostWithLongTitleTest() {

        String longTitle = "a".repeat(1000);
        String body = "Нормальное тело";
        int userId = 1;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", longTitle);
        requestBody.put("body", body);
        requestBody.put("userId", userId);

        RestAssured.given()
                .baseUri(URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo(longTitle))
                .body("body", equalTo(body))
                .body("userId", equalTo(userId));
    }

    // Тест-кейс 8. POST — проверка Content-Type ответа
    @Test
    public void createPostCheckContentTypeTest() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Проверка типа");
        requestBody.put("body", "Проверяем, что ответ — JSON");
        requestBody.put("userId", 1);

        RestAssured.given()
                .baseUri(URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON) // проверяем заголовок ответа
                .body("id", notNullValue());
    }
}