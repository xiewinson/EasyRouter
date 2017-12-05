package io.github.xiewinson.easyrouter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Size;
import android.util.SizeF;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.xiewinson.easyrouter.annotation.BundleParam;
import io.github.xiewinson.easyrouter.annotation.Router;
import io.github.xiewinson.easyrouter.library.EasyRouter;

@Router(path = "/user")
public class UserActivity extends BaseActivity {

    @BundleParam
    int age;

    @BundleParam
    Integer age1;

    @BundleParam("user_name")
    String name;

    @BundleParam
    Bitmap bitmap;

    @BundleParam
    Bitmap[] data;

    @BundleParam
    ArrayList<Bitmap> images;

    @BundleParam
    U<Bitmap> u;

    @BundleParam
    HashMap<Integer, String> us;

    @BundleParam
    ArrayList<CharSequence> uuu;

    @BundleParam
    Size size;

    @BundleParam
    SizeF sizeF;

//    @BundleParam
//    HashMap<U, String> is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyRouter.injectIntentParams(this);
        setContentView(R.layout.activity_user);

    }
}
