package io.github.xiewinson.easyrouter.library.inner;

import java.util.List;
import java.util.Map;

import io.github.xiewinson.easyrouter.library.Interceptor;

/**
 * Created by winson on 2017/12/2.
 */

public interface IRouterTable {

    void putRoutes(Map<String, Class<?>> map);

    void putInterceptorRelations(Map<Class<?>, List<Class<? extends Interceptor>>> map);
}
