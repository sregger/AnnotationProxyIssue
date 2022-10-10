package org.example;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.event.EventListener;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(Main.class, args);
    }
//    private final Logger log;
//
//    public Main(Logger log) {
//        this.log = log;
//    }

    @EventListener(ApplicationReadyEvent.class)
    void init() {
        logger().info("hello world, I have just started up");
        System.out.println("");

        var timer = Metrics.globalRegistry.find("log.info").timer();
        System.out.println("Number of logger().info(..) calls: " + (timer != null ? timer.count() : null));

        Metrics.globalRegistry.counter("objects.instance").increment(1.0);
        var counter = Metrics.globalRegistry.find("objects.instance").counter();
        System.out.println("Counter: " + (counter != null ? counter.count() : null));

        System.out.println("\nDefined Meters:");
        Metrics.globalRegistry.forEachMeter(m->System.out.println(m.getId().getName()));
    }

//    @Autowired Logger log;
//
//    // Causes "The bean 'logger' could not be injected because it is a JDK dynamic proxy" and "The bean is of type 'jdk.proxy2.$Proxy45' and implements:"
//    @Bean
//    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    // Define a bean rather than a @Component to work around https://www.baeldung.com/spring-not-eligible-for-auto-proxying
    @Bean
    Logger logger() {
        return new Logger();
    }

    // Set by csb, and causes the `@Timed` metric to be null
    @Bean
    public AnnotationAwareAspectJAutoProxyCreator aspectJProxyCreator() {
        var a = new AnnotationAwareAspectJAutoProxyCreator();
        // Fixes the problem
        // a.setProxyTargetClass(true);
        return a;
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

            logger().info("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                logger().info(beanName);
            }

        };
    }
}