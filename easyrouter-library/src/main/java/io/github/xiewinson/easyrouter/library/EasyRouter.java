package io.github.xiewinson.easyrouter.library;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.github.xiewinson.easyrouter.annotation.Constants;

/**
 * Created by winson on 2017/11/29.
 */

public class EasyRouter {

    private static Map<Class<?>, Constructor<?>> paramInjectorMap = new LinkedHashMap<>();
    private static Map<String, Class<?>> routerMap = new HashMap<>();

    public static void injectIntentParams(Object object) {

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

    public static void init(IRouterTable... tables) {
        for (IRouterTable table : tables) {
            table.insertInto(routerMap);
        }
    }

    public static ActivityRequest.Builder<ActivityRequest.Builder> activity(Context context, String path) {
        Class<?> activityClass = routerMap.get(path);
        return new ActivityRequest.Builder<>(context, activityClass);
    }


    public static FragmentRequest.Builder<Object, FragmentRequest.Builder> fragment(String path) {
        Class<Object> fragmentClass = (Class<Object>) routerMap.get(path);
        return new FragmentRequest.Builder<>(fragmentClass);
    }

}
