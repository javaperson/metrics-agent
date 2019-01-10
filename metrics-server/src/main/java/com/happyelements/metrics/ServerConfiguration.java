package com.happyelements.metrics;

import io.dropwizard.Configuration;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.server.SimpleServerFactory;

public class ServerConfiguration extends Configuration {
    public ServerConfiguration() {
        super();
        setServerFactory(new SimpleServerFactory());
    }
}
