package basic.post;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    String content;

    protected Post() {
    }

    public Post(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Long getId() {
        return id;
    }
}