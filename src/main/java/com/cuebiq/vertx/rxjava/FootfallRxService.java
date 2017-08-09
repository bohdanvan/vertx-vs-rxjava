package com.cuebiq.vertx.rxjava;

import rx.Single;


/**
 * @author bvanchuhov
 */
public interface FootfallRxService {

    Single<Void> setFootfallByBrand(String brand, long footfall);
    Single<Long> getFootfallByBrand(String brand);
}
