package uz.example.firebaseauth_java.model;

import androidx.annotation.Nullable;

public class Post {
    String id;
    String title;
    String body;
    String image;

    public Post() {
    }

    public Post(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Post(String id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }
    public Post(String id, String title, String body,String image) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
