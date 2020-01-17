package top.starrysea.rina.init;

import lombok.Data;

@Data
public class ServerConfig {
	private int port;
	private String basePackage;
	private long waitTime;
	private String jdbcUrl;
	private String jdbcUsername;
	private String jdbcPassword;
	private String jdbcDriver;
}
