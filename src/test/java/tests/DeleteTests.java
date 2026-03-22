package tests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

public class DeleteTests {

    String URL = "https://jsonplaceholder.typicode.com";

    /**
     * Тест-кейс 1. DELETE — удаление поста
     * Что проверяем:
     * - При DELETE сервер удаляет ресурс
     * - Статус 200 (или 204 — зависит от API)
     * - После удаления GET на тот же ресурс возвращает 404
     */
    @Test
    public void deletePostTest() {

        int postId = 1;

        // Шаг 1: Удаляем пост
        RestAssured.given()
                .baseUri(URL)
                .when()
                .delete("/posts/" + postId)
                .then()
                .statusCode(200);  // или 204 — зависит от API

        // Шаг 2: Проверяем, что пост действительно удалён
        RestAssured.given()
                .baseUri(URL)
                .when()
                .get("/posts/" + postId)
                .then()
                .statusCode(404);
    }
}