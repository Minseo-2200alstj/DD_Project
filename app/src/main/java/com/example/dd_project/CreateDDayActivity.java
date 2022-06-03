package com.example.dd_project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


@SuppressWarnings("deprecation")
public class CreateDDayActivity extends AppCompatActivity {

    private EditText ddayTitleText;

    private TextView ddayText;
    private TextView todayText;
    private TextView resultText;
    private ImageButton dialogBtn;
    private Button createBtn;
    private Button cancleBtn;


    private int tYear;  //오늘 날짜 변수 - 년월일
    private int tMonth;
    private int tDay;

    private int dYear;  //디데이 날짜변수 - 년월일
    private int dMonth;
    private int dDay;

    private long d;
    private long t;
    private long r;

    private int resultNumber=0;

    static final  int DATE_DIALOG_ID=0;

    public int dCalCode = 0; //0-> 아무것도 안 누른 상태, 1-> 날짜 설정 상태

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dday);
        setTitle("디데이 생성"); //상단 표시



        ddayTitleText = (EditText)findViewById((R.id.ddayTitle));

        ddayText=(TextView)findViewById((R.id.dday));
        todayText=(TextView)findViewById((R.id.today));
        resultText=(TextView)findViewById((R.id.result));
        dialogBtn =(ImageButton) findViewById((R.id.dialogBtn));
        createBtn =(Button) findViewById((R.id.createBtn));
        cancleBtn =(Button) findViewById((R.id.cancleBtn));



        Calendar calendar = Calendar.getInstance();   //현재날짜 불러오기
        tYear = calendar.get(Calendar.YEAR);
        tMonth = calendar.get(Calendar.MONTH);
        tDay = calendar.get(Calendar.DAY_OF_MONTH);



        if(dCalCode==0) { //날짜 미입력시 자동으로 오늘날짜에 맞춰짐
            dYear = tYear;
            dMonth = tMonth;
            dDay = tDay;
        }

        Calendar dCalendar = Calendar.getInstance();   //디데이 날짜 불러오기
        dCalendar.set(dYear, dMonth, dDay);



        t = calendar.getTimeInMillis();   //오늘날짜 변환 - 밀리타임
        d = dCalendar.getTimeInMillis();   //디데이 날짜 변환 - 밀리타임


        r=(d-t)/(24*60*60*1000);     //디데이 날짜에서 오늘 날짜를 뺸 값 변환 - 일 단위

        resultNumber = (int)r-1;
        updateDisplay();

        
        
        //이벤트 처리 함수---------------------------


        //날짜 입력 버튼 - 다이얼로그 띄우기
        dialogBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v){
                showDialog(0);
            }
        });
        
        //creatButton 클릭시 디데이 저장
        createBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //디데이 정보 
                String eTitle = ddayTitleText.getText().toString();
                int eYear  = dYear;
                int eMonth  = dMonth + 1;
                int eDay  = dDay;

                //리스트 업데이트
                addArrayList(MainActivity.SETTINGS_list_title, SingerItem.list_title, eTitle);
                addArrayList(MainActivity.SETTINGS_list_Date_Year, SingerItem.list_Date_Year, Integer.toString(eYear));
                addArrayList(MainActivity.SETTINGS_list_Date_Month, SingerItem.list_Date_Month, Integer.toString(eMonth));
                addArrayList(MainActivity.SETTINGS_list_Date_Day, SingerItem.list_Date_Day, Integer.toString(eDay));


                //메인창으로 돌아가기
                Intent intent = new Intent(CreateDDayActivity.this,
                        MainActivity.class);
                startActivity(intent);

                dCalCode=0;
            }
        });

        //페이지 전환 - 생성페이지로
        cancleBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v){
                //메인창으로 돌아가기
                Intent intent = new Intent(CreateDDayActivity.this,
                        MainActivity.class);
                startActivity(intent);

                dCalCode=0;
            }
        });


    }

    private void updateDisplay(){

        todayText.setText(String.format("%d년 %d월 %d일", tYear, tMonth+1, tDay));
        ddayText.setText(String.format("%d년 %d월 %d일", dYear, dMonth+1, dDay));



        if(r==0) {  //D-Day 문구가 당일과 하루 전, 두 상태 모두에서 표기되는 오류 수정
            if(dDay<tDay){
                resultText.setText(String.format("D-1"));
            }else {
                resultText.setText(String.format("D-Day"));
            }
        }
        else if (resultNumber<0){ //D-Day 날짜가 밀리는 오류 수정
            int absR = Math.abs(resultNumber) + 1; //절댓값+1
            resultText.setText(String.format("D+%d", absR));
        }
        else if (resultNumber>0){

            resultText.setText(String.format("D-%d", resultNumber));
        }
        //디데이 표기
        //목표 날짜보다 이전-> D-n 사용 / 이후-> D+n 사용
    }

    private DatePickerDialog.OnDateSetListener dDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


            //DatePickerDialog 에서 날짜 받아오기
            dYear=year;
            dMonth=monthOfYear;
            dDay=dayOfMonth;


            //디데이 계산
            final Calendar dCalendar = Calendar.getInstance();
            dCalendar.set(dYear, dMonth, dDay);

            d=dCalendar.getTimeInMillis();
            r=(d-t)/(24*60*60*1000);


            resultNumber=(int)r;
            updateDisplay();

            dCalCode=1;

        }
    };

    @Override
    protected Dialog onCreateDialog (int id){
        if(id==DATE_DIALOG_ID){
            return new DatePickerDialog(this,dDateSetListener, tYear, tMonth, tDay);
        }
        return null;
    }




    //여기서부턴 Preferences 설정 ------------------------------------------------------------


    private void addArrayList(String fileName ,ArrayList<String> list, String value){
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
}

