package com.dingyong.brokenlinetrendview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] mDefaultHorizontalText = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};

    private Double[] mScoreArr = new Double[]{200d, 220d, 30d, 40d, 190d, 140d, 260d, 0d, 40d, 178d, 250d, 140d};
    private Double[] mScoreArr2 = new Double[]{150d, 40d, 90d, 90d, 290d, 340d, 160d, 232d, 220d, 278d, 290d, 340d};
    private Double[] mScoreArr3 = new Double[]{90d, 80d, 170d, 290d, 230d, 240d, 260d, 332d, 120d, 78d, 90d, 40d};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BrokenLineTrendView mBrokenLineTrendView = (BrokenLineTrendView) findViewById(R.id.scoreTrendView);
        List<Double> doubles = new ArrayList<>();
        doubles.add(0d);
        doubles.add(100d);
        doubles.add(200d);
        doubles.add(300d);
        doubles.add(400d);



        BrokenLineTrendData data = new BrokenLineTrendData();
        List<Double> doubles1 = Arrays.asList(mScoreArr);
        List<Double> doubles2 = Arrays.asList(mScoreArr2);
        List<Double> doubles3 = Arrays.asList(mScoreArr3);
        List<BrokenLineDimension> mDataList = new ArrayList<>();
        BrokenLineDimension d1 = new BrokenLineDimension();
        d1.mDatasList = doubles1;
        d1.mBrokenLineColor = getResources().getColor(R.color.color_01_line);
        d1.mBrokenPointColor = getResources().getColor(R.color.color_01_line);
        d1.mBrokenPointIntColor = getResources().getColor(R.color.color_01_point_in);
        d1.mBrokenPointOutColor = getResources().getColor(R.color.color_01_point_out);
        d1.remark = "研发体系";

        BrokenLineDimension d2 = new BrokenLineDimension();
        d2.mDatasList = doubles2;
        d2.mBrokenLineColor = getResources().getColor(R.color.color_02_line);
        d2.mBrokenPointColor = getResources().getColor(R.color.color_02_point);
        d2.mBrokenPointIntColor = getResources().getColor(R.color.color_02_point_in);
        d2.mBrokenPointOutColor = getResources().getColor(R.color.color_02_point_out);
        d2.remark = "市场体系";




        BrokenLineDimension d3 = new BrokenLineDimension();
        d3.mDatasList = doubles3;
        d3.mBrokenLineColor = getResources().getColor(R.color.colorPrimary);
        d3.mBrokenPointColor = getResources().getColor(R.color.colorPrimary);
        d3.mBrokenPointIntColor = getResources().getColor(R.color.colorPrimary);
        d3.mBrokenPointOutColor = getResources().getColor(R.color.colorPrimary);
        d3.remark = "职能体系";
        mDataList.add(d1);
        mDataList.add(d2);
        mDataList.add(d3);


        data.mYLineDataList = doubles;
        data.mXLineDataList = Arrays.asList(mDefaultHorizontalText);
        data.mDimensionList = mDataList;
        data.mSelectColor = getResources().getColor(R.color.colorAccent);

        mBrokenLineTrendView.setBrokenLineTrendData(data);
        mBrokenLineTrendView.setOnItemClick(new BrokenLineTrendView.OnItemClick() {
            @Override
            public void onBrokenLinePointClick(int position, BrokenLineDimension dimension, List<String> xData) {
                Toast.makeText(MainActivity.this, xData.get(position) + dimension.remark +"." + dimension.mDatasList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onXLinePointClick(int position, List<BrokenLineDimension> data, List<String> xData) {
                String msg = "";
                for (BrokenLineDimension dimension : data) {
                    String message = dimension.remark + ":"+ dimension.mDatasList.get(position) +",";
                    msg += message;
                }
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
