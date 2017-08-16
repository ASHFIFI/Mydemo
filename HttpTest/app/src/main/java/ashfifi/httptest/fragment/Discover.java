package ashfifi.httptest.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ashfifi.httptest.R;

/**
 * Created by aa on 2017/8/16.
 */

public class Discover extends Fragment {
    Activity activity;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(view==null) {
            activity = getActivity();
            view = activity.getLayoutInflater().inflate(R.layout.discover,container,false);
        }else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }
}
