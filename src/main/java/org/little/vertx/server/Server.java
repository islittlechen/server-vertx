package org.little.vertx.server;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.little.vertx.annotation.RouterHandller;
import org.little.vertx.annotation.RouterMapping;
import org.little.vertx.common.CommonPropertyConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

/**
 * <p>
 *     vert.x 应用服务入口类。
 * </p>
 * @author littlechen
 *
 */
public class Server extends AbstractVerticle{
	
	private static final Logger LOG = LoggerFactory.getLogger(Server.class);
	
	private  static ApplicationContext applicationContext;
	 
	private void initRouter(Router router) {
		Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RouterHandller.class);
		controllers.forEach((k,v) ->{
			Method[] methods = v.getClass().getMethods();
			Arrays.asList(methods).forEach( method->{
				RouterMapping[] annotations = method.getAnnotationsByType(RouterMapping.class);
				Arrays.asList(annotations).forEach( requestMapping -> {
					final String path = requestMapping.value();
					router.route(path).handler(e -> {
						e.vertx().executeBlocking(future -> {
							try {
								JsonObject param = null;
								String requestMethod = e.request().method().name();
								if("GET".equals(requestMethod.toUpperCase())) {
									 MultiMap map = e.request().params();
									 param = new JsonObject();
									 Iterator<Entry<String, String>> it = map.iterator();
									 while(it.hasNext()) {
										 Entry<String, String> entry = it.next();
										 param.put(entry.getKey(), entry.getValue());
									 }
								}else {
									 param = e.getBodyAsJson();
								}
								
								JsonObject result = (JsonObject)method.invoke(v, param);
								future.complete(result);
							}catch (Exception e1) {
								LOG.error("request " + path +" error:", e1);
								JsonObject result = new JsonObject();
								result.put("ret", -1);
								
								future.complete(result);
							}
						}, false,res -> {
							e.response().end(res.result().toString());
						});
						
					});
				
				});
			});
		});
	}

	@Override
	public void start() throws Exception {
		 Router router = Router.router(vertx);
		 router.route().handler(CookieHandler.create());
		 router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
		 initRouter(router);
		 vertx.createHttpServer().requestHandler(router::accept).listen(Integer.parseInt(CommonPropertyConfigurer.getInstance().getProperty("vertx.server.http.port","80")));
	}
	
	public static void main(String[] args) throws Exception {
		applicationContext = new ClassPathXmlApplicationContext("classpath*:server-spring.xml");
		Vertx vertx = Vertx.vertx();
		DeploymentOptions option = new DeploymentOptions().setInstances(Integer.parseInt(CommonPropertyConfigurer.getInstance().getProperty("vertx.server.instance", "4")));
		option.setWorkerPoolSize(Integer.parseInt(CommonPropertyConfigurer.getInstance().getProperty("vertx.server.worker.poolsize", option.getInstances()*2+"")));
        vertx.deployVerticle(Server.class, option);
        LOG.trace("Server finished.");
	}
	
}
