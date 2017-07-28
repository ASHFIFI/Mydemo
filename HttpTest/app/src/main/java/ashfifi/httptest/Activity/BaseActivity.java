package ashfifi.httptest.Activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hanks.htextview.base.HTextView;



public class BaseActivity extends AppCompatActivity {
    String[] sentences = {
            "你说",
            "我是错的",
            "那你最好证明",
            "你是对的",
            "MJ"};
    int index;
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof HTextView) {
                if (index + 1 >= sentences.length) {
                    index = 0;
                }
                ((HTextView) v).animateText(sentences[index++]);
            }
        }
    }

}
