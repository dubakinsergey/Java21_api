package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // ← не включает null поля в JSON
public class PatchRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("userId")
    private Integer userId;

    // ========== BUILDER ==========
    public static PatchRequestBuilder builder() {
        return new PatchRequestBuilder();
    }

    public static class PatchRequestBuilder {

        private String title;
        private String body;
        private Integer userId;

        public PatchRequestBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PatchRequestBuilder body(String body) {
            this.body = body;
            return this;
        }

        public PatchRequestBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public PatchRequest build() {

            return new PatchRequest(this.title, this.body, this.userId);
        }
    }
}