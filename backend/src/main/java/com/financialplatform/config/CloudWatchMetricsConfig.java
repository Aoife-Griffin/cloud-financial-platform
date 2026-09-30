package com.financialplatform.config;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics; // Added
import io.micrometer.core.instrument.binder.system.ProcessorMetrics; // Added
import io.micrometer.core.instrument.binder.system.UptimeMetrics; // Added
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.time.Duration;

@Configuration
public class CloudWatchMetricsConfig {

    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient.builder()
                .region(Region.EU_NORTH_1)
            .build(); 
    }

    @Bean
    public CloudWatchConfig cloudWatchConfig() {
        return new CloudWatchConfig() {
            @Override
            public String get(String key) {
                return null; 
            }
            @Override
            public String namespace() {
                return "FinancialPlatform";
            }
            @Override
            public Duration step() {
                return Duration.ofMinutes(1);
            }
        };
    }

    @Bean
    public MeterRegistry meterRegistry(CloudWatchConfig config, CloudWatchAsyncClient client) {
        CloudWatchMeterRegistry registry = new CloudWatchMeterRegistry(config, Clock.SYSTEM, client);
        
        new JvmMemoryMetrics().bindTo(registry);
        new UptimeMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        
        System.out.println("====== CUSTOM CLOUDWATCH REGISTRY + JVM BINDERS SUCCESSFULLY INITIALIZED ======");
        return registry;
    }
}
