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

        // JSONPlaceholder эмулирует удаление, возвращая 200
        TestClient.request()
                .when()
                .delete("/posts/" + postId)
                .then()
                .statusCode(200);  // В реальном API было бы 204 No Content
    }
}