package io.github.the28awg.ploy.experiential.bus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by the28awg on 25.12.16.
 */

public class Bus {
    private static int maxID = 0;
    private final int id = maxID++;
    private Map<Object, Map<String, Method>> subscribers;

    public Bus() {
        subscribers = new ConcurrentHashMap<>();
    }

    private static String argumentTypesToString(Class<?>[] argTypes) {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        if (argTypes != null) {
            for (int i = 0; i < argTypes.length; i++) {
                if (i > 0) {
                    buf.append(", ");
                }
                Class<?> c = argTypes[i];
                if (c != null && !c.isAnnotationPresent(Event.class)) {
                    throw new RuntimeException(c.getName() + " is not an @Event");
                }
                buf.append((c == null) ? "null" : c.getName());
            }
        }
        buf.append(")");
        return buf.toString();
    }

    private static String argumentObjectToString(Object o) {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        if (o == null) {
            buf.append("null");
        } else {
            Class<?> c = o.getClass();
            buf.append((c == null) ? "null" : c.getName());

        }
        buf.append(")");
        return buf.toString();
    }

    public void enable(Object o) {
        boolean found = false;

        for (Map.Entry<Object, Map<String, Method>> entry : subscribers.entrySet()) {
            Object object = entry.getKey();
            if (object == null) {
                subscribers.remove(entry.getKey());
            }
            if (o == object) {
                found = true;
            }
        }
        if (!found) {
            Map<String, Method> tmp = new ConcurrentHashMap<>();
            Class clazz = o.getClass();
            while (clazz != null) {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Subscribe.class)) {
                        Subscribe subscribe = method.getAnnotation(Subscribe.class);
                        String identifier = subscribe.value();
                        if (identifier.isEmpty()) {
                            identifier = method.getName() + "_" + argumentTypesToString(method.getParameterTypes());
                        }
                        tmp.put(identifier, method);

                    }
                }
                clazz = clazz.getSuperclass();
            }
            subscribers.put(o, tmp);
        }
    }

    public void disable(Object o) {
        for (Map.Entry<Object, Map<String, Method>> entry : subscribers.entrySet()) {
            Object object = entry.getKey();
            if (object == null || o == object) {
                subscribers.remove(entry.getKey());
            }
        }
    }

    public void post(Object o) {
        if (o != null) {
            Class<?> c = o.getClass();
            if (c.isAnnotationPresent(Event.class)) {
                Event event = c.getAnnotation(Event.class);
                String identifier = event.value();
                if (identifier.isEmpty()) {
                    identifier = argumentObjectToString(o);
                }

                for (Map.Entry<Object, Map<String, Method>> entry : subscribers.entrySet()) {
                    Object object = entry.getKey();
                    if (object == null) {
                        subscribers.remove(entry.getKey());
                    } else {
                        for (Map.Entry<String, Method> tmp : entry.getValue().entrySet()) {
                            if (tmp.getKey().contains(identifier)) {
                                Method method = tmp.getValue();
                                method.setAccessible(true);
                                try {
                                    method.invoke(object, o);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                } catch (InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            } else {
                throw new RuntimeException(c.getName() + " is not an @Event");
            }
        }
    }
}
