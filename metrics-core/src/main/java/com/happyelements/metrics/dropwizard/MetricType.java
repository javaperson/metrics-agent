package com.happyelements.metrics.dropwizard;

enum MetricType {
    COUNTER     ("counter"),
    GAUGE       ("gauge"),
    SUMMARY     ("summary"),
    HISTOGRAM   ("histogramUpdate"),
    UNTYPED     ("untyped");

    private final String text;

    MetricType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
