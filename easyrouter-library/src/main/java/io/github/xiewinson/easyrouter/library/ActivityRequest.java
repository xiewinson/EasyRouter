package io.github.xiewinson.easyrouter.library;

import android.content.Context;
import android.content.Intent;

/**
 * Created by winson on 2017/11/29.
 */

public class ActivityRequest {

    private Intent intent;

    private ActivityRequest(Intent intent) {
        this.intent = intent;
    }

    public Intent asIntent() {
        return intent;
    }


    public void navigation(Context context) {
        context.startActivity(intent);
    }


    public static abstract class Builder {
        private Intent intent;

        protected Builder(Intent intent) {
            this.intent = intent;
        }

        public ActivityRequest build() {
            return new ActivityRequest(intent);
        }

        protected Intent getIntent() {
            return intent;
        }


    }
}
