package basic.post;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Cacheable(cacheNames = "posts")
    public List<Post> getPosts() {
        List<Post> all = postRepository.findAll();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return all;
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Cacheable(cacheNames = "posts", key = "#id")
    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow();
    }

    @CacheEvict(value = "posts", key = "#id")
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @CacheEvict(value = "posts", key = "#id")
    public void evictPost(Long id) {

    }

    @Transactional
    @CachePut(value = "posts", key = "#id")
    public Post updateContent(Long id, String content) {
        Post post = postRepository.findById(id).orElseThrow();
        post.updateContent(content);
        return post;
    }

}