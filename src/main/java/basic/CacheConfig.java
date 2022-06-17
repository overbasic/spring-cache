package basic;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public Caffeine caffeine() {
        return Caffeine
            .newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(60, TimeUnit.MINUTES);
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
            .map(cache ->
                new CaffeineCache(cache.getCacheName(), Caffeine.newBuilder().recordStats()
                    .expireAfterWrite(cache.getExpiredAfterWrite(), TimeUnit.SECONDS)
                    .maximumSize(cache.getMaximumSize())
                    .build()
                )
            )
            .toList();

        cacheManager.setCaches(caches);

        return cacheManager;
    }
}