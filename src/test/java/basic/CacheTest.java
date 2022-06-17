package basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import basic.post.DataObject;
import basic.post.Post;
import basic.post.PostService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

@SpringBootTest
class CacheTest {

    @Autowired
    PostService postService;

    @Autowired
    CacheManager cacheManager;

    @Test
    @DisplayName("Caffeine Cache 생성")
    void testCacheInitialization() {
        Cache<String, DataObject> cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();

        String key = "key";
        DataObject dataObject = new DataObject("data");
        cache.put(key, dataObject);
        dataObject = cache.getIfPresent(key);
        assertThat(dataObject).isNotNull();
    }

    @Test
    @DisplayName("cache 적용 시간 비교")
    void testCacheTime() {
        postService.savePost(new Post("aaa"));
        postService.savePost(new Post("bbb"));

        Long startTime = System.currentTimeMillis();

        List<Post> posts = postService.getPosts();
        Long afterNoneCache = System.currentTimeMillis();
        System.out.println("posts = " + posts);
        System.out.printf("TIME: NONE CACHE = %s ms%n", afterNoneCache - startTime);

        posts = postService.getPosts();
        Long afterCache = System.currentTimeMillis();
        System.out.println("posts = " + posts);
        System.out.printf("TIME: CACHE = %s ms%n", afterCache - afterNoneCache);

        assertThat(posts).hasSize(2);
    }

    @Test
    @DisplayName("cache 삭제 확인")
    void testCacheEvict() {
        Post post = postService.savePost(new Post("aaa"));
        postService.getPost(post.getId());
        Post cachingPost = cacheManager.getCache("posts").get(post.getId(), Post.class);
        postService.evictPost(post.getId());

        ValueWrapper afterEvict = cacheManager.getCache("posts").get(post.getId());

        assertAll(
            () -> assertThat(cachingPost).isNotNull(),
            () -> assertThat(cachingPost.getContent()).isEqualTo("aaa"),
            () -> assertThat(afterEvict).isNull()
        );
    }

    @Test
    @DisplayName("cache 수정 확인")
    void testCachePut() {
        Post post = postService.savePost(new Post("aaa"));
        postService.getPost(post.getId());
        postService.updateContent(post.getId(), "bbb");

        Post updatedPost = postService.getPost(post.getId());
        Post cachingPost = cacheManager.getCache("posts").get(post.getId(), Post.class);

        assertAll(
            () -> assertThat(updatedPost.getContent()).isEqualTo("bbb"),
            () -> assertThat(cachingPost).isNotNull()
        );
    }
}