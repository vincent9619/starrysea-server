package top.starrysea.rina.core.controller;

import top.starrysea.rina.core.annotation.RinaController;
import top.starrysea.rina.core.annotation.RinaGet;
import top.starrysea.rina.core.connection.HttpResponse;
import top.starrysea.rina.core.connection.entity.enums.HttpContentType;
import top.starrysea.rina.core.connection.entity.enums.HttpStatus;

@RinaController
public class ErrorController {
	@RinaGet("/error/404")
	public HttpResponse notFound() {
		HttpResponse response = new HttpResponse();
		response.setHttpStatus(HttpStatus.NOT_FOUND);
		response.setHttpContentType(HttpContentType.TEXT_HTML);
		String s = """
				            <!DOCTYPE html>
				            <html lang="zh">
				            <head>
				                <title>出错了zura</title>
				                <meta charset="utf-8" />
				                <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
				                <meta name="viewport" content="width=device-width, initial-scale=1" />
				                <style type="text/css">
				                    body {
				                        background-color: #f0f0f2;
				                        margin: 0;
				                        padding: 0;
				                    }
				                    div {
				                        width: 600px;
				                        margin: 5em auto;
				                        padding: 2em;
				                        background-color: #fdfdff;
				                        border-radius: 0.5em;
				                        box-shadow: 2px 3px 7px 2px rgba(0, 0, 0, 0.02);
				                    }
				                    @media (max-width: 700px) {
				                        div {
				                            margin: 0 auto;
				                            width: auto;
				                        }
				                    }
				                </style>
				            </head>
				            <body>
				                <div>
				                    <h1>404</h1>
				                    <p>无法找到您请求的资源</p>
				                    <hr>
				                    <p>Rina :(</p>
				                </div>
				            </body>
				            </html>
				""";
		response.setResponseContent(s);
		return response;
	}
}
