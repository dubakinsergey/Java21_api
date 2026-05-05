package factory;

import models.PostRequest;

public class PostFactory {

    public static PostRequest validPost() {

        return PostRequest.builder()
                .title("Обычный заголовок")
                .body("Обычное тело поста")
                .userId(1)
                .build();
    }

    public static PostRequest postWithEmptyTitle() {

        return PostRequest.builder()
                .title("")
                .body("Тело для пустого заголовка")
                .userId(2)
                .build();
    }

    public static PostRequest postWithLongTitle() {

        return PostRequest.builder()
                .title("a".repeat(1000))
                .body("Тело с длинным заголовком")
                .userId(3)
                .build();
    }
}