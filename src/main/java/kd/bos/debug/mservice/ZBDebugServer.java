package kd.bos.debug.mservice;

import kd.bos.config.client.util.ConfigUtils;
import kd.bos.db.DB;
import kd.bos.db.SqlLogger;
import kd.bos.service.webserver.JettyServer;

public class ZBDebugServer {

	public static void main(String[] args) throws Exception
	{
		System.setProperty(ConfigUtils.APP_NAME_KEY, "leiflocal");

		//设置集群环境名称和配置服务器地址
		System.setProperty(ConfigUtils.CLUSTER_NAME_KEY, "cosmic");
		System.setProperty(ConfigUtils.CONFIG_URL_KEY, "172.20.110.162:2181");
	    System.setProperty("configAppName", "mservice,web");
	    System.setProperty("webmserviceinone", "true");

		System.setProperty("file.encoding", "utf-8");
	    System.setProperty("xdb.enable", "false");

		System.setProperty("mq.consumer.register", "true");
	    System.setProperty("MONITOR_HTTP_PORT", "9998");
	    System.setProperty("JMX_HTTP_PORT", "9091");
	    System.setProperty("dubbo.protocol.port", "28888");
	    System.setProperty("dubbo.consumer.url", "dubbo://localhost:28888");
	    System.setProperty("dubbo.consumer.url.qing", "dubbo://localhost:30880");
            System.setProperty("dubbo.registry.register", "true");
            //System.setProperty("mq.debug.queue.tag", "whb1133");
            System.setProperty("dubbo.service.lookup.local", "true");
	    System.setProperty("appSplit", "false");
	    System.setProperty("login.type", "STANDALONE");

	    System.setProperty("lightweightdeploy","true");

		System.setProperty("db.sql.out", "true");
		System.setProperty("db.sql.out.withParameter", "true");

		System.setProperty("JETTY_WEB_PORT","8080");
		System.setProperty("JETTY_WEBAPP_PATH", "F:\\CosmicDebugZB/mservice-cosmic/webapp");
		System.setProperty("JETTY_WEBRES_PATH", "F:\\CosmicDebugZB/static-file-service");

		System.setProperty("domain.contextUrl","http://hszc1712-0250:8080/ierp");
	    System.setProperty("domain.tenantCode","cosmic-simple");
	    System.setProperty("tenant.code.type","config");

		System.setProperty("fileserver","http://172.20.110.162:8100/fileserver/");
	    System.setProperty("imageServer.url","http://172.20.110.162:8100/fileserver/");
	    System.setProperty("bos.app.special.deployalone.ids","");
		System.setProperty("mc.server.url","http://172.20.110.162:8090/");
		DB.setSqlLogger(new SqlLogger() {
			@Override
			public void log(String sql, Object... arg1) {
				System.out.println(sql);
			}
		});
		JettyServer.main(null);
	}

}