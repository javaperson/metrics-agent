package com.happyelements.metrics;

import com.alipay.sofa.rpc.common.RpcConstants;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.context.RpcInternalContext;
import com.alipay.sofa.rpc.context.RpcInvokeContext;
import com.alipay.sofa.rpc.core.request.RequestBase;
import com.alipay.sofa.rpc.message.bolt.BoltSendableResponseCallback;

public class ClientMain {

    public static void main(String[] args) {

        ConsumerConfig<HelloService> consumerConfig = new ConsumerConfig<HelloService>()
                .setInvokeType(RpcConstants.INVOKER_TYPE_ONEWAY)
                .setInterfaceId(HelloService.class.getName())
                .setProtocol("bolt")
                .setDirectUrl("127.0.0.1:12345");

        // 拿到代理类
        HelloService service = consumerConfig.refer();

        // 发起调用
        while (true) {
            service.mark();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}