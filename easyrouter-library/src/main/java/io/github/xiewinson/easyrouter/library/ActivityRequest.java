package io.github.xiewinson.easyrouter.library;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import io.github.xiewinson.easyrouter.library.base.IntentRequest;
import io.github.xiewinson.easyrouter.library.callback.NavigateListener;

/**
 * Created by winson on 2017/11/29.
 */

public class ActivityRequest extends IntentRequest {

    protected ActivityRequest(@NonNull RequestConfig config) {
        super(config);
    }

    private Intent checkIntent(@NonNull Context context) {
        Intent intent = asIntent(context);
        ActivityInfo activityInfo = intent.resolveActivityInfo(context.getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY);
        boolean result = activityInfo != null;
        NavigateListener listener = getConfig().navigateListener;
        if (listener != null) {
            listener.onNavigate(intent, result);
        }
        return result ? intent : null;
    }

    public void navigation(@NonNull Context context) {
        Intent intent = checkIntent(context);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    public void navigation(@NonNull Activity activity, int requestCode) {
        Intent intent = checkIntent(activity);
        if (intent != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public void navigation(@NonNull Activity activity) {
        navigation(activity, -1);
    }

    public void navigation(@NonNull Activity activity, @NonNull Bundle options, int requestCode) {
        Intent intent = checkIntent(activity);
        if (intent != null) {
            ActivityCompat.startActivityForResult(activity, intent, requestCode, options);
        }
    }

    public void navigation(@NonNull Activity activity, @NonNull Bundle options) {
        Intent intent = checkIntent(activity);
        if (intent != null) {
            ActivityCompat.startActivity(activity, intent, options);
        }
    }
    
    public void navigation(@NonNull Fragment fragment, int requestCode) {
        Intent intent = checkIntent(fragment.getActivity());
        if (intent != null) {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    public void navigation(@NonNull Fragment fragment) {
        navigation(fragment, -1);
    }

    public void navigation(@NonNull android.support.v4.app.Fragment fragment, int requestCode) {
        Intent intent = checkIntent(fragment.getActivity());
        if (intent != null) {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    public void navigation(@NonNull android.support.v4.app.Fragment fragment) {
        navigation(fragment, -1);
    }

    public static class Builder<B extends Builder> extends IntentRequest.Builder<B> {

        protected Builder(Class<?> cls) {
            super(cls);
        }

        @Override
        public ActivityRequest build() {
            return new ActivityRequest(getConfig());
        }

    }
}
