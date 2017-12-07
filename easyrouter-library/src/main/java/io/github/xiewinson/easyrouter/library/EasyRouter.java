package io.github.xiewinson.easyrouter.library;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.xiewinson.easyrouter.annotation.Constants;
import io.github.xiewinson.easyrouter.library.builder.ActivityRequestBuilder;
import io.github.xiewinson.easyrouter.library.builder.FragmentRequestBuilder;
import io.github.xiewinson.easyrouter.library.builder.FragmentV4RequestBuilder;
import io.github.xiewinson.easyrouter.library.builder.ServiceRequestBuilder;
import io.github.xiewinson.easyrouter.library.inner.IRouterTable;
import io.github.xiewinson.easyrouter.library.request.ActivityRequest;
import io.github.xiewinson.easyrouter.library.request.IntentRequest;

/**
 * Created by winson on 2017/11/29.
 */

public class EasyRouter {

    /**
     * 参数注解
     */
    private static Map<Class<?>, Constructor<?>> paramInjectorMap = new LinkedHashMap<>();

    private static Map<String, Class<?>> routerMap = new LinkedHashMap<>();
    private static Map<Class<?>, String> classMap = new LinkedHashMap<>();

    private static Map<Class<?>, List<Class<? extends Interceptor>>> interceptorRelationsMap = new LinkedHashMap<>();

    /**
     * 通过class找到对应route
     * @param clazz
     * @return
     */
    public static String findRouteByClass(Class<?> clazz) {
        if (classMap.size() != routerMap.size()) {
            for (Map.Entry<String, Class<?>> item : routerMap.entrySet()) {
                classMap.put(item.getValue(), item.getKey());
            }
        }
        return classMap.get(clazz);
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({Constants.ACTIVITY_PREFIX, Constants.FRAGMENT_PREFIX, Constants.FRAGMENT_V4_PREFIX, Constants.SERVICE_PREFIX})
    public @interface RouterPrefix {
    }

    /**
     * 通过route找到对应的类
     * @param prefix
     * @param route
     * @return
     */
    public static Class<?> findClassByRoute(@RouterPrefix String prefix, String route) {
        return routerMap.get(prefix + route);
    }


    public static List<Class<? extends Interceptor>> findInterceptorsByClass(Class<?> clazz){
        return interceptorRelationsMap.get(clazz);
    }

    public static void injectParams(@NonNull Activity activity) {
        injectParamsInternal(activity, null);
    }


    public static void injectParams(@NonNull Fragment fragment) {
        injectParamsInternal(fragment, null);
    }

    public static void injectParams(@NonNull android.support.v4.app.Fragment fragment) {
        injectParamsInternal(fragment, null);
    }

    public static void injectParams(@NonNull Object object, @NonNull Intent intent) {
        if (object instanceof Activity) {
            throw new IllegalArgumentException("if the obj is Activity, use injectParams(Activity)");
        }
        if (object instanceof Fragment) {
            throw new IllegalArgumentException("if the obj is Fragment, use injectParams(Fragment)");
        }
        if (object instanceof android.support.v4.app.Fragment) {
            throw new IllegalArgumentException("if the obj is Fragment v4, use injectParams(Fragment)");
        }
        injectParamsInternal(object, intent);
    }

    private static void injectParamsInternal(@NonNull Object object, Intent intent) {
        Class<?> key = object.getClass();
        Constructor<?> constructor = paramInjectorMap.get(key);
        if (constructor == null) {
            try {
                try {
                    Class<?> aClass = Class.forName(key.getName() + Constants._INTENT_PARAM_INJECTOR);
                    if (intent != null) {
                        constructor = aClass.getConstructor(key, Intent.class);
                    } else {
                        constructor = aClass.getConstructor(key);
                    }


                    if (constructor != null) {
                        paramInjectorMap.put(key, constructor);
                    } else {
                        throw new IllegalArgumentException("can not find the construct in " + aClass.getName());
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
                if (intent != null) {
                    constructor.newInstance(object, intent);
                } else {
                    constructor.newInstance(object);
                }
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
                    IRouterTable routerTable = (IRouterTable) obj;
                    routerTable.putRoutes(routerMap);
                    routerTable.putInterceptorRelations(interceptorRelationsMap);
                } else {
                    throw new IllegalArgumentException("init method need IRouterTable' class its parameter");
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static IntentRequest.Builder buildIntent(){
        return new IntentRequest.Builder() {
            @Override
            public IntentRequest build() {
                return new IntentRequest(getConfig());
            }
        };
    }

    public static ActivityRequestBuilder activity(@NonNull String route) {
        Class<?> activityClass = findClassByRoute(Constants.ACTIVITY_PREFIX, route);
        return new ActivityRequestBuilder(activityClass);
    }

    public static ActivityRequestBuilder activity(@NonNull Uri uri) {
        return new ActivityRequestBuilder().withData(uri);
    }

    public static ServiceRequestBuilder service(@NonNull String route) {
        Class<?> serviceClass = findClassByRoute(Constants.SERVICE_PREFIX, route);
        return new ServiceRequestBuilder(serviceClass);
    }

    @SuppressWarnings("unchecked")
    public static FragmentRequestBuilder fragment(@NonNull String route) {
        Class<?> fragmentClass = findClassByRoute(Constants.FRAGMENT_PREFIX, route);
        return new FragmentRequestBuilder((fragmentClass != null
                && Fragment.class.isAssignableFrom(fragmentClass))
                ? (Class<Fragment>) fragmentClass : null);
    }

    @SuppressWarnings("unchecked")
    public static FragmentV4RequestBuilder fragmentV4(@NonNull String route) {
        Class<?> fragmentClass = findClassByRoute(Constants.FRAGMENT_V4_PREFIX, route);
        return new FragmentV4RequestBuilder((fragmentClass != null
                && android.support.v4.app.Fragment.class.isAssignableFrom(fragmentClass))
                ? (Class<android.support.v4.app.Fragment>) fragmentClass : null);
    }


}
