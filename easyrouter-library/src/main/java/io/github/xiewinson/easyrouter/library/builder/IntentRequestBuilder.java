package io.github.xiewinson.easyrouter.library.builder;

import io.github.xiewinson.easyrouter.library.request.IntentRequest;

/**
 * Created by winson on 2017/12/7.
 */

public class IntentRequestBuilder extends IntentRequest.Builder<IntentRequestBuilder> {
    @Override
    public IntentRequest build() {
        return new IntentRequest(getConfig());
    }
}
