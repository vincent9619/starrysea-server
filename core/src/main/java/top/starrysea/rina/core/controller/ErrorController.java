package top.starrysea.rina.core.controller;

import top.starrysea.rina.core.annotation.RinaController;
import top.starrysea.rina.core.annotation.RinaGet;

@RinaController
public class ErrorController {
	@RinaGet("/error/404")
	public String notFound(){
		return "<html lang=\"zh\">" +
				"<head>" +
				"<title>出错了zura</title>" +
				"</head>" +
				"<body>" +
				"<div>" +
				"<div style=\"text-align:center\">" +
				"<h1>404</h1>" +
				"<p>无法找到您请求的资源</p>" +
				"<hr>" +
				"<p>Rina :(</p>" +
				"</div>" +
				"</div>" +
				"</body>" +
				"</html>";
	}
}
