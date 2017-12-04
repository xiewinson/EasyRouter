package io.github.xiewinson.easyrouter.library;

import java.util.Map;

/**
 * Created by winson on 2017/12/2.
 */

public interface IRouterTable {
    void insertInto(Map<String, Class<?>> map);
}
