package org.little.vertx.biz.handler;

import javax.annotation.Resource;

import org.little.vertx.annotation.RouterHandller;
import org.little.vertx.annotation.RouterMapping;
import org.little.vertx.biz.service.UserService;

import io.vertx.core.json.JsonObject;

@RouterHandller
public class UserHandler{

	@Resource
	private UserService userService;

	@RouterMapping("/userLogin")
	public JsonObject userLogin(JsonObject param) {
		 
		return userService.echo(param);
	}
 
	@RouterMapping("/userLogout")
	public JsonObject userLogout(JsonObject param) {
		return userService.echo(param);
	}
	
}
