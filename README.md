# server-vertx
-server-vertx 是一个集成了vert.x，spring等开源组件的异步WEB服务开发框架，通过自定义注解@RouterHandller，@RouterMapping实现了类似spring mvc的开发模式。
 -项目实现简单易懂，且继承vert.x自身的异步特性。开发人员可以通过此框架，可快速搭建一个高性能的WEB服务。有能力的开发人员可在此基础上嵌入vert.x 的其他特性。 vert.x 实例 github 地址 https://github.com/vert-x3/vertx-examples
 -

 -开发实例：
 
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
