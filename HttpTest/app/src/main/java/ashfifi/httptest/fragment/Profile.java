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

public class Profile extends Fragment{
    Activity activity;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(view==null) {
            super.onCreateView(inflater, container, savedInstanceState);
            activity = getActivity();
            view = activity.getLayoutInflater().inflate(R.layout.profile,container,false);
        }else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }
}
