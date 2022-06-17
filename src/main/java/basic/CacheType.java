package basic;

public enum CacheType {

    POST("posts", 3 * 60, 10000);

    CacheType(String cacheName, int expiredAfterWrite, int maximumSize) {
        this.cacheName = cacheName;
        this.expiredAfterWrite = expiredAfterWrite;
        this.maximumSize = maximumSize;
    }

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;

    public String getCacheName() {
        return cacheName;
    }

    public int getExpiredAfterWrite() {
        return expiredAfterWrite;
    }

    public int getMaximumSize() {
        return maximumSize;
    }
}