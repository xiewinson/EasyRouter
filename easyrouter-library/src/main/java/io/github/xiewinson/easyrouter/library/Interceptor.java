package io.github.xiewinson.easyrouter.library;

import android.content.Context;
import android.content.Intent;

/**
 * Created by winson on 2017/12/7.
 */

public interface Interceptor {
    boolean intercept(Context context, Intent intent);
}
