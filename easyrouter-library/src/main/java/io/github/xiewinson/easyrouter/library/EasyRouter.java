package io.github.xiewinson.easyrouter.library;

import android.app.Fragment;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.github.xiewinson.easyrouter.annotation.Constants;
import io.github.xiewinson.easyrouter.library.inner.ActivityRequestBuilder;
import io.github.xiewinson.easyrouter.library.inner.FragmentRequestBuilder;
import io.github.xiewinson.easyrouter.library.inner.FragmentV4RequestBuilder;

/**
 * Created by winson on 2017/11/29.
 */

public class EasyRouter {

    private static Map<Class<?>, Constructor<?>> paramInjectorMap = new LinkedHashMap<>();
    private static Map<String, Class<?>> routerMap = new HashMap<>();

    public static String findRouteByClass(Class<?> clazz) {
        for (Map.Entry<String, Class<?>> next : routerMap.entrySet()) {
            if (next.getValue() == clazz) {
                return next.getKey();
            }
        }
        return null;
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({Constants.ACTIVITY_PREFIX, Constants.FRAGMENT_PREFIX, Constants.FRAGMENT_V4_PREFIX})
    public @interface RouterPrefix {
    }

    public static Class<?> findClassByRoute(@RouterPrefix String prefix, String route) {
        return routerMap.get(prefix + route);
    }

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

    public static void init(Class<?>... tables) {
        for (Class<?> table : tables) {
            try {
                Object obj = table.newInstance();
                if (obj instanceof IRouterTable) {
                    ((IRouterTable) obj).putRoutes(routerMap);
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static ActivityRequestBuilder activity(@NonNull String path) {
        Class<?> activityClass = findClassByRoute(Constants.ACTIVITY_PREFIX, path);
        return new ActivityRequestBuilder(activityClass);
    }

    public static ActivityRequestBuilder activity(@NonNull Uri uri) {
        return new ActivityRequestBuilder().withData(uri);
    }

    @SuppressWarnings("unchecked")
    public static FragmentRequestBuilder fragment(@NonNull String path) {
        Class<?> fragmentClass = findClassByRoute(Constants.FRAGMENT_PREFIX, path);
        return new FragmentRequestBuilder(fragmentClass == null ? null : (Class<Fragment>) fragmentClass);
    }

    @SuppressWarnings("unchecked")
    public static FragmentV4RequestBuilder fragmentV4(@NonNull String path) {
        Class<?> fragmentClass = findClassByRoute(Constants.FRAGMENT_V4_PREFIX, path);
        return new FragmentV4RequestBuilder(fragmentClass == null ? null : (Class<android.support.v4.app.Fragment>) fragmentClass);
    }


}
