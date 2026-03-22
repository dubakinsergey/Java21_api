package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class PatchTests {

    String URL = "https://jsonplaceholder.typicode.com";

    /**
     * Тест-кейс 1. PATCH — частичное обновление поста
     * Что проверяем: PATCH обновляет только переданные поля,
     * остальные остаются без изменений.
     * Статус 200 — успешно.
     */
    @Test
    public void updatePostWithPatchTest() {

        int expectedUserId = 1;
        int postId = 1;
        String expectedBody = "quia et suscipit\nsuscipit recusandae consequuntur " +
                "expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum " +
                "rerum est autem sunt rem eveniet architecto";

        // Отправляем ТОЛЬКО те поля, которые хотим изменить
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Поменяли только заголовок");

        RestAssured.given()
                .baseUri(URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch("/posts/" + postId)
                .then()
                .statusCode(200)
                .body("userId", equalTo(expectedUserId))
                .body("id", equalTo(postId))
                .body("title", equalTo("Поменяли только заголовок"))
                .body("body", equalTo(expectedBody));
    }
}