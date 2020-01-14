package com.solarexsoft.solarexeventbus;

import java.lang.reflect.Method;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 15:36/2020-01-14
 *    Desc:
 * </pre>
 */

public class SolarexSubscribeMethod {
    private Method method;
    private SolarexThreadMode threadMode;
    private Class<?> argType;

    public SolarexSubscribeMethod(Method method, SolarexThreadMode threadMode, Class<?> argType) {
        this.method = method;
        this.threadMode = threadMode;
        this.argType = argType;
    }

    public Method getMethod() {
        return method;
    }

    public SolarexThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getArgType() {
        return argType;
    }
}
