package ashfifi.httptest.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hanks.htextview.fall.FallTextView;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import ashfifi.httptest.R;

public class MainActivity extends BaseActivity {
    Button button;
    TextView textView;

    TextInputLayout editText,editText2;
    Context context;

    private FallTextView  hTextView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        editText = (TextInputLayout) findViewById(R.id.usernameLayout);
        editText2 = (TextInputLayout)findViewById(R.id.passwordLayout);
        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView);
        hTextView3 = (FallTextView) findViewById(R.id.textview3);
        hTextView3.setOnClickListener(new ClickListener());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Splash.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                getAsynHttp();
            }
        });
    }
    private void getAsynHttp() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("name", editText.getEditText().getText().toString())
                .build();
        final Request request = new Request.Builder()
                .url("http://39.108.60.222:8080")
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
                        Toast.makeText(getApplication(), "请求成功", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}
