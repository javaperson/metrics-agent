package com.happyelements.metrics;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;

public class ServerMain {
    public static void main(String[] args) {
        // 指定服务端协议和地址
        ServerConfig serverConfig = new ServerConfig()
                .setProtocol("bolt")
                .setPort(12345)
                .setDaemon(false);
        //　发布一个服务
        ProviderConfig<HelloService> providerConfig = new ProviderConfig<HelloService>()
                .setInterfaceId(HelloService.class.getName())
                .setRef(new HelloServiceImpl())
                .setServer(serverConfig);
        providerConfig.export();
    }
}
