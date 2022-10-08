package org.example;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.TimedAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Logger {

//    @Autowired
//    TimedAspect aspect;
    @Timed("log.info")
    public void info(String s) {
        System.out.println("Info: " + s);
    }
}
