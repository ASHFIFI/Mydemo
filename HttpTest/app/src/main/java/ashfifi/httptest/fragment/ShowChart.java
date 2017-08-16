package ashfifi.httptest.fragment;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import ashfifi.httptest.Data.MyDataObject;
import ashfifi.httptest.R;


/**
 * Created by aa on 2017/8/15.
 */

public class ShowChart extends Fragment{
    LineChart chart;
    Activity  activity;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null) {
            activity =this.getActivity();
            view = activity.getLayoutInflater().inflate(R.layout.showchart,container,false);

            chart = (LineChart) view.findViewById(R.id.chart);

            MyDataObject[] dataObjects = new MyDataObject[5];
            dataObjects[0] = new MyDataObject(1, 11);
            dataObjects[1] = new MyDataObject(2, 12);
            dataObjects[2] = new MyDataObject(3, 13);
            dataObjects[3] = new MyDataObject(4, 14);
            dataObjects[4] = new MyDataObject(5, 15);
            List<Entry> entries = new ArrayList<Entry>();
            for (MyDataObject data : dataObjects) {
                entries.add(new Entry(data.getValueX(), data.getValueY()));
            }
            LineDataSet dataSet = new LineDataSet(entries, "测试");
            dataSet.setColor(200);
            dataSet.setValueTextColor(200);
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

            LineData lineData = new LineData(dataSet);

            chart.setData(lineData);
            chart.invalidate();
        }else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;

    }
}
