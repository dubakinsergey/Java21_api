package client;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class TestClient {

    public static RequestSpecification request() {

        return RestAssured.given()
                .baseUri(TestConfig.getBaseUrl())
                .contentType(ContentType.JSON)
                .log()
                .ifValidationFails();
    }
}