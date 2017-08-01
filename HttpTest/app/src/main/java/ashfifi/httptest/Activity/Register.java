package ashfifi.httptest.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ashfifi.httptest.R;

/**
 * Created by aa on 17/7/31.
 */

public class Register extends AppCompatActivity {

    Button button;
    TextView textView;
    TextInputLayout editText,editText2,editText3;
      Boolean usernameable=false,passwordable=false,phoneable=false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        textView = (TextView)findViewById(R.id.textView);
        button =(Button)findViewById(R.id.button);
        editText = (TextInputLayout)findViewById(R.id.usernameLayout);
        editText2 = (TextInputLayout)findViewById(R.id.passwordLayout);
        editText3 = (TextInputLayout)findViewById(R.id.phoneLayout);

        editText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>6){
                    usernameable=true;
                    isUseful();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>8){
                    passwordable=true;
                    isUseful();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText3.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>11){
                    phoneable=true;
                    isUseful();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                LoginOrNot();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_DIAL, Uri.parse("tel:15889634309"));
                startActivity(intent);
            }
        });
    }
    public void LoginOrNot(){
        String username=editText.getEditText().getText().toString();
        String password=editText2.getEditText().getText().toString();
        String phonenum=editText3.getEditText().getText().toString();
        if(!isLetterOrMix(username)){
            Toast.makeText(getApplicationContext(),"用户名为字母或与数字混合",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(!isLetterDigit(password)){
            Toast.makeText(getApplicationContext(),"密码为数字加字母",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(!isTelPhoneNumber(phonenum)){
            Toast.makeText(getApplicationContext(), "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        getAsynHttp();

    }
    public static boolean isLetterOrMix(String str){
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for(int i=0 ; i<str.length();i++){
            if(Character.isDigit(str.charAt(i))){   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if(Character.isLetter(str.charAt(i))){  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        return  (isDigit && isLetter&&str.matches(regex)) || isLetter&&str.matches(regex);
    }

    public static boolean isLetterDigit(String str){
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for(int i=0 ; i<str.length();i++){
            if(Character.isDigit(str.charAt(i))){   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if(Character.isLetter(str.charAt(i))){  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        return  isDigit && isLetter&&str.matches(regex);
    }

    public static boolean isTelPhoneNumber(String value) {
        if (value != null && value.length() == 11) {
            Pattern pattern = Pattern.compile("^1[3|4|5|6|7|8][0-9]\\d{8}$");
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }
        return false;
    }
    public  void isUseful(){
        if(usernameable && passwordable && phoneable){
            button.setClickable(true);
        }
    }
    private void getAsynHttp() {
        SimpleDateFormat   formatter   =   new SimpleDateFormat("yyyy-MM-dd");
        Date curDate =  new Date(System.currentTimeMillis());
        String   date   =   formatter.format(curDate);
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("type","reg")
                .add("username", editText.getEditText().getText().toString())
                .add("password",editText2.getEditText().getText().toString())
                .add("date",date)
                .add("phonenum",editText3.getEditText().getText().toString())
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
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(str.equals("已经被注册")){
                            Toast.makeText(getApplication(),str,Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(Register.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}
