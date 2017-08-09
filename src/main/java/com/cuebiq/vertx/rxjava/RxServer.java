package com.cuebiq.vertx.rxjava;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import io.vertx.rxjava.redis.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.net.HttpURLConnection.HTTP_CREATED;

/**
 * @author bvanchuhov
 */
public class RxServer extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(RxServer.class);

    private FootfallRxService footfallRxService;

    public RxServer(FootfallRxService footfallRxService) {
        this.footfallRxService = footfallRxService;
    }

    public RxServer() {
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
        getFootfallRxService().setFootfallByBrand(brand, footfall)
                .subscribe(
                        nothing -> {
                            logger.info("Successful setting: " + brand + " -> " + footfall);
                            context.response().setStatusCode(HTTP_CREATED).end();
                        },
                        e -> {
                            logger.warn("Can't set footfall for brand " + brand, e);
                            context.fail(e);
                        }
                );
    }

    private void getFootfallByBrand(RoutingContext context) {
        String brand = context.request().getParam("brand");
        getFootfallRxService().getFootfallByBrand(brand)
                .subscribe(
                        footfall -> {
                            logger.info(brand + " -> " + footfall);
                            context.response().end(new JsonObject().put("footfall", footfall).toString());
                        },
                        e -> {
                            context.fail(e);
                        }
                );
    }

    public FootfallRxService getFootfallRxService() {
        if (footfallRxService == null) {
            footfallRxService = new RedisFootfallRxService(RedisClient.create(vertx));
        }
        return footfallRxService;
    }
}
