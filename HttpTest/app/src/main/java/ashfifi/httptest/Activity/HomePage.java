package ashfifi.httptest.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import ashfifi.httptest.Adapter.FragAdapter;
import ashfifi.httptest.R;
import ashfifi.httptest.fragment.Discover;
import ashfifi.httptest.fragment.Profile;
import ashfifi.httptest.fragment.ShowChart;
import ashfifi.httptest.fragment.Spectrum;
import ashfifi.httptest.transform.DepthPageTransformer;

/**
 * Created by aa on 17/8/1.
 */

public class HomePage extends AppCompatActivity {
    Profile profile;
    Discover discover;
    ShowChart showChart;
    Spectrum spectrum;
    Fragment[] fragments;

    private TextView txt_title;
    private ImageView img_right;
    protected static final String TAG = "MainActivity";
    private TextView unreaMsgdLabel;// 未读消息textview
    private TextView unreadAddressLable;// 未读通讯录textview
    private TextView unreadFindLable;// 发现
    private ImageView[] imagebuttons;
    private TextView[] textviews;
    private String connectMsg = "";;
    private int index;
    private int currentTabIndex;// 当前fragment的index
    private ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        profile = new Profile();
        discover = new Discover();
        showChart = new ShowChart();
        spectrum = new Spectrum();
        fragments = new Fragment[]{showChart,spectrum,discover,profile};

        imagebuttons = new ImageView[4];
        imagebuttons[0] = (ImageView) findViewById(R.id.ib_weixin);
        imagebuttons[1] = (ImageView) findViewById(R.id.ib_contact_list);
        imagebuttons[2] = (ImageView) findViewById(R.id.ib_find);
        imagebuttons[3] = (ImageView) findViewById(R.id.ib_profile);

        imagebuttons[0].setSelected(true);

        textviews = new TextView[4];
        textviews[0] = (TextView) findViewById(R.id.tv_weixin);
        textviews[1] = (TextView) findViewById(R.id.tv_contact_list);
        textviews[2] = (TextView) findViewById(R.id.tv_find);
        textviews[3] = (TextView) findViewById(R.id.tv_profile);
        textviews[0].setTextColor(0xFF45C01A);

        FragmentManager fm =getSupportFragmentManager();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        FragAdapter fragAdapter = new FragAdapter(fm,fragments);
        viewPager.setAdapter(fragAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setPageTransformer(true,new DepthPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imagebuttons[currentTabIndex].setSelected(false);
                // 把当前tab设为选中状态
                imagebuttons[position].setSelected(true);
                textviews[currentTabIndex].setTextColor(0xFF999999);
                textviews[position].setTextColor(0xFF45C01A);
                currentTabIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.re_weixin:
                index = 0;
                if (showChart != null) {
                    //refresh
                }
                //txt_title.setText("图表");
                //img_right.setImageResource(R.drawable.icon_add);
                break;
            case R.id.re_contact_list:
                index = 1;
               // txt_title.setText("频谱");
                //img_right.setVisibility(View.VISIBLE);
                //img_right.setImageResource(R.drawable.icon_titleaddfriend);
                break;
            case R.id.re_find:
                index = 2;
                //txt_title.setText("发现");
                break;
            case R.id.re_profile:
                index = 3;
                //txt_title.setText("我");
                break;
        }
        if (currentTabIndex != index) {
            viewPager.setCurrentItem(index);
        }
        imagebuttons[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        imagebuttons[index].setSelected(true);
        textviews[currentTabIndex].setTextColor(0xFF999999);
        textviews[index].setTextColor(0xFF45C01A);
        currentTabIndex = index;
    }

}
