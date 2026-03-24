package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("userId")
    private int userId;

    public PostRequest() {
    }

    public PostRequest(String title, String body, int userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return
                "PostRequest{" +
                        "title = '" + title + '\'' +
                        ",body = '" + body + '\'' +
                        ",userId = '" + userId + '\'' +
                        "}";
    }
}