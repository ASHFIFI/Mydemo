package ashfifi.httptest.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hanks.htextview.fall.FallTextView;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ashfifi.httptest.R;

import static ashfifi.httptest.Activity.Register.isLetterDigit;
import static ashfifi.httptest.Activity.Register.isTelPhoneNumber;

public class MainActivity extends BaseActivity {
    Button button;
    TextView textView;

    TextInputLayout editText,editText2;
    String phoneNumber;
    String password;
    String phoneNumberText = null;
    String passwordText = null;
    private FallTextView  hTextView3;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    String longitude="0";//经度
    String latitude="0";//纬度
    String speed="0";//
    String Bearing="0";
    String address="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        editText = (TextInputLayout) findViewById(R.id.usernameLayout);
        editText2 = (TextInputLayout)findViewById(R.id.passwordLayout);
        SharedPreferences sharedPre=getApplicationContext().getSharedPreferences("config",Context.MODE_PRIVATE);
        phoneNumberText = sharedPre.getString("phoneNumber","");
        passwordText = sharedPre.getString("password","");
        if(!(phoneNumberText.equals(""))){
            editText.getEditText().setText(phoneNumberText);
            editText2.getEditText().setText(passwordText);
        }

        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView);
        hTextView3 = (FallTextView) findViewById(R.id.textview3);
        hTextView3.setOnClickListener(new ClickListener());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                if(!LoginOrNot()){
                    Toast.makeText(getApplicationContext(),"手机号码不能为空",Toast.LENGTH_SHORT);
                }else{
                    getAsynHttp();
                }
            }
        });
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //声明定位回调监听器
            AMapLocationListener mLocationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation loc) {
                    if (null != loc) {
                        //解析定位结果
                        showTheResult(loc);
                    }else {
                        Toast.makeText(getApplicationContext(),"获取定位失败",Toast.LENGTH_SHORT);
                    }
                }
            };
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            mLocationClient.setLocationOption(getOptions());
            mLocationClient.startLocation();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECORD_AUDIO},8);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }
    public boolean LoginOrNot(){
        String phone=editText.getEditText().getText().toString();
        String password=editText2.getEditText().getText().toString();
        if(!isLetterDigit(password)){
            Toast.makeText(getApplicationContext(),"密码为数字加字母",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isTelPhoneNumber(phone)){
            Toast.makeText(getApplicationContext(), "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void destroyLocation(){
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationClient = null;
        }
    }
    public void onRequestPermissionsResult(int RequestCode,String[] Permissions,int[] Grantresult){
        if(RequestCode == 8){
            if(Grantresult[0]==PackageManager.PERMISSION_GRANTED){
                AMapLocationListener mLocationListener = new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation loc) {
                        if (null != loc) {
                            //解析定位结果
                           showTheResult(loc);
                        }else {
                           Toast.makeText(getApplicationContext(),"获取定位失败",Toast.LENGTH_SHORT);
                        }
                    }
                };
                //初始化定位
                mLocationClient = new AMapLocationClient(getApplicationContext());
                //设置定位回调监听
                mLocationClient.setLocationListener(mLocationListener);
                mLocationClient.setLocationOption(getOptions());
                mLocationClient.startLocation();

            }else{
                Toast.makeText(MainActivity.this,"没有权限",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(RequestCode,Permissions,Grantresult);
    }
    private void getAsynHttp() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("type","log")
                .add("phonenum",editText.getEditText().getText().toString())
                .add("password", editText2.getEditText().getText().toString())
                .add("longitude",longitude)
                .add("latitude",latitude)
                .add("speed",speed)
                .add("Bearing",Bearing)
                .add("address",address)
                .build();
        final Request request = new Request.Builder()
                .url("xxx")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplication(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(final Response response) throws IOException {
                final String data = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleLogin(data);
                    }
                });
            }
        });
    }
    private void handleLogin(String data){
        if(data.equals("登录成功")){
            if(phoneNumberText.equals("")){
                phoneNumber = editText.getEditText().getText().toString();
                password = editText2.getEditText().getText().toString();
                saveLoginInfo(getApplicationContext(),phoneNumber,password);
            }
            jumpToHomePage();
        }else{
            Toast.makeText(getApplication(),data, Toast.LENGTH_SHORT).show();
        }
    }
    public static void saveLoginInfo(Context context,String phoneNumber,String password){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("password", password);
        //提交
        editor.commit();
    }
    private void jumpToHomePage(){
        Intent intent=new Intent(MainActivity.this,HomePage.class);
        startActivity(intent);
        finish();
    }
    public void showTheResult(AMapLocation location){
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if(location.getErrorCode() == 0){
//            sb.append("定位成功" + "\n");
//            sb.append("定位类型: " + location.getLocationType() + "\n");
//            sb.append("经    度    : " + location.getLongitude() + "\n");
//            sb.append("纬    度    : " + location.getLatitude() + "\n");
//            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//            sb.append("提供者    : " + location.getProvider() + "\n");
//            sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//            sb.append("角    度    : " + location.getBearing() + "\n");
//            // 获取当前提供定位服务的卫星个数
//            sb.append("星    数    : " + location.getSatellites() + "\n");
//            sb.append("国    家    : " + location.getCountry() + "\n");
//            sb.append("省            : " + location.getProvince() + "\n");
//            sb.append("市            : " + location.getCity() + "\n");
//            sb.append("城市编码 : " + location.getCityCode() + "\n");
//            sb.append("区            : " + location.getDistrict() + "\n");
//            sb.append("区域 码   : " + location.getAdCode() + "\n");
//            sb.append("地    址    : " + location.getAddress() + "\n");
//            sb.append("兴趣点    : " + location.getPoiName() + "\n");
//            //定位完成的时间
//            sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
              longitude = ""+location.getLongitude();
              latitude  = ""+location.getLatitude();
              speed     = ""+location.getSpeed();
              Bearing   = ""+location.getBearing();
              address   = ""+location.getAddress();
        } else {
            //定位失败
//            sb.append("定位失败" + "\n");
//            sb.append("错误码:" + location.getErrorCode() + "\n");
//            sb.append("错误信息:" + location.getErrorInfo() + "\n");
//            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        //定位之后的回调时间
        sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
    }
    private static SimpleDateFormat sdf = null;
    public synchronized static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }
    public AMapLocationClientOption getOptions(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
}
