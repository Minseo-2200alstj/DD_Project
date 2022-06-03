package com.example.dd_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

/*
public class SingerItem extends AppCompatActivity {
    //아이템에 넣을 속성 정의

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singer_item);
    }
}
 */

// 디데이 항목 정보 담기
public class SingerItem {

    String dTitle;
    int dDate_Year;
    int dDate_Month;
    int dDate_Day;
    int dResult;
    //private static int로 하면 additem할때 모든 정보가 하나로 덮어씌워지는 오류가 생김




    public static ArrayList<String> list_title = new ArrayList<String>();
    public static ArrayList<String> list_Date_Year = new ArrayList<String>();
    public static ArrayList<String> list_Date_Month = new ArrayList<String>();
    public static ArrayList<String> list_Date_Day = new ArrayList<String>();



    // 생성
    public SingerItem(String dTitle, int dDate_Year, int dDate_Month, int dDate_Day) {
        this.dTitle = dTitle;
        this.dDate_Year = dDate_Year;
        this.dDate_Month = dDate_Month;
        this.dDate_Day = dDate_Day;
    }

    // Generate > Getter and Setter. 접근 함수들
    public String getDTitle() {
        return dTitle;
    }

    public void setDTitle(String dTitle) {
        this.dTitle = dTitle;
    }

    ///
    public int getDDateYear() {
        return dDate_Year;
    }

    public void setDDateYear(int dDate_Year) {
        this.dDate_Year = dDate_Year;
    }


    public int getDDateMonth() {
        return dDate_Month;
    }

    public void setDDateMonth(int dDate_Month) {
        this.dDate_Month = dDate_Month;
    }

    public int getDDateDay() {
        return dDate_Day;
    }

    public void setDDateDay(int dDate_Day) {
        this.dDate_Day = dDate_Day;
    }

    ///


    /*
    public void deleteArray (int i,
                             String title, int year, int month, int day){

        // 배열의 마지막 인덱스부터 우리가 바꾸고 싶은 인덱스 전까지 반복문
        for(int j = list_title.length-1 ; j > i ; j--){
            list_title[j] = list_title[j+1];
            list_Date_Year[j] = list_Date_Year[j-1];
            list_Date_Month[j] = list_Date_Year[j-1];
            list_Date_Day[j] = list_Date_Year[j-1];
        }

        list_title[i] = title;
        list_Date_Year[i] = year;
        list_Date_Month[i] = month;
        list_Date_Day[i] = day;
    }
*/

/*
    public int getDcode() {
        return dCode;
    }

    public void setDCode(int dCode) {
        this.dCode = dCode;
    }
 */

    // Generate > toString() : 아이템을 문자열로 출력


    @Override
    public String toString() {
        return "SingerItem{" +
                "dTitle='" + dTitle + '\'' +
                ", dDate='" + dDate_Year + '년' + dDate_Month + '월' + dDate_Day + '일' +
               '}';
    }
}





