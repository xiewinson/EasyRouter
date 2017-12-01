package io.github.xiewinson.easyrouter.library;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

import io.github.xiewinson.easyrouter.annotation.Constants;

/**
 * Created by winson on 2017/11/29.
 */

public abstract class BaseEasyRouterManager {
    private LinkedHashMap<Class, Constructor<?>> paramInjectorMap = new LinkedHashMap<>();

    protected void injectIntentParamsInternal(Object object) {
        Class<?> key = object.getClass();
        Constructor<?> constructor = paramInjectorMap.get(key);
        if (constructor == null) {
            try {
                try {
                    constructor = Class.forName(key.getName() + Constants._INTENT_PARAM_INJECTOR).getConstructor(key);
                    if (constructor != null) {
                        paramInjectorMap.put(key, constructor);
                    }
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
