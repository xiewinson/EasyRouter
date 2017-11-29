package io.github.xiewinson.easyrouter.annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

/**
 * Created by winson on 2017/11/28.
 */

public abstract class BaseRouter {

    private static LinkedHashMap<String, Constructor<?>> map = new LinkedHashMap<>();

    public static void inject(Object object) {
        String name = object.getClass().getName();
        Constructor<?> constructor = map.get(name);
        if (constructor == null) {
            try {
                try {
                    constructor = Class.forName(name + "_" + "IntentBinding").getConstructor(object.getClass());
                    map.put(name, constructor);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (constructor != null) {
            try {
                constructor.newInstance(object);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

}
