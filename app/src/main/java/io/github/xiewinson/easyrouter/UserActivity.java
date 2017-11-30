package io.github.xiewinson.easyrouter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.xiewinson.easyrouter.annotation.IntentParam;
import io.github.xiewinson.easyrouter.annotation.Router;
import io.github.xiewinson.easyrouter.core.EasyRouter;

@Router(path = "user")
public class UserActivity extends AppCompatActivity {

    @IntentParam("user_name")
    String name;

    @IntentParam
    Bitmap bitmap;

    @IntentParam
    int[] data;

    @IntentParam
    ArrayList<Bitmap> images;

    @IntentParam
    HashMap<View, String> is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        EasyRouter.injectIntentParams(this);
        Log.d("winson", "name ->" + name);

    }
}
