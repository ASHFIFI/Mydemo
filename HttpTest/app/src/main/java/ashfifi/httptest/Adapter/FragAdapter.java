package ashfifi.httptest.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by aa on 2017/8/17.
 */

public class FragAdapter extends FragmentPagerAdapter {

    //private List<Fragment> mFragments;
    private Fragment[] mFragments;

//    public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
//        super(fm);
//        // TODO Auto-generated constructor stub
//        mFragments=fragments;
//    }

    public FragAdapter(FragmentManager fm, Fragment[] fragments){
        super(fm);
        // TODO Auto-generated constructor stub
        mFragments=fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        //return mFragments.get(arg0);
        return  mFragments[arg0];
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return mFragments.size();
        return mFragments.length;
    }

}

