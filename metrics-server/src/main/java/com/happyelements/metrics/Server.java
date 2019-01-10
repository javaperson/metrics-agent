package com.happyelements.metrics;

import com.codahale.metrics.MetricRegistry;
import com.happyelements.metrics.dropwizard.PrometheusBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Server extends Application<ServerConfiguration> {
    public final MetricRegistry metricRegistry;

    public Server(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public void initialize(Bootstrap bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addBundle(new PrometheusBundle().setMetricRegistry(metricRegistry));
    }

    @Override
    public void run(ServerConfiguration configuration, Environment environment) throws Exception {

    }
}
