package io.github.xiewinson.easyrouter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.xiewinson.easyrouter.annotation.Param;
import io.github.xiewinson.easyrouter.annotation.Router;
import io.github.xiewinson.easyrouter.router.EasyRouter;

@Router(path = "user")
public class UserActivity extends AppCompatActivity {

    @Param("user_name")
    String name;

    @Param
    Bitmap bitmap;

    @Param
    int[] data;

    @Param
    ArrayList<Bitmap> images;

    @Param
    HashMap<View, String> is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        EasyRouter.inject(this);

        Log.d("winson", "name ->" + name);
    }
}
