package com.cuebiq.vertx.pure;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author bvanchuhov
 */
public interface FootfallAsyncService {

    void setFootfallByBrand(String brand, long footfall, Handler<AsyncResult<Void>> handler);
    void getFootfallByBrand(String brand, Handler<AsyncResult<Long>> handler);
}
