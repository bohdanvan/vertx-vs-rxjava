package com.cuebiq.vertx.rxjava;

import io.vertx.rxjava.redis.RedisClient;
import rx.Single;

/**
 * @author bvanchuhov
 */
public class RedisFootfallRxService implements FootfallRxService {

    private final RedisClient redisClient;

    public RedisFootfallRxService(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public Single<Void> setFootfallByBrand(String brand, long footfall) {
        return redisClient.rxSet(footfallKey(brand), Long.toString(footfall));
    }

    @Override
    public Single<Long> getFootfallByBrand(String brand) {
        return redisClient.rxGet(footfallKey(brand))
                .map(Long::valueOf);
    }

    private String footfallKey(String brand) {
        return brand + ":footfall";
    }
}
