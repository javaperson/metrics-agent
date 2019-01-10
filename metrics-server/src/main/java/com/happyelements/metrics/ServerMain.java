package com.happyelements.metrics;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

import static com.happyelements.metrics.Constants.SOFA_PORT;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        MetricRegistry metricRegistry = new MetricRegistry();

        ServerConfig serverConfig = new ServerConfig()
                .setProtocol("bolt")
                .setPort(SOFA_PORT)
                .setDaemon(false);
        ProviderConfig<MetricsService> providerConfig = new ProviderConfig<MetricsService>()
                .setInterfaceId(MetricsService.class.getName())
                .setRef(new MetricsServiceImpl(metricRegistry))
                .setServer(serverConfig);
        providerConfig.export();

        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(5, TimeUnit.SECONDS);

        new Server(metricRegistry).run("server");
    }
}
