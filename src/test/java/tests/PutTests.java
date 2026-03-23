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
@Feature("PUT запросы")
public class PutTests {

    @Story("Полное обновление поста")
    @Description("PUT полностью заменяет существующий пост")
    @Test
    public void updatePostWithPutTest() {

        int postId = 1;

        // В PUT обязательно передаём ВСЕ поля
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", postId);
        requestBody.put("title", "Обновлённый заголовок");
        requestBody.put("body", "Обновлённое тело поста");
        requestBody.put("userId", 777);

        TestClient.request()
                .body(requestBody)
                .when()
                .put("/posts/" + postId)
                .then()
                .statusCode(200)
                .body("id", equalTo(postId))
                .body("title", equalTo("Обновлённый заголовок"))
                .body("body", equalTo("Обновлённое тело поста"))
                .body("userId", equalTo(777));
    }
}