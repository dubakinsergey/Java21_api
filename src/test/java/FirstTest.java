import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class FirstTest {

    String URL = "https://jsonplaceholder.typicode.com";

    /**
     * Тест-кейс 1. Массив не пустой
     * Что проверяем: API возвращает список постов (массив) и он не пустой.
     * Статус 200 означает, что запрос выполнен успешно.
     */
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

    /**
     * Тест-кейс 2. Проверка, что у всех постов обязательные поля не пустые
     * Что проверяем: Каждый пост в массиве содержит поля userId, id, title, body,
     * и они не равны null (даже если пустая строка — это не null).
     */
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

    /**
     * Тест-кейс 3. Проверка типов данных полей у конкретного поста
     * Что проверяем: У поста с id=1 поля соответствуют ожидаемым типам:
     * userId и id — числа (Integer), title и body — строки (String).
     */
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

    /**
     * Тест-кейс 4. Проверка конкретного поста по ID
     * Что проверяем: Пост с id=1 содержит ожидаемые значения всех полей.
     * Сравниваем с эталонными данными из документации API.
     */
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

    /**
     * Тест-кейс 5. Проверка 404 на несуществующий пост
     * Что проверяем: При запросе поста с несуществующим ID сервер возвращает 404 Not Found
     * и пустое тело ответа.
     */
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

    /**
     * Тест-кейс 6. Создание нового поста (POST)
     * Что проверяем: При отправке корректных данных сервер создаёт пост,
     * возвращает статус 201, присваивает id и эхо-возвращает отправленные поля.
     */
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

    /**
     * Тест-кейс 7. POST — очень длинный заголовок
     * Что проверяем: Сервер корректно обрабатывает длинные строки (1000 символов)
     * и не обрезает их, не падает с ошибкой 500.
     */
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

    /**
     * Тест-кейс 8. POST — проверка Content-Type ответа
     * Что проверяем: Сервер возвращает данные именно в формате JSON,
     * а не в XML, HTML или другом формате.
     */
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

    /**
     * Тест-кейс 9. POST — пустой заголовок
     * Что проверяем: Сервер должен отклонять создание поста с пустым title
     * (статус 400 Bad Request), так как это обязательное поле.
     * Если API пропускает — это баг.
     */
    @Test
    public void createPostWithEmptyTitleTest() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", ""); // пустая строка
        requestBody.put("body", "Тело поста");
        requestBody.put("userId", 1);

        RestAssured.given()
                .baseUri(URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(400); // ожидаем Bad Request
    }

    /**
     * Тест-кейс 10. POST — userId передан строкой
     * Что проверяем: Сервер должен проверять типы данных.
     * Если API ожидает число (userId), то передача строки должна вызывать ошибку 400.
     */
    @Test
    public void createPostWithUserIdAsStringTest() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Хасл");
        requestBody.put("body", "Проверяем тип userId");
        requestBody.put("userId", "один"); // строка, а не число

        RestAssured.given()
                .baseUri(URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(400);
    }

    /**
     * Тест-кейс 11. POST — отсутствует обязательное поле body
     * Что проверяем: Если не отправить обязательное поле (body),
     * сервер должен вернуть ошибку 400 Bad Request.
     */
    @Test
    public void createPostWithoutBodyTest() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Нет тела");
        // поле "body" не отправляем
        requestBody.put("userId", 1);

        RestAssured.given()
                .baseUri(URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(400); // Bad Request
    }

    /**
     * Тест-кейс 12. POST — отправка лишнего поля
     * Что проверяем: Как сервер реагирует на лишние поля.
     * В идеале — 400 Bad Request (отклоняет мусор),
     * но некоторые API игнорируют и отдают 201.
     */
    @Test
    public void createPostWithExtraFieldTest() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Лишнее поле");
        requestBody.put("body", "Тело поста");
        requestBody.put("userId", 1);
        requestBody.put("extraField", "этого поля быть не должно");

        RestAssured.given()
                .baseUri(URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(400); // либо 201 — зависит от API
    }

    /**
     * Тест-кейс 13. PUT — полное обновление поста
     * Что проверяем: PUT полностью заменяет существующий пост.
     * Все поля должны обновиться, старые данные перезаписываются.
     * Статус 200 — успешно.
     */
    @Test
    public void updatePostWithPutTest() {

        int postId = 1;

        // В PUT обязательно передаём ВСЕ поля
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", postId);
        requestBody.put("title", "Обновлённый заголовок");
        requestBody.put("body", "Обновлённое тело поста");
        requestBody.put("userId", 777);

        RestAssured.given()
                .baseUri(URL)
                .contentType(ContentType.JSON)
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

    /**
     * Тест-кейс 14. PATCH — частичное обновление поста
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