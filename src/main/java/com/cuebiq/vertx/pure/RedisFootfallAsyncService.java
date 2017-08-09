package com.cuebiq.vertx.pure;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.redis.RedisClient;

/**
 * @author bvanchuhov
 */
public class RedisFootfallAsyncService implements FootfallAsyncService {

    private final RedisClient redisClient;

    public RedisFootfallAsyncService(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public void setFootfallByBrand(String brand, long footfall, Handler<AsyncResult<Void>> handler) {
        redisClient.set(footfallKey(brand), Long.toString(footfall), handler);
    }

    @Override
    public void getFootfallByBrand(String brand, Handler<AsyncResult<Long>> handler) {
        redisClient.get(footfallKey(brand), asyncResult -> {
            if (asyncResult.succeeded()) {
                String res = asyncResult.result();
                if (res != null) {
                    Long footfall = Long.valueOf(asyncResult.result());
                    handler.handle(Future.succeededFuture(footfall));
                } else {
                    handler.handle(Future.failedFuture("Brand not found"));
                }
            } else {
                handler.handle(Future.failedFuture(asyncResult.cause()));
            }
        });
    }

    private String footfallKey(String brand) {
        return brand + ":footfall";
    }
}
