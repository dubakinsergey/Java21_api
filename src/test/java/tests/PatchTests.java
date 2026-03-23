package tests;

import client.TestClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

@Epic("API Тестирование")
@Feature("PATCH запросы")
public class PatchTests {

    @Story("Частичное обновление поста")
    @Description("PATCH обновляет только переданные поля, остальные не меняются")
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

        TestClient.request()
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