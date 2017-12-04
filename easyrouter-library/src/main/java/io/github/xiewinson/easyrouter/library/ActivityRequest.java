package io.github.xiewinson.easyrouter.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.github.xiewinson.easyrouter.library.base.IntentRequest;
import io.github.xiewinson.easyrouter.library.callback.NavigateCallback;

/**
 * Created by winson on 2017/11/29.
 */

public class ActivityRequest extends IntentRequest {

    public ActivityRequest(Context context, Intent intent, Bundle bundle) {
        super(context, intent, bundle);
    }

    @Override
    public void navigation(NavigateCallback callback) {
        context.startActivity(asIntent());

    }

    public static class Builder<T extends Builder> extends IntentRequest.Builder<T> {

        protected Builder(Context context, Class<?> cls) {
            super(context, cls);
        }

        @Override
        public ActivityRequest build() {
            return new ActivityRequest(context, getIntent(), getBundle());
        }

    }
}
