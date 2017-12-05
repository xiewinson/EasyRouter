package io.github.xiewinson.easyrouter.library.inner;

import io.github.xiewinson.easyrouter.library.ActivityRequest;

/**
 * Created by winson on 2017/12/5.
 */

public final class ActivityRequestBuilder extends ActivityRequest.Builder<ActivityRequestBuilder> {

    public ActivityRequestBuilder(Class<?> cls) {
        super(cls);
    }

    public ActivityRequestBuilder() {
        super();
    }
}
