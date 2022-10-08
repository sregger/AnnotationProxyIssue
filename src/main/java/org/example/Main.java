package org.example;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import java.util.Arrays;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(Main.class, args);
    }
    private final Logger log;

    public Main(Logger log) {
        this.log = log;
    }

    @EventListener(ApplicationReadyEvent.class)
    void init() {
        log.info("hello world, I have just started up");

        Metrics.globalRegistry.counter("objects.instance").increment(1.0);

        var timer = Metrics.globalRegistry.find("log.info").timer();
        System.out.println("Number of log.info(..) calls: " + (timer != null ? timer.count() : null));

        var counter = Metrics.globalRegistry.find("objects.instance").counter();
        System.out.println("Counter: " + (counter != null ? counter.count() : null));

        Metrics.globalRegistry.forEachMeter(m->System.out.println(m.getId().getName()));
    }

    @Bean
    TimedAspect timedAspect() {
        // Register an aspect against the global CompositeMeterRegistry
        // Without this there is no support for the @Timed annotation
        return new TimedAspect(Metrics.globalRegistry);
    }

    @Bean
    SimpleMeterRegistry simpleMeterRegistry() {
        var simpleMeterRegistry = new SimpleMeterRegistry();
        // Register in the global CompositeMeterRegistry
        // Without it there is no store for metrics
        Metrics.globalRegistry.add(simpleMeterRegistry);
        return simpleMeterRegistry;
    }

    @Bean
    CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            log.info("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                log.info(beanName);
            }

        };
    }
}