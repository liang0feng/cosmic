package kd.bos.debug.mservice;

import kd.bos.config.client.util.ConfigUtils;
import kd.bos.service.webserver.JettyServer;

public class DebugServer9701 {

    public static void main(String[] args) throws Exception
    {
        System.setProperty(ConfigUtils.APP_NAME_KEY, "leif_cosmicsrv");//应用标识，每个端都改成自己的名字命名

        //设置集群环境名称和配置服务器地址
        System.setProperty(ConfigUtils.CLUSTER_NAME_KEY, "ierp");//集群编码，同服务器中mc配置一致
        System.setProperty(ConfigUtils.CONFIG_URL_KEY, "172.20.182.97:2181");//远程zk路径
        System.setProperty("configAppName", "mservice,web");
        System.setProperty("webmserviceinone", "true");

        System.setProperty("xdb.enable", "false");//xdb需要垂直分库时使用

//        System.setProperty("mqConfigFiles.config", "jimmymqconfig.xml");//mq配置文件
//        System.setProperty("dubbo.registry.register", "true");
//        System.setProperty("dubbo.registry.address", "172.20.182.97:2181");
        System.setProperty("mq.consumer.register", "true");
        System.setProperty("mq.debug.queue.tag", "jimmy_unique_queue");//改成自己的队列名，全集群唯一性，对工作流、定时任务有影响

        System.setProperty("MONITOR_HTTP_PORT", "9998");
        System.setProperty("JMX_HTTP_PORT", "9091");
        System.setProperty("dubbo.protocol.port", "28888");
        System.setProperty("dubbo.consumer.url", "dubbo://172.20.110.162:28888");
        //  System.setProperty("dubbo.consumer.url.qing", "dubbo://localhost:30880");//

        System.setProperty("springboot.enable", "false");

        System.setProperty("dubbo.service.lookup.local", "false");
        System.setProperty("appSplit", "false");

        System.setProperty("lightweightdeploy","false");//
        System.setProperty("db.sql.out", "true");//打印日志
        System.setProperty("script.debug.enable", "true");//kde脚本调度
        System.setProperty("login.type", "STANDALONE");//登录方式

        System.setProperty("JETTY_WEBAPP_PATH", "F:/CosmicDebugX/mservice/webapp");//本地Jettywebapp路径
        System.setProperty("JETTY_WEBRES_PATH", "F:/CosmicDebugX/static-file-service/webapp");//本地静态资源路径
        System.setProperty("actionConfigFiles.config", "F:/CosmicDebugX/mservice/conf/qing-actionconfig.xml");//轻分析action本地配置文件路径
        System.setProperty("ActionConfigFile", "F:/CosmicDebugX/mservice/conf/actionconfig.xml");//微服务action本地配置文件路径

        System.setProperty("domain.contextUrl","http://hszc1712-0250:8080/ierp");	//影响登录路径入口地址
        System.setProperty("domain.tenantCode","ierp");//租户编码，同服务器中mc配置一致
        System.setProperty("tenant.code.type","config");

        System.setProperty("mc.server.url","http://172.20.182.97:8090/mc/");//远程mc路径
        System.setProperty(
                "org.eclipse.jetty.server.Request.maxFormContentSize",
                "9000000");
        JettyServer.main(null);
    }

}