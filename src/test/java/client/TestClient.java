package client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class TestClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static RequestSpecification request() {

        return RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .log()
                .ifValidationFails();
    }
}