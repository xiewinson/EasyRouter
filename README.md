### Android 统一跳转

## 使用步骤(暂时还未上传到自己的Maven)

在基础 module 或 application module 中引入基础 library 
```
dependencies {
   // implementation方式只在本module可见，若是在基础 library 中引入，请使用api方式
   implementation  project(':easyrouter-library') 
}
```

在有需要生成路由的 module 的 gradle 中, 引入compiler library, 指定 moduleName
```
dependencies {
    // 此处请使用 annotationProcessor 方式引用，compiler用于编译时生成类，不用将此部分打包 
    annotationProcessor project(':easyrouter-compiler')
  
}
defaultConfig {
    javaCompileOptions {
        annotationProcessorOptions {
            // 需要指定模块名称，因为会根据此名称生成此模块的 RouterTable 类，取名按照 Java 命名规范即可，若 module 取名为 'App'，则生成文件名最终为 AppRouterTable
            arguments = [moduleName : 'App']
        }
    }
}
```
在 APP 入口初始化
```
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // 此AppRouterTable即为上一步骤所指定名称，
        EasyRouter.init(AppRouterTable.class, MlRouterTable.class);
    }
}

```
在 Activity/Service/Fragment 中可使用 Route 和 Param 注解
```
@Route(value = "user", interceptors = {SimpleInterceptor.class})
public class UserActivity extends BaseActivity {

    @Param
    int age;

    @Param("user_name")
    String name;

    @Param
    long id;
    
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        EasyRouter.injectParams(this);
    }
}
```
Activity 跳转
```
//指定在 activity 中寻找
EasyRouter.activity("user")
    .withParam  
    //支持所有 Bundle  支持的参数
    .withParam("user_name", "winson")
    .withParam("age", 18)
    .withParam("id", 123456789L)
    //指定 Intent 启动 方式
    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    //指定拦截器 ，执行顺序按照插入顺序，在Route 中声明的拦截器最先执行
    .interceptor(SimpleInterceptor.class)
    .intentCallback(new IntentListener() {
        @Override
        public void onCreate(Intent intent) {
            //若有任何补充还可以直接声明该回调进 行操作，该回调在Intent创建完成时执行
        }
    })
    .navigateListener(new NavigateListener() {          
        @Override
        public void onNavigate(Intent intent, boolean result) {
            //跳转回调，通过result 取得是否跳转成功
        }
    })
    .build()
    //执行跳转，通过需求选择重载版本，可添加 RequestCode、转场动画、从 fragment 中打开 activity 等
    .navigation(context);
```
> Service 的使用方式与 Activity 相似，但 navigation  方法只支持 startService 方式。如果要进行 bindService 的方式，推荐在调用 build() 方法后使用 asIntent(Context) 方法得到 Intent 对象进行操作
> Fragment 的使用方式也类似，但注意，根据实际项目中使 用的 fragment 版本，提供了 fragment() 和 fragmentV4() 两个版本。由于 Fragment 必须依附于Activity，所以没有提供 navigation 方法，在 build() 后直接使用 asFragment() 方法可取得 Fragment 对象

隐式启动 Activity (网页打开App, 互无关联的两个 Module 打开对方 Activity 只能使用此方式)

```
//必须在 Manifest.xml 中指定 intent-filter，在示例例中 host 字段代表了了具体的页 面，如果实际需求中 host 在整个 APP 中是固定不不变的，那么就只能用 path 来表示指定页 面，推荐使用 pathPrefix 属性来匹配，这样如果声明 pathPrefix 为 "/user", 能匹配到请求是 "/user/detail" 的 Uri
<activity android:name=".UserActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <data android:host="user" android:scheme="app" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>

//与显示启动不不同，但仅需要 activity(Uri) 方法即可
EasyRouter
    .activity(Uri.parse("app://user/discovery/trade? id=257&name=winson"))
    .withParam("p0", "winson")
    .build()
    .navigation(context);

//获取参数, 例例如请求为"app://user/discovery/trade?id=257&name=winson"" 
Uri data = getIntent().getData();
//取得host，即为"user"
data.getHost();
//取得 一个list，为 path 的分段，结果 {"discovery", "trade"}
data.getPathSegments();
//通过 key 取得 query参数
data.getQueryParameter(String)；
```
> 隐式启动的方式仅支持 Activity，Fragment 本身就 无此概念，Service 在 Android 5.0 后不不能在隐式启动，因此 EasyRouter 也不不提供隐式启动 Service 方法。对于 BroadCast，因很少使用，没有专门做支持，但可使用 EasyRouter.buildIntent() 方法链式调用构建 Intent

## 其他的一些用法

> 可选的其他跳转方式
在使用中，输入字符串串去匹配路路由很舒服很统 一，但是传递参数时候必须输入 key 和 value 两部分，字符串串输错了了就没法跳转了；其次，创建 Fragment的过程通过路路由其实并没有太大必要。所以根据 Route 和 Param 注解在编译时为每个
Acivity、Service、Fragment也生成了快捷使用 方法，建议在使用 Fragment
 和 Service 时优先考虑这种方式
 
``` 
//这个类每个是前面提过的需要自己指定前缀名字的

AppRouterTable
    .activity()
    //user对应 UserActivity 中 route 注解的 value 参数
    .userBuilder()
    //根据 Param 注解的 name 变量生成的方法
    .name("winson")
    .age(25)
    .id(1242141L)
    .build()
    .navigation(context);
```

关于解析 Bundle

> 前面提过在使用 EasyRouter.injectParams(Activity)， EasyRouter.injectParams(Fragment) 可以自动解析对关联 Bundle 中的参数到类变量量。如果是非 Activity 和 Fragment 的类需要解析 Intent 中的 Bundle，则使用 EasyRouter.injectParams(this, Intent) 方法，手动传 入当前实例和需要解析的 Intent。最后，变量必须是默认访问权限或者 public 权限。
