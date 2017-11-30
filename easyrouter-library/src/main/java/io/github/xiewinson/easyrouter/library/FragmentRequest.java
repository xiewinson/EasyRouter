package io.github.xiewinson.easyrouter.library;

import android.os.Bundle;

/**
 * Created by winson on 2017/11/30.
 */

public class FragmentRequest<T> {

    private Bundle bundle;
    private Class<T> cls;

    private FragmentRequest(Class<T> cls, Bundle bundle) {
        this.bundle = bundle;
        this.cls = cls;
    }

    public T asFragment() {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static abstract class Builder<T> {

        private Bundle bundle;
        private Class<T> cls;

        protected Builder(Class<T> acls, Bundle bundle) {
            this.bundle = bundle;
            this.cls = acls;
        }

        public FragmentRequest bulid() {
            return new FragmentRequest<>(cls, bundle);
        }
    }
}