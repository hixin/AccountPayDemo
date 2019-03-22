a demo show how to use AccountPay SDK

## AccountPay
> AccountPay integrat login and payment functions. The interface is simple and easy to integrate. Users do not need to develop login and payment functions, which can effectively shorten the develop time.

## Download

You can use Gradle:

```
 repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            url "https://dl.bintray.com/cloudminds/CloudMindsAPIs"
        }
    }

dependencies {
   implementation 'com.cloudminds:AccountPay:1.1.0-preview'
}
```

## How do I use AccountPay?

SDK, all interfaces for use are placed in the class DataServiceApi, Most of the interfaces are asynchronous, returning callback information via AccountCallback.
```
public interface AccountCallback {

        /**
         * information related with userinfo
         *
         * @param userInfo
         */
        void onUserInfoResponse(@NonNull UserInfo userInfo);


        /**
         * other info, indicate request result
         * @param status  status:  STATUS_SUCCESS, STATUS_FAIL
         * @param msg     other info
         */
        void onOtherInfoResponse(@Nullable String status, @Nullable String msg);
    }
```

Simple use cases will look something like this:
```

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // step1  init, This method must be called first when using other functions 
        boolean result = DataServiceApi.getInstance().init(this, "450ecc36f5bb46cb8eec5ca8f589222d");
        if (!result) {
            Toast.makeText(this, "init: " + result, Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.user_image);
        textView = findViewById(R.id.user_name);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //use login function
                DataServiceApi.getInstance().login(MainActivity.this, new DataServiceApi.AccountCallback() {
                    @Override
                    public void onUserInfoResponse(@NonNull final UserInfo userInfo) {
                        mUserInfo = userInfo;
                    }

                    @Override
                    public void onOtherInfoResponse(@Nullable String status, @Nullable String msg) {
                        if (status.equals(Constants.STATUS_FAIL)) {
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });
        
        
        // use pay function
        findViewById(R.id.test_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataServiceApi.getInstance().payOrder(MainActivity.this, "Test pay subject", 0.01, PAY_CODE);
            }
        });

```

## 
