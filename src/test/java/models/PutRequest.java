package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutRequest {

    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("userId")
    private int userId;

    // ========== BUILDER ==========
    public static PutRequestBuilder builder() {
        return new PutRequestBuilder();
    }

    public static class PutRequestBuilder {
        private int id;
        private String title;
        private String body;
        private int userId;

        public PutRequestBuilder id(int id) {
            this.id = id;
            return this;
        }

        public PutRequestBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PutRequestBuilder body(String body) {
            this.body = body;
            return this;
        }

        public PutRequestBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public PutRequest build() {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title не может быть пустым");
            }
            if (userId < 0) {
                throw new IllegalArgumentException("UserId не может быть отрицательным");
            }
            if (id <= 0) {
                throw new IllegalArgumentException("Id должен быть больше 0");
            }
            return new PutRequest(this.id, this.title, this.body, this.userId);
        }
    }
}