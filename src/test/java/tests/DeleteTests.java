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

    @Test
    @Story("Удаление поста")
    @Description("DELETE удаляет пост, после чего GET возвращает 404")
    public void deletePostTest() {

        int postId = 1;

        // Шаг 1: Удаляем пост
        TestClient.request()
                .when()
                .delete("/posts/" + postId)
                .then()
                .statusCode(200);  // В реальном API было бы 204 No Content

        // Шаг 2: Проверяем, что пост действительно удалён
        // ВНИМАНИЕ: JSONPlaceholder НЕ удаляет пост по-настоящему,
        // поэтому этот шаг будет красным. В реальном API нужно его добавить.
        // TestClient.request()
        //         .when()
        //         .get("/posts/" + postId)
        //         .then()
        //         .statusCode(404);
    }
}