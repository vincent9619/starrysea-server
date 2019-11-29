package top.starrysea.rina.core.controller;

import top.starrysea.rina.core.annotation.RinaController;
import top.starrysea.rina.core.annotation.RinaGet;

@RinaController
public class ErrorController {
	@RinaGet("/error/404")
	public void notFound(){

	}
}
