package ashfifi.httptest.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ashfifi.httptest.Activity.Test;
import ashfifi.httptest.R;

/**
 * Created by aa on 2017/8/16.
 */

public class Profile extends Fragment{
    Activity activity;
    View view;
    ImageView imageView;
    private Button button;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(view==null) {
            super.onCreateView(inflater, container, savedInstanceState);
            activity = getActivity();
            view = activity.getLayoutInflater().inflate(R.layout.profile,container,false);

            button =(Button)view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(),Test.class);
                    startActivity(intent);
                }
            });
            imageView = (ImageView)view.findViewById(R.id.imageView2);
//            Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(imageView);
            Picasso.with(getContext()).load("http://39.108.60.222:8080/pic/macbook.jpg").into(imageView);
        }else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }
}
