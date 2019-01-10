package com.happyelements.metrics;

import com.alipay.sofa.rpc.common.RpcConstants;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.invoke.SendableResponseCallback;
import com.alipay.sofa.rpc.core.request.RequestBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.happyelements.metrics.Constants.SOFA_PORT;

public class MetricsServiceFactory {
    private static MetricsService metricsService;
    private static Log logger = LogFactory.getLog(MetricsServiceFactory.class);

    public static void main(String[] args) throws InterruptedException {
        // 发起调用
        while (true) {
            MetricsService metricsService = getMetricsService();
            metricsService.meterMark(MetricsServiceFactory.class.getName(), 1);
            Thread.sleep(1000);
        }
    }

    public static MetricsService getMetricsService() {
        if (metricsService == null) {
            synchronized (MetricsServiceFactory.class) {
                if (metricsService == null) {
                    ConsumerConfig<MetricsService> consumerConfig = new ConsumerConfig<MetricsService>()
                            .setInvokeType(RpcConstants.INVOKER_TYPE_CALLBACK)
                            .setInterfaceId(MetricsService.class.getName())
                            .setProtocol("bolt")
                            .setDirectUrl("127.0.0.1:" + SOFA_PORT)
                            .setOnReturn(new CallBack());
                    metricsService = consumerConfig.refer();
                }
            }
        }
        return metricsService;
    }

    private static class CallBack implements SendableResponseCallback {

        @Override
        public void sendAppResponse(Object appResponse) {
        }

        @Override
        public void sendAppException(Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }

        @Override
        public void sendSofaException(SofaRpcException exception) {
            logger.error(exception.getMessage(), exception);
        }

        @Override
        public void onAppResponse(Object appResponse, String methodName, RequestBase request) {
        }

        @Override
        public void onAppException(Throwable throwable, String methodName, RequestBase request) {
            logger.error(throwable.getMessage() + ", " + request, throwable);
        }

        @Override
        public void onSofaException(SofaRpcException sofaException, String methodName, RequestBase request) {
            logger.error(sofaException.getMessage() + ", " + request, sofaException);
        }
    }
}