package com.cuebiq.vertx.rxjava;

import io.vertx.rxjava.core.Vertx;

/**
 * @author bvanchuhov
 */
public class RxApplication {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(RxServer.class.getName());
    }
}
