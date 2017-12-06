package io.github.xiewinson.easyrouter.library.builder;

import io.github.xiewinson.easyrouter.library.ServiceRequest;

/**
 * Created by winson on 2017/12/5.
 */

public final class ServiceRequestBuilder extends ServiceRequest.Builder<ServiceRequestBuilder> {

    public ServiceRequestBuilder(Class<?> cls) {
        super(cls);
    }

}
