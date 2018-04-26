package org.little.vertx.biz.service;

import org.springframework.stereotype.Service;

import io.vertx.core.json.JsonObject;

@Service
public class UserService {

	public JsonObject echo(JsonObject req) {
		return req;
	}
}
