package com.solarexsoft.solarexeventbus;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 15:32/2020-01-14
 *    Desc:
 * </pre>
 */

public @interface SolarexSubscribe {
    SolarexThreadMode threadMode() default SolarexThreadMode.POSTING;
}
