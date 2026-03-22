package tests;

import client.TestClient;
import org.testng.annotations.Test;

public class DeleteTests {

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
        TestClient.request()
                .delete("/posts/" + postId)
                .then()
                .statusCode(200);  // или 204 — зависит от API

        // Шаг 2: Проверяем, что пост действительно удалён
        TestClient.request()
                .get("/posts/" + postId)
                .then()
                .statusCode(404);
    }
}