package io.github.xiewinson.easyrouter.library;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;

import io.github.xiewinson.easyrouter.library.base.IntentRequest;
import io.github.xiewinson.easyrouter.library.callback.NavigateListener;

/**
 * Created by winson on 2017/12/5.
 */

public class ServiceRequest extends IntentRequest {
    protected ServiceRequest(@NonNull IntentConfig config) {
        super(config);
    }

    private Intent checkIntent(@NonNull Context context) {
        Intent intent = asIntent(context);
        ResolveInfo resolveInfo = context.getPackageManager().resolveService(intent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean result = resolveInfo != null;
        NavigateListener listener = getConfig().navigateListener;
        if (listener != null) {
            listener.onNavigate(intent, result);
        }
        return result ? intent : null;
    }

    public void navigation(@NonNull Context context) {
        Intent intent = checkIntent(context);
        if (intent != null) {
            context.startService(intent);
        }
    }

    public static class Builder<B extends Builder> extends IntentRequest.Builder<B> {

        protected Builder(Class<?> cls) {
            super(cls);
        }

        @Override
        public ServiceRequest build() {
            return new ServiceRequest(getConfig());
        }

    }
}
