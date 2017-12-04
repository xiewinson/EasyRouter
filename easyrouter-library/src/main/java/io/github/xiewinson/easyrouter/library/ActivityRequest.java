package io.github.xiewinson.easyrouter.library;

import android.content.Context;
import android.support.annotation.NonNull;

import io.github.xiewinson.easyrouter.library.base.IntentRequest;

/**
 * Created by winson on 2017/11/29.
 */

public class ActivityRequest extends IntentRequest {

    protected ActivityRequest(@NonNull RequestConfig config) {
        super(config);
    }

    public void navigation(Context context) {
        context.startActivity(asIntent(context));
    }

    public static class Builder<B extends Builder> extends IntentRequest.Builder<B> {

        protected Builder(Class<?> cls) {
            super(cls);
        }

        @Override
        public ActivityRequest build() {
            return new ActivityRequest(config);
        }

    }
}
