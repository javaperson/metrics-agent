package com.happyelements.metrics;

public interface MetricsService {
    String sayHello(String name);

    void meterMark(String name, long n);

    void counterInc(String name);

    void counterInc(String name, long vaule);

    void counterDec(String name);

    void counterDec(String name, long value);

    void histogramUpdate(String name, long value);
}