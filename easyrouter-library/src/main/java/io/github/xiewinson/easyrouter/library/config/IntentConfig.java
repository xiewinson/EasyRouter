package io.github.xiewinson.easyrouter.library.config;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.github.xiewinson.easyrouter.library.Interceptor;
import io.github.xiewinson.easyrouter.library.callback.IntentListener;
import io.github.xiewinson.easyrouter.library.callback.NavigateListener;

/**
 * Created by winson on 2017/12/4.
 */

public class IntentConfig {

    public Class<?> clazz;

    public Bundle bundle = new Bundle();

    public int flags = -1;

    public String action;

    public Uri data;

    public String type;

    public String[] categories;

    public List<Class<? extends Interceptor>> interceptors = new ArrayList<>();

    public IntentListener intentListener;

    public NavigateListener navigateListener;

    public Intent toIntent(Context context) {
        Intent intent = new Intent();
        if (context != null && clazz != null) {
            intent.setClass(context, clazz);
        }
        intent.putExtras(bundle);
        if (flags != -1) {
            intent.setFlags(flags);
        }
        if (action != null) {
            intent.setAction(action);
        }
        if (data != null) {
            intent.setData(data);
        }

        if (type != null) {
            intent.setType(type);
        }

        if (categories != null) {
            for (String category : categories) {
                intent.addCategory(category);
            }
        }
        return intent;
    }
}
