package io.github.xiewinson.easyrouter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import io.github.xiewinson.easyrouter.library.Interceptor;

/**
 * Created by winson on 2017/12/7.
 */

public class SimpleInterceptor implements Interceptor {
    @Override
    public boolean intercept(Context context, Intent intent) {
        Toast.makeText(context, "经过拦截器", Toast.LENGTH_SHORT).show();
        return false;
    }
}
