package basic;

import static org.assertj.core.api.Assertions.assertThat;

import basic.post.DataObject;
import basic.post.Post;
import basic.post.PostService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CacheTest {

    @Autowired
    PostService postService;

    @Test
    @DisplayName("cache 초기화")
    void testCacheInitialization() {
        Cache<String, DataObject> cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();

        String key = "A";
        DataObject dataObject = cache.getIfPresent(key);

        assertThat(dataObject).isNull();

        DataObject dataObject1 = new DataObject("data");
        cache.put(key, dataObject1);
        dataObject1 = cache.getIfPresent(key);
        assertThat(dataObject1).isNotNull();
    }

    @Test
    @DisplayName("cache 초기화")
    void testCacheTime() {
        postService.savePost(new Post("aaa"));
        postService.savePost(new Post("bbb"));

        System.out.println("start = " + System.currentTimeMillis());

        List<Post> posts = postService.getPosts();
        System.out.println("posts = " + posts);
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());

        posts = postService.getPosts();
        System.out.println("posts = " + posts);
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());

        posts = postService.getPosts();
        System.out.println("posts = " + posts);
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
    }
}