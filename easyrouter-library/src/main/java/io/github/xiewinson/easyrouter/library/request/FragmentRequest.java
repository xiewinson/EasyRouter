package io.github.xiewinson.easyrouter.library.request;

import android.app.Fragment;
import android.os.Bundle;

import io.github.xiewinson.easyrouter.library.util.BundleHelper;
import io.github.xiewinson.easyrouter.library.config.FragmentConfig;

/**
 * Created by winson on 2017/11/30.
 */

public class FragmentRequest<T> {

    private FragmentConfig<T> config;

    protected FragmentRequest(FragmentConfig<T> config) {
        this.config = config;
    }

    public T asFragment() {
        if (config.clazz == null) {
            return null;
        }
        try {
            T t = config.clazz.newInstance();
            if (t instanceof Fragment) {
                ((Fragment) t).setArguments(config.bundle);
            } else if (t instanceof android.support.v4.app.Fragment) {
                ((android.support.v4.app.Fragment) t).setArguments(config.bundle);
            }
            return t;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static class Builder<T, B> {
        private FragmentConfig<T> config;

        protected Builder(Class<T> cls) {
            config = new FragmentConfig<>();
            config.clazz = cls;
        }

        @SuppressWarnings("unchecked")
        public B withBundleParam(String key, Object value) {
            BundleHelper.put(config.bundle, key, value);
            return (B) this;
        }

        public Bundle getBundle() {
            return config.bundle;
        }

        public FragmentRequest<T> build() {
            return new FragmentRequest<>(config);
        }
    }
}