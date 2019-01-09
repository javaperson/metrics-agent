package com.happyelements.metrics;

import com.alipay.sofa.rpc.common.RpcConstants;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.core.request.RequestBase;
import com.alipay.sofa.rpc.message.bolt.BoltSendableResponseCallback;

public class ClientMain {

    public static void main(String[] args) {

        ConsumerConfig<HelloService> consumerConfig = new ConsumerConfig<HelloService>()
                .setInvokeType(RpcConstants.INVOKER_TYPE_FUTURE)
                .setOnReturn(new BoltSendableResponseCallback() {
                    @Override
                    public void onAppResponse(Object appResponse, String methodName, RequestBase request) {
                        System.out.println(appResponse);
                    }
                })
                .setInterfaceId(HelloService.class.getName())
                .setProtocol("bolt")
                .setDirectUrl("127.0.0.1:12345");

        // 拿到代理类
        HelloService service = consumerConfig.refer();

        // 发起调用
        while (true) {

            System.out.println(service.sayHello("world"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}