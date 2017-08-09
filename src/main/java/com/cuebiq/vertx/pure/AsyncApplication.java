package com.cuebiq.vertx.pure;

import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;

/**
 * @author bvanchuhov
 */
public class AsyncApplication {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        RedisClient redis = RedisClient.create(vertx);
        FootfallAsyncService cache = new RedisFootfallAsyncService(redis);

        vertx.deployVerticle(new AsyncServer(cache));
    }
}

