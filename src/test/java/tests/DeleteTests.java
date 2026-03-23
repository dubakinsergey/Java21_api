package tests;

import client.TestClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

@Epic("API Тестирование")
@Feature("DELETE запросы")
public class DeleteTests {

    @Story("Удаление поста")
    @Description("DELETE удаляет пост, после чего GET возвращает 404")
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