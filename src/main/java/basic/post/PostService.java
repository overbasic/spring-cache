package basic.post;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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

    public void savePost(Post post) {
        postRepository.save(post);
    }
}