package com.example.dd_project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //버튼을 누르면 ArrayList를 Json으로 변환해 저장하고 복원하는 코드
    private static final String SETTINGS_ITEM_JSON = "settings_item_json";

    public static final String SETTINGS_list_title = "settings_list_title";
    public static final String SETTINGS_list_Date_Year = "settings_list_date_year";
    public static final String SETTINGS_list_Date_Month = "settings_list_date_month";
    public static final String SETTINGS_list_Date_Day = "settings_list_date_day";

    //MainActivity.java에 listView 호출 + Adapter 만들기
    SingerAdapter adapter;

    ImageButton menuBtn;

    private Calendar calendar;
    private int tYear;  //오늘 날짜 변수 - 년월일
    private int tMonth;
    private int tDay;

    private int dYear;  //디데이 날짜변수 - 년월일
    private int dMonth;
    private int dDay;
    private String dResult = null;

    private long d;
    private long t;
    private long r;

    private int resultNumber = 0;

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("DD"); //상단 표시


        menuBtn = (ImageButton) findViewById(R.id.menuBtn);
        registerForContextMenu(menuBtn);  //컨텍스트 메뉴

        //리스트 뷰에 어뎁터로 만든 데이터 넣어주기
        listView = (ListView) findViewById(R.id.listView);

        // 어댑터 안에 데이터 담기 준비
        adapter = new SingerAdapter();

        //Json -> ArrayList로 변환
        ArrayList<String> list_title = getStringArrayPref(SETTINGS_list_title);
        ArrayList<String> list_year = getStringArrayPref(SETTINGS_list_Date_Year);
        ArrayList<String> list_month = getStringArrayPref(SETTINGS_list_Date_Month);
        ArrayList<String> list_day = getStringArrayPref(SETTINGS_list_Date_Day);


        todayDate(); //오늘 날짜 입력


        for (int i = 0; i < list_title.size(); i++) {

            adapter.addItem(new SingerItem(
                    list_title.get(i), Integer.parseInt(list_year.get(i)),
                    Integer.parseInt(list_month.get(i)), Integer.parseInt(list_day.get(i))));
        }

        // 리스트 뷰에 데이터가 다 셋팅된 어댑터를 넣어줌
        listView.setAdapter(adapter);

    }

    //메뉴--------------------------------------
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.newItem:
                //페이지 전환 - 생성페이지로
                Intent intent = new Intent(MainActivity.this,
                        CreateDDayActivity.class);
                startActivity(intent);

                break;

            case R.id.clearItem:
                //디데이 정보 지우기 & Preferences 수정
                //Preferences 업데이트
                SingerItem.list_title.clear();
                SingerItem.list_Date_Year.clear();
                SingerItem.list_Date_Month.clear();
                SingerItem.list_Date_Day.clear();

                setStringArrayPref(SETTINGS_list_title, SingerItem.list_title);
                setStringArrayPref(SETTINGS_list_Date_Year, SingerItem.list_Date_Year);
                setStringArrayPref(SETTINGS_list_Date_Month, SingerItem.list_Date_Month);
                setStringArrayPref(SETTINGS_list_Date_Day, SingerItem.list_Date_Day);

                adapter.items.clear();

                // 리스트 뷰에 데이터가 다 셋팅된 어댑터를 넣어줌
                listView.setAdapter(adapter);

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //어뎁터 정의-----------------------------------------------------

    class SingerAdapter extends BaseAdapter {
        //데이터가 들어갈 공간
        ArrayList<SingerItem> items = new ArrayList<SingerItem>();


        // Generate > implement methods
        @Override
        public int getCount() {
            return items.size();
        } //아이템 리스트 길이 반환

        public void addItem(SingerItem item) {
            items.add(item);
        }  //아이템 리스트 항목 추가

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }   //아이템 리스트 번호 받아옴

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        //Singer_item.xml 리턴
        public View getView(int position, View convertView, ViewGroup parent) {
            //어뎁터가 데이터가 관리하고 뷰도 만들수 있게 함
            // 뷰 객체 재사용가능하 만듦
            //리스트뷰에 출력
            //화면에 안보여 지게되는것은 다시 재사용하게 되면서 new로 만들필요 없다.
            // 코드 재사용 convertView 사용


            //오늘 날짜 계산
            todayDate();


            SingerItemView view = null;

            if (convertView == null) {//convertView 가 null 이면
                view = new SingerItemView(getApplicationContext());
            } else {//이전에 사용했던 데이터 singeritemview로 캐스팅해서 재사용
                view = (SingerItemView) convertView;
            }
            //위에서 정의한 arraylist 에서 하나하나의 데이터를 가져와서 String item 객체에 담음
            SingerItem item = items.get(position);

            //디데이 계산
            dResult = ddayCal(item.getDDateYear(), item.getDDateMonth(), item.getDDateDay());


            // SingerItemView 클래스에 정의한 set 메서드에 데이터를 담아서 리턴
            view.setDTitle(item.getDTitle());
            view.setDDate_Year(item.getDDateYear());
            view.setDDate_Month(item.getDDateMonth());
            view.setDDate_Day(item.getDDateDay());
            view.setResult(dResult);

            ///


            return view;
        }
    }


    //dday 계산

    private String ddayCal(int dYear, int dMonth, int dDay) {
        String result = "";

        todayDate(); //오늘 날짜 불러오기
        calendar.set(tYear, tMonth + 1, tDay);

        Calendar dCalendar = Calendar.getInstance();   //디데이 날짜 불러오기
        dCalendar.set(dYear, dMonth, dDay);


        t = calendar.getTimeInMillis();   //오늘날짜 변환 - 밀리타임
        d = dCalendar.getTimeInMillis();   //디데이 날짜 변환 - 밀리타임


        r = (d - t) / (24 * 60 * 60 * 1000);     //디데이 날짜에서 오늘 날짜를 뺸 값 변환 - 일 단위

        resultNumber = (int) r;

        /////////////////////////////
        result = ddayResult(dDay, tDay, resultNumber);


        return result;
    }

    //디데이 표시 함수
    private String ddayResult(int dDay, int tDay, int resultNumber) {
        String result = null;

        if (r == 0) {  //D-Day 문구가 당일과 하루 전, 두 상태 모두에서 표기되는 오류 수정
            if (dDay < tDay) {
                result = "D-1";
            } else {
                result = "D-Day";
            }
        } else if (resultNumber < 0) { //D-Day 날짜가 밀리는 오류 수정
            int absR = Math.abs(resultNumber) + 1; //절댓값+1
            result = "D+" + absR;
        } else if (resultNumber > 0) {

            result = "D-" + resultNumber;
        }
        //디데이 표기
        //목표 날짜보다 이전-> D-n 사용 / 이후-> D+n 사용

        return result;
    }

    private void todayDate() {
        calendar = Calendar.getInstance();   //현재날짜 불러오기
        tYear = calendar.get(Calendar.YEAR);
        tMonth = calendar.get(Calendar.MONTH);
        tDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //여기서부턴 Preferences 설정 ------------------------------------------------------------


    private void addArrayList(String fileName, ArrayList<String> list, String value) {
        list.add(value);
        //setStringArrayPref(SETTINGS_ITEM_JSON, list);
        setStringArrayPref(fileName, list);
    }


    //SharedPreferences에 ArrayList를 Json으로 변환하여 String 저장하는 코드
    //저장된 String은 Json형식이기 때문에 ArrayList로 변환이 가능
    private void setStringArrayPref(/*Context context, */String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this/*context*/);
        SharedPreferences.Editor editor = prefs.edit();   //Editor를 preferences에 쓰겠다고 연결

        JSONArray a = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i)); //JSon에 리스트 입력
        }

        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }

        editor.apply();  //commit or apply로 저장
    }


    //SharedPreferences에서 Json형식의 String을 가져와서 다시 ArrayList로 변환하는 코드
    private ArrayList<String> getStringArrayPref(/*Context context,*/ String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this/*context*/);
        String json = prefs.getString(key, null);  //Editor를 preferences에 쓰겠다고 연결

        ArrayList<String> urls = new ArrayList<String>();

        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);

                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    //SharedPreferences 클리어
    private void clearSharedPreferences(String fileName) {
        SharedPreferences pref = getSharedPreferences(fileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.clear();

        editor.commit();
    }

}