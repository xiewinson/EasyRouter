package io.github.xiewinson.easyrouter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.xiewinson.easyrouter.annotation.Param;
import io.github.xiewinson.easyrouter.annotation.Route;
import io.github.xiewinson.easyrouter.library.EasyRouter;

@Route(value = "user")
public class UserActivity extends BaseActivity {

    @Param
    Bundle originBundle;

    @Param
    int age;

    @Param
    Integer age1;

    @Param("user_name")
    String name;

    @Param
    Bitmap bitmap;

    @Param
    Bitmap[] data;

    @Param
    ArrayList<Bitmap> images;

    @Param
    HashMap<Integer, String> us;

    @Param
    ArrayList<CharSequence> uuu;

    @Param
    Size size;

    @Param
    SizeF sizeF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        EasyRouter.injectParams(this);
        Log.d("winson", "");
    }
}
