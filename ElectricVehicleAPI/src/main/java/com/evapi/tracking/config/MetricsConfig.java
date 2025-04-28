package com.evapi.tracking.config;

import org.springframework.context.annotation.Configuration;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;

@Configuration
public class MetricsConfig {
	@Bean
    public MeterRegistry meterRegistry() {
        return io.micrometer.core.instrument.Metrics.globalRegistry;
    }
}
