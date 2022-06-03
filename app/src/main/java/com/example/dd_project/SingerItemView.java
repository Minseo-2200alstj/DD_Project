package com.example.dd_project;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SingerItemView extends LinearLayout {
    //아이템을 위한 뷰 정의

    TextView itemTitle;
    TextView itemDDate_Year;
    TextView itemDDate_Month;
    TextView itemDDate_Day;
    TextView itemResult;

    // Generate > Constructor

    public SingerItemView(Context context) {
        super(context);

        init(context);
    }

    public SingerItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    // singer_item.xml을 inflation(메모리객체화)해서 붙여줌
    //단말이 켜졌을때 기본적으로 백그라운드에서 실행시키는 시스템 서비스 사용
    private void init(Context context) {
        //시스템 서비스 참조 해줌
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.singer_item, this, true);

        itemTitle = (TextView) findViewById(R.id.itemTitle);
        itemDDate_Year = (TextView) findViewById(R.id.itemDDate_Year);
        itemDDate_Month = (TextView) findViewById(R.id.itemDDate_Month);
        itemDDate_Day = (TextView) findViewById(R.id.itemDDate_Day);
        itemResult = (TextView) findViewById(R.id.itemResult);
    }

    public void setDTitle(String dTitle) {
        itemTitle.setText(dTitle);
    }

    public void setDDate_Year(int dDate_Year) {
        itemDDate_Year.setText( Integer.toString(dDate_Year) );
    }

    public void setDDate_Month(int dDate_Month) {
        itemDDate_Month.setText( Integer.toString(dDate_Month) );
    }

    public void setDDate_Day(int dDate_Day) {
        itemDDate_Day.setText( Integer.toString(dDate_Day) );
    }

    public void setResult(String result) {
        itemResult.setText(result);
    }

}
