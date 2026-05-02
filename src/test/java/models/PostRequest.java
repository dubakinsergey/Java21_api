package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("userId")
    private int userId;

    // ========== BUILDER ==========
    public static PostRequestBuilder builder() {
        return new PostRequestBuilder();
    }

    public static class PostRequestBuilder {
        private String title;
        private String body;
        private int userId;

        public PostRequestBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PostRequestBuilder body(String body) {
            this.body = body;
            return this;
        }

        public PostRequestBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public PostRequest build() {

            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title не может быть пустым");
            }

            if (userId < 0) {
                throw new IllegalArgumentException("UserId не может быть отрицательным");
            }

            return new PostRequest(this.title, this.body, this.userId);
        }
    }
}