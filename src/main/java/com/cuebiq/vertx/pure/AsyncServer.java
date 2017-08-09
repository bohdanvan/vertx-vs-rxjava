package com.cuebiq.vertx.pure;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.net.HttpURLConnection.HTTP_CREATED;

/**
 * @author bvanchuhov
 */
public class AsyncServer extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(AsyncServer.class);

    private final FootfallAsyncService footfallAsyncService;

    public AsyncServer(FootfallAsyncService footfallAsyncService) {
        this.footfallAsyncService = footfallAsyncService;
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/:brand/footfall").handler(this::setFootfallForBrand);
        router.get("/:brand/footfall").handler(this::getFootfallByBrand);
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private void setFootfallForBrand(RoutingContext context) {
        String brand = context.request().getParam("brand");
        JsonObject body = context.getBodyAsJson();
        Long footfall = body.getLong("footfall");
        footfallAsyncService.setFootfallByBrand(brand, footfall, asyncResult -> {
            if (asyncResult.succeeded()) {
                logger.info("Successful setting: " + brand + " -> " + footfall);
                context.response().setStatusCode(HTTP_CREATED).end();
            } else {
                logger.warn("Can't set footfall for brand " + brand, asyncResult.cause());
                context.fail(asyncResult.cause());
            }
        });
    }

    private void getFootfallByBrand(RoutingContext context) {
        String brand = context.request().getParam("brand");
        footfallAsyncService.getFootfallByBrand(brand, asyncResult -> {
            if (asyncResult.succeeded()) {
                Long footfall = asyncResult.result();
                logger.info(brand + " -> " + footfall);
                context.response().end(new JsonObject().put("footfall", footfall).toString());
            } else {
//                context.response().setStatusCode(404).end();
                context.fail(asyncResult.cause());
            }
        });
    }
}
