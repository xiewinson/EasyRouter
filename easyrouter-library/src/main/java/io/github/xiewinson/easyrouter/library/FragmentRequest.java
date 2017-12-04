package io.github.xiewinson.easyrouter.library;

import android.app.Fragment;
import android.os.Bundle;

import io.github.xiewinson.easyrouter.library.base.BundleHelper;

/**
 * Created by winson on 2017/11/30.
 */

public class FragmentRequest<T> {

    private Class<T> cls;
    private Bundle bundle;

    protected FragmentRequest(Class<T> cls, Bundle bundle) {
        this.cls = cls;
        this.bundle = bundle;
    }

    public T asFragment() {
        try {
            T t = cls.newInstance();
            if (t instanceof Fragment) {
                ((Fragment) t).setArguments(bundle);
            } else if (t instanceof android.support.v4.app.Fragment) {
                ((android.support.v4.app.Fragment) t).setArguments(bundle);
            }
            return t;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static class Builder<T, B> {

        private Class<T> cls;
        private Bundle bundle;

        protected Builder(Class<T> cls) {
            this.cls = cls;
            this.bundle = new Bundle();
        }

        @SuppressWarnings("unchecked")
        public B withParam(String key, Object value) {
            BundleHelper.put(bundle, key, value);
            return (B) this;
        }

        public Bundle getBundle() {
            return bundle;
        }

        public FragmentRequest<T> build() {
            return new FragmentRequest<>(cls, bundle);
        }
    }
}