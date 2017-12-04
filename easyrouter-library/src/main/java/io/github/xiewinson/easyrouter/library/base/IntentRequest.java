package io.github.xiewinson.easyrouter.library.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import io.github.xiewinson.easyrouter.library.callback.IntentCallback;
import io.github.xiewinson.easyrouter.library.callback.NavigateCallback;

/**
 * Created by winson on 2017/11/29.
 */

public abstract class IntentRequest {

    protected Context context;
    protected Intent intent;

    protected IntentRequest(Context context, Intent intent, Bundle bundle) {
        this.context = context;
        this.intent = intent;
        this.intent.putExtras(bundle);
    }

    public Intent asIntent() {
        return intent;
    }

    public abstract void navigation(NavigateCallback callback);

    public void navigation() {
        navigation(null);
    }

    public static abstract class Builder<T extends Builder> extends BaseRequestBuilder<T> {
        protected Context context;
        protected Intent intent;

        protected Builder(Context context, Class<?> cls) {
            this.context = context;
            this.intent = new Intent(context, cls);
        }

        protected Intent getIntent() {
            return intent;
        }

        @SuppressWarnings("unchecked")
        public T withFlags(int flags) {
            intent.addFlags(flags);
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T withAction(String action) {
            intent.setAction(action);
            return (T) this;
        }


        @SuppressWarnings("unchecked")
        public T withData(Uri uri) {
            intent.setData(uri);
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T withIntentCallback(IntentCallback intentCallback) {
            return (T) this;
        }

        @Override
        public abstract IntentRequest build();
    }
}
