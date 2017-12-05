package io.github.xiewinson.easyrouter.library.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.github.xiewinson.easyrouter.library.RequestConfig;
import io.github.xiewinson.easyrouter.library.callback.IntentListener;
import io.github.xiewinson.easyrouter.library.callback.NavigateListener;

/**
 * Created by winson on 2017/11/29.
 */

public class IntentRequest {

    private RequestConfig config;

    protected IntentRequest(@NonNull RequestConfig config) {
        this.config = config;
    }

    public Intent asIntent(@Nullable Context context) {
        Intent intent = config.toIntent(context);
        if (config.intentListener != null) {
            config.intentListener.onCreate(intent);
        }
        return intent;
    }

    public Intent asIntent() {
        return asIntent(null);
    }

    protected RequestConfig getConfig() {
        return config;
    }

    public static abstract class Builder<B extends Builder> {

        private RequestConfig config;

        private Builder() {
            config = new RequestConfig();
        }

        public RequestConfig getConfig() {
            return config;
        }

        protected Bundle getBundle() {
            return config.bundle;
        }

        protected Builder(Class<?> cls) {
            this();
            config.clazz = cls;
        }


        @SuppressWarnings("unchecked")
        public B withFlags(int flags) {
            config.flags = flags;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B withAction(String action) {
            config.action = action;
            return (B) this;
        }


        @SuppressWarnings("unchecked")
        public B withData(Uri uri) {
            config.data = uri;
            return (B) this;
        }


        @SuppressWarnings("unchecked")
        public B withType(String type) {
            config.type = type;
            return (B) this;
        }


        @SuppressWarnings("unchecked")
        public B withCategories(String[] categories) {
            config.categories = categories;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B withParam(String key, Object value) {
            BundleHelper.put(config.bundle, key, value);
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B intentCallback(IntentListener callback) {
            config.intentListener = callback;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B navigateListener(NavigateListener listener) {
            config.navigateListener = listener;
            return (B) this;
        }

        public abstract IntentRequest build();
    }
}
