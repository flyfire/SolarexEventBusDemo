package com.solarexsoft.solarexeventbus;


import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 15:26/2020-01-14
 *    Desc:
 * </pre>
 */

public class SolarexEventBus {
    private static final SolarexEventBus gDefault = new SolarexEventBus();
    private HashMap<Object, List<SolarexSubscribeMethod>> CACHE;
    private Handler mainHandler;
    private ExecutorService executorService;
    private SolarexEventBus() {
        CACHE = new HashMap<>();
        mainHandler  = new Handler(Looper.getMainLooper());
        executorService = Executors.newCachedThreadPool(new ThreadFactory(){
            AtomicInteger atomicInteger = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"solarex-pool-thread-"+atomicInteger.getAndIncrement());
            }
        });
    }
    public static SolarexEventBus getDefault() {
        return gDefault;
    }

    public void register(Object object) {
        List<SolarexSubscribeMethod> subscribeMethods = CACHE.get(object);
        if (subscribeMethods != null) {
            return;
        }
        Class<?> objClz = object.getClass();
        subscribeMethods = new ArrayList<>();
        while (objClz != null) {
            String name = objClz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.") || name.startsWith("androidx.")) {
                break;
            }
            Method[] declaredMethods = objClz.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                SolarexSubscribe annotation = declaredMethod.getAnnotation(SolarexSubscribe.class);
                if (annotation == null) {
                    continue;
                }
                Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException("SolarexEventBus don't support multi arguments method invoke!");
                }
                SolarexSubscribeMethod method = new SolarexSubscribeMethod(declaredMethod, annotation.threadMode(), parameterTypes[0]);
                subscribeMethods.add(method);
            }
            objClz = objClz.getSuperclass();
        }
        if (subscribeMethods.size() == 0) {
            throw new RuntimeException(object + " subscribed without subscribe methods");
        }
        CACHE.put(object, subscribeMethods);
    }

    public void unregister(Object object) {
        List<SolarexSubscribeMethod> subscribeMethods = CACHE.get(object);
        if (subscribeMethods != null) {
            subscribeMethods = CACHE.remove(object);
            subscribeMethods.clear();
        }
    }

    public void post(Object arg) {
        Set<Object> objects = CACHE.keySet();
        Iterator<Object> iterator = objects.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            List<SolarexSubscribeMethod> subscribeMethods = CACHE.get(object);
            for (SolarexSubscribeMethod subscribeMethod : subscribeMethods) {
                if (subscribeMethod.getArgType().isAssignableFrom(arg.getClass())) {
                    invoke(subscribeMethod, object, arg);
                }
            }
        }
    }

    private void invoke(SolarexSubscribeMethod subscribeMethod, final Object object, final Object arg) {
        final Method method = subscribeMethod.getMethod();
        switch (subscribeMethod.getThreadMode()) {
            case POSTING:
                invoke(method, object, arg);
                break;
            case MAIN:
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    invoke(method, object, arg);
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            invoke(method, object, arg);
                        }
                    });
                }
                break;
            case ASYNC:
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            invoke(method, object, arg);
                        }
                    });
                } else {
                    invoke(method, object, arg);
                }
                break;
        }
    }

    private void invoke(Method method, Object object, Object arg) {
        try {
            method.setAccessible(true);
            method.invoke(object, arg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
