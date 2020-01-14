package com.solarexsoft.solarexeventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 15:32/2020-01-14
 *    Desc:
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SolarexSubscribe {
    SolarexThreadMode threadMode() default SolarexThreadMode.POSTING;
}
