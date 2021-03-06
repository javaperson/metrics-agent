package com.happyelements.metrics.dropwizard;

import com.codahale.metrics.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class DropwizardMetricsExporter {

    private static final Logger LOG = LoggerFactory.getLogger(DropwizardMetricsExporter.class);

    private final PrometheusTextWriter writer;

    public DropwizardMetricsExporter(PrometheusTextWriter writer) {
        this.writer = writer;
    }

    private static String getHelpMessage(String metricName, Metric metric) {
        return String.format("Generated from Dropwizard metric import (metric=%s, type=%s)", metricName, metric.getClass().getName());
    }

    static String sanitizeMetricName(String dropwizardName) {
        return dropwizardName.replaceAll("[^a-zA-Z0-9:_]", "_");
    }

    public void writeGauge(String name, Gauge<?> gauge) throws IOException {
        final String sanitizedName = sanitizeMetricName(name);
        writer.writeHelp(sanitizedName, getHelpMessage(name, gauge));
        writer.writeType(sanitizedName, MetricType.GAUGE);

        Object obj = gauge.getValue();
        double value;
        if (obj instanceof Number) {
            value = ((Number) obj).doubleValue();
        } else if (obj instanceof Boolean) {
            value = ((Boolean) obj) ? 1 : 0;
        } else {
            LOG.warn("Invalid type for Gauge {}: {}", name, obj.getClass().getName());
            return;
        }

        writer.writeSample(sanitizedName, emptyMap(), value);
    }

    /**
     * Export counter as Prometheus <a href="https://prometheus.io/docs/concepts/metric_types/#gauge">Gauge</a>.
     */
    public void writeCounter(String dropwizardName, Counter counter) throws IOException {
        String name = sanitizeMetricName(dropwizardName);
        writer.writeHelp(name, getHelpMessage(dropwizardName, counter));
        writer.writeType(name, MetricType.GAUGE);
        writer.writeSample(name, emptyMap(), counter.getCount());
    }

    /**
     * Export a histogramUpdate snapshot as a prometheus SUMMARY.
     *
     * @param dropwizardName metric name.
     * @throws IOException
     */
    public void writeHistogram(String dropwizardName, Histogram histogram) throws IOException {
        writeSnapshotAndCount(dropwizardName, histogram.getSnapshot(), histogram.getCount(), 1.0, MetricType.SUMMARY, getHelpMessage(dropwizardName, histogram));
    }

    private void writeSnapshotAndCount(String dropwizardName, Snapshot snapshot, long count, double factor, MetricType type, String helpMessage) throws IOException {
        String name = sanitizeMetricName(dropwizardName);
        writer.writeHelp(name, helpMessage);
        writer.writeType(name, type);
        writer.writeSample(name, mapOf("quantile", "0.5"), snapshot.getMedian() * factor);
        writer.writeSample(name, mapOf("quantile", "0.75"), snapshot.get75thPercentile() * factor);
        writer.writeSample(name, mapOf("quantile", "0.95"), snapshot.get95thPercentile() * factor);
        writer.writeSample(name, mapOf("quantile", "0.98"), snapshot.get98thPercentile() * factor);
        writer.writeSample(name, mapOf("quantile", "0.99"), snapshot.get99thPercentile() * factor);
        writer.writeSample(name, mapOf("quantile", "0.999"), snapshot.get999thPercentile() * factor);
        writer.writeSample(name + "_min", emptyMap(), snapshot.getMin());
        writer.writeSample(name + "_max", emptyMap(), snapshot.getMax());
        writer.writeSample(name + "_median", emptyMap(), snapshot.getMedian());
        writer.writeSample(name + "_mean", emptyMap(), snapshot.getMean());
        writer.writeSample(name + "_stddev", emptyMap(), snapshot.getStdDev());
        writer.writeSample(name + "_count", emptyMap(), count);
    }

    private void writeMetered(String dropwizardName, Metered metered) throws IOException {
        String name = sanitizeMetricName(dropwizardName);
        writer.writeSample(name, mapOf("rate", "m1"), metered.getOneMinuteRate());
        writer.writeSample(name, mapOf("rate", "m5"), metered.getFiveMinuteRate());
        writer.writeSample(name, mapOf("rate", "m15"), metered.getFifteenMinuteRate());
        writer.writeSample(name, mapOf("rate", "mean"), metered.getMeanRate());
    }

    private Map<String, String> mapOf(String key, String value) {
        HashMap<String, String> result = new HashMap<>();
        result.put(key, value);
        return result;
    }

    private Map<String, String> emptyMap() {
        return Collections.emptyMap();
    }

    public void writeTimer(String dropwizardName, Timer timer) throws IOException {
        writeSnapshotAndCount(dropwizardName, timer.getSnapshot(), timer.getCount(), 1.0D / TimeUnit.SECONDS.toNanos(1L), MetricType.SUMMARY, getHelpMessage(dropwizardName, timer));
        writeMetered(dropwizardName, timer);
    }

    public void writeMeter(String dropwizardName, Meter meter) throws IOException {
        String name = sanitizeMetricName(dropwizardName) + "_total";

        writer.writeHelp(name, getHelpMessage(dropwizardName, meter));
        writer.writeType(name, MetricType.COUNTER);
        writer.writeSample(name, emptyMap(), meter.getCount());

        writeMetered(dropwizardName, meter);
    }

}
