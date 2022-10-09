package org.example;

import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Component;

@Component
public class Logger {
    @Timed("log.info")
    public void info(String s) {
        System.out.println("Info: " + s);
    }
}
