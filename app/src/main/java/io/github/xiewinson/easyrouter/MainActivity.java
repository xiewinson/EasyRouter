package io.github.xiewinson.easyrouter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.xiewinson.easyrouter.annotation.Route;
import io.github.xiewinson.easyrouter.core.AppRouterTable;
import io.github.xiewinson.easyrouter.library.EasyRouter;

@Route("/main")
public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ButterKnife.bind(this, this);
        setSupportActionBar(toolbar);
        final ArrayList<View> views = new ArrayList<>();
        TextView tv = new TextView(this);
        views.add(tv);

        final Bundle bundle = new Bundle();
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, SecondActivity.class));
//                new EasyRouter.ActivityRouter().mainBuilder();
                HashMap<Integer, String> map = new HashMap<>();
                final ArrayList<Bitmap> bitmaps = new ArrayList<>();
                final Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
                final Bitmap[] data = {bitmap};
                bitmaps.add(bitmap);
                map.put(1, "555");
                map.put(2, "000");
                android.support.v4.app.Fragment studentFragment = EasyRouter.fragmentV4("StudentFragment")
                        .withBundleParam(null, null).build().asFragment();

                ArrayList<CharSequence> uuu = new ArrayList<>();
                uuu.add("22");

//                AppRouterTable
//                        .activity()
//                        .userBuilder()
//                        .age(92)
//                        .name("winson")
//                        .us(map)
//                        .uuu(uuu)
//                        .intentCallback(new IntentListener() {
//                            @Override
//                            public void onCreate(Intent intent) {
//                                bundle.putSparseParcelableArray(null, new SparseArray<Parcelable>());
//                                intent.putExtra("bitmap", bitmap);
//                                intent.putExtra("data", data);
//                                intent.putParcelableArrayListExtra("images", bitmaps);
//                                intent.putExtra("bitmap", bitmap);
//                            }
//                        })
//                        .build()
//                        .navigation(MainActivity.this);
//                EasyRouter
//                        .activity(Uri.parse("test://user"))
//                        .build()
//                        .navigation(MainActivity.this);
//                AppRouterTable.service().myServiceBuilder().taskId(222).taskName("哈哈哈哈").build().navigation(MainActivity.this);

                Bundle b = new Bundle();
                b.putInt("age", 100);
                EasyRouter.activity("user")
                        .withParam("age", 25)
                        .withParam("originBundle", b)
                        .build()
                        .navigation(MainActivity.this);
//                AppRouterTable
//                        .activity()
//                        .userBuilder()
//                        .age(25)
//                        .originBundle(b)
//                        .build()
//                        .navigation(MainActivity.this);


//                EasyRouter.activity("second").build().navigation(MainActivity.this);
//                MLRouterTable.activity().secondBuilder().name("哈哈哈").build().navigation(MainActivity.this);
//                EasyRouter.activity(Uri.parse("test://user:8888/detail?name=winson&id=20118622&isMan=true")).build().navigation(MainActivity.this);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
