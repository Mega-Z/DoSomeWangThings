package com.megaz.dosomewangthings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class ArrangeActivity extends BaseActivity {

    private WangDex wang_dex;
    private String string_dex;
    private WangTable wang_plantable;
    private String string_plantable;
    private final String dexfilename="WangDex.txt";
    private final String planfilename="WangPlan.txt";
    private int indexs[];
    private LinearLayout arrange_hint;
    private TextView choice0;
    private TextView choice1;
    private TextView choice2;
    private Button girl_choose;
    private Button boy_choose;
    private LinearLayout layout_success;
    private LinearLayout layout_fail;
    private TextView text_success;
    private TextView text_fail;
    private Button btn_success;
    private Button btn_fail;
    private Toast toast;
    private boolean girl_choosing=false;
    private boolean boy_choosing=false;
    private boolean girl_chose=false;
    private boolean boy_chose=false;
    private int thing_chose=-1;
    private int thing_chose_by_girl=-1;
    private int thing_chose_by_boy=-1;
    private String boy_name = "男的";
    private String girl_name = "女的";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        Button btn_renew = (Button) findViewById(R.id.btn_renew);
        choice0 = (TextView)findViewById(R.id.choice_0);
        choice1 = (TextView)findViewById(R.id.choice_1);
        choice2 = (TextView)findViewById(R.id.choice_2);
        arrange_hint = (LinearLayout)findViewById(R.id.arrange_hint);
        girl_choose = (Button)findViewById(R.id.girl_choose);
        boy_choose = (Button)findViewById(R.id.boy_choose);
        layout_success = (LinearLayout)findViewById(R.id.layout_success);
        layout_fail = (LinearLayout)findViewById(R.id.layout_fail);
        btn_success = (Button)findViewById(R.id.btn_success);
        btn_fail = (Button)findViewById(R.id.btn_fail);
        text_success = (TextView)findViewById(R.id.text_success);
        text_fail = (TextView)findViewById(R.id.text_fail);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        Bundle bundle = getIntent().getExtras();
        string_dex=bundle.getString("dex");
        string_plantable=bundle.getString("plan");
        wang_dex = new WangDex(string_dex);
        wang_plantable = new WangTable(string_plantable);
        sharedPreferences = getSharedPreferences("cfg",MODE_PRIVATE);
        if(BuildConfig.VERSION_CODE == 1){
            boy_name = "阿闻";
            girl_name = "阿绛";
        }else{
            boy_name = sharedPreferences.getString("boy_name","男的");
            girl_name = sharedPreferences.getString("girl_name","女的");
        }
        refresh();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        choice0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(girl_choosing&&thing_chose!=0){
                    thing_chose = 0;
                    choice0.setBackgroundResource(R.drawable.bg_thing_girl_choice);
                }else if(boy_choosing&&thing_chose!=0){
                    thing_chose = 0;
                    choice0.setBackgroundResource(R.drawable.bg_thing_boy_choice);
                }else if((girl_choosing||boy_choosing)&&thing_chose==0){
                    thing_chose = -1;
                    choice0.setBackgroundResource(R.drawable.bg_thing_choice);
                }
                choice1.setBackgroundResource(R.drawable.bg_thing_choice);
                choice2.setBackgroundResource(R.drawable.bg_thing_choice);
            }
        });
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(girl_choosing&&thing_chose!=1){
                    thing_chose = 1;
                    choice1.setBackgroundResource(R.drawable.bg_thing_girl_choice);
                }else if(boy_choosing&&thing_chose!=1){
                    thing_chose = 1;
                    choice1.setBackgroundResource(R.drawable.bg_thing_boy_choice);
                }else if((girl_choosing||boy_choosing)&&thing_chose==1){
                    thing_chose = -1;
                    choice1.setBackgroundResource(R.drawable.bg_thing_choice);
                }
                choice0.setBackgroundResource(R.drawable.bg_thing_choice);
                choice2.setBackgroundResource(R.drawable.bg_thing_choice);
            }
        });
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(girl_choosing&&thing_chose!=2){
                    thing_chose = 2;
                    choice2.setBackgroundResource(R.drawable.bg_thing_girl_choice);
                }else if(boy_choosing&&thing_chose!=2){
                    thing_chose = 2;
                    choice2.setBackgroundResource(R.drawable.bg_thing_boy_choice);
                }else if((girl_choosing||boy_choosing)&&thing_chose==2){
                    thing_chose = -1;
                    choice2.setBackgroundResource(R.drawable.bg_thing_choice);
                }
                choice1.setBackgroundResource(R.drawable.bg_thing_choice);
                choice0.setBackgroundResource(R.drawable.bg_thing_choice);
            }
        });
        girl_choose.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(boy_choosing){
                    toast.setText("欸 "+boy_name+"还在选呢");
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"欸 阿闻还在选呢",Toast.LENGTH_SHORT).show();
                }else if(!girl_choosing&&!girl_chose){
                    girl_choosing = true;
                    thing_chose_by_girl = -1;
                    toast.setText("请"+girl_name+"选一个汪事叭");
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"请阿绛选一个汪事叭",Toast.LENGTH_SHORT).show();
                    girl_choose.setText(girl_name+"选好了");
                }else if(girl_choosing&&!girl_chose&&thing_chose!=-1){
                    girl_choosing = false;
                    girl_chose = true;
                    thing_chose_by_girl = thing_chose;
                    thing_chose = -1;
                    choice0.setBackgroundResource(R.drawable.bg_thing_choice);
                    choice1.setBackgroundResource(R.drawable.bg_thing_choice);
                    choice2.setBackgroundResource(R.drawable.bg_thing_choice);
                    girl_choose.setVisibility(View.GONE);
                    if(boy_chose){
                        get_result();
                    }
                }else if(girl_choosing&&!girl_chose&&thing_chose==-1){
                    toast.setText("欸 "+girl_name+"还没有选呢");
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"欸 阿绛还没有选呢",Toast.LENGTH_SHORT).show();
                }
            }
        });
        boy_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(girl_choosing){
                    toast.setText("欸 "+girl_name+"还在选呢");
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"欸 阿绛还在选呢",Toast.LENGTH_SHORT).show();
                }else if(!boy_choosing&&!boy_chose){
                    boy_choosing = true;
                    thing_chose_by_boy = -1;
                    toast.setText("请"+boy_name+"选一个汪事叭");
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"请阿闻选一个汪事叭",Toast.LENGTH_SHORT).show();
                    boy_choose.setText(boy_name+"选好了");
                }else if(boy_choosing&&!boy_chose&&thing_chose!=-1){
                    boy_choosing = false;
                    boy_chose = true;
                    thing_chose_by_boy = thing_chose;
                    thing_chose = -1;
                    choice0.setBackgroundResource(R.drawable.bg_thing_choice);
                    choice1.setBackgroundResource(R.drawable.bg_thing_choice);
                    choice2.setBackgroundResource(R.drawable.bg_thing_choice);
                    boy_choose.setVisibility(View.GONE);
                    if(girl_chose){
                        get_result();
                    }
                }else if(boy_choosing&&!boy_chose&&thing_chose==-1){
                    toast.setText("欸 "+boy_name+"还没有选呢");
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"欸 阿闻还没有选呢",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_success.setVisibility(View.GONE);
                refresh();
            }
        });
        btn_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_fail.setVisibility(View.GONE);
                refresh();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void refresh(){
        if(wang_dex.WangThingsNum<3){
            arrange_hint.setVisibility(View.VISIBLE);
            choice0.setVisibility(View.GONE);
            choice1.setVisibility(View.GONE);
            choice2.setVisibility(View.GONE);
            girl_choose.setVisibility(View.GONE);
            boy_choose.setVisibility(View.GONE);
        }else{
            indexs=wang_dex.RandomThree();
            choice0.setText(wang_dex.GetWangThing(indexs[0]).thing);
            choice1.setText(wang_dex.GetWangThing(indexs[1]).thing);
            choice2.setText(wang_dex.GetWangThing(indexs[2]).thing);
            choice0.setBackgroundResource(R.drawable.bg_thing_choice);
            choice1.setBackgroundResource(R.drawable.bg_thing_choice);
            choice2.setBackgroundResource(R.drawable.bg_thing_choice);
            girl_choose.setVisibility(View.VISIBLE);
            girl_choose.setText(girl_name+"选一个");
            boy_choose.setVisibility(View.VISIBLE);
            boy_choose.setText(boy_name+"选一个");
        }
        thing_chose=-1;
        thing_chose_by_boy=-1;
        thing_chose_by_girl=-1;
        girl_choosing=false;
        boy_choosing=false;
        girl_chose=false;
        boy_chose=false;
    }
    @SuppressLint("SetTextI18n")
    private void get_result(){
        if(thing_chose_by_boy==thing_chose_by_girl&&thing_chose_by_girl!=-1){ // success
            text_success.setText(boy_name+"和"+girl_name+"的默契使得汪事成功安排上了耶");
            layout_success.setVisibility(View.VISIBLE);
            if(get_chose(thing_chose_by_girl)!=null)
                get_chose(thing_chose_by_girl).setBackgroundResource(R.drawable.bg_thing_boyngirl_choice);
            wang_plantable.AddWangPlan(indexs[thing_chose_by_girl]);
            save(wang_plantable.TableToString(),planfilename);
        }else{ // fail
            if(get_chose(thing_chose_by_boy)!=null)
                get_chose(thing_chose_by_boy).setBackgroundResource(R.drawable.bg_thing_boy_choice);
            if(get_chose(thing_chose_by_girl)!=null)
                get_chose(thing_chose_by_girl).setBackgroundResource(R.drawable.bg_thing_girl_choice);
            text_fail.setText(boy_name+"和"+girl_name+"选的不一样汪事没有安排上欸");
            layout_fail.setVisibility(View.VISIBLE);
        }
    }
    private TextView get_chose(int index){
        switch (index){
            case 0:
                return choice0;
            case 1:
                return choice1;
            case 2:
                return choice2;
            default:
                return null;
        }
    }
}