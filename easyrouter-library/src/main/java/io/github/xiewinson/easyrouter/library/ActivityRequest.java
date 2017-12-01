package io.github.xiewinson.easyrouter.library;

import android.content.Context;
import android.content.Intent;

/**
 * Created by winson on 2017/11/29.
 */

public class ActivityRequest {

    private Intent intent;

    private Context context;

    private ActivityRequest(Context context, Intent intent) {
        this.intent = intent;
        this.context = context;
    }

    public Intent asIntent() {
        return intent;
    }


    public void navigation() {
        context.startActivity(intent);
    }


    public static abstract class Builder {
        private Intent intent;

        private Context context;

        protected Builder(Context context, Intent intent) {
            this.context = context;
            this.intent = intent;
        }

        public ActivityRequest build() {
            return new ActivityRequest(context, intent);
        }

        protected Intent getIntent() {
            return intent;
        }


    }
}
