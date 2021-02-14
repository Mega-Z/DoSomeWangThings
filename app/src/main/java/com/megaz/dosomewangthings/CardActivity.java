package com.megaz.dosomewangthings;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class CardActivity extends BaseActivity {

    private LinearLayout plan_hint;
    private LinearLayout layout_plan;
    private WangDex wang_dex;
    private String string_dex;
    private WangTable wang_plantable;
    private String string_plantable;
    private int clear_id=-1;
    private boolean deling = false;
    private final String dexfilename="WangDex.txt";
    private final String planfilename="WangPlan.txt";
    private ProgressBar pb;
    private Button clearing;
    private Toast toast;
    private ObjectAnimator objectAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        Button btn_del = (Button)findViewById(R.id.btn_del);
        Button btn_deling = (Button)findViewById(R.id.btn_deling);
        plan_hint = (LinearLayout)findViewById(R.id.plan_hint);
        layout_plan = (LinearLayout)findViewById(R.id.layout_plan);
        pb = (ProgressBar)findViewById(R.id.progressbar);
        clearing = (Button) findViewById(R.id.btn_clearing);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        Bundle bundle = getIntent().getExtras();
        string_dex=bundle.getString("dex");
        string_plantable=bundle.getString("plan");
        wang_dex = new WangDex(string_dex);
        wang_plantable = new WangTable(string_plantable);
        update();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deling = true;
                btn_del.setVisibility(View.GONE);
                btn_deling.setVisibility(View.VISIBLE);
                toast.setText("点击删除汪安排");
                toast.show();
                //Toast.makeText(getApplicationContext(),"点击删除汪安排",Toast.LENGTH_SHORT).show();
            }
        });
        btn_deling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deling = false;
                btn_deling.setVisibility(View.GONE);
                btn_del.setVisibility(View.VISIBLE);
                toast.setText("结束删除模式");
                toast.show();
                //Toast.makeText(getApplicationContext(),"结束删除模式",Toast.LENGTH_SHORT).show();
            }
        });
        objectAnimator = ObjectAnimator.ofInt(pb,"progress",0,100);
        objectAnimator.setDuration(3000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d("end",String.valueOf(pb.getProgress()));
                pb.setVisibility(View.INVISIBLE);
                clearing.setVisibility(View.INVISIBLE);
                wang_dex.GetWangThing(wang_plantable.GetWangPlan(clear_id).wang_id).times++;
                wang_plantable.ClearWangPlan(clear_id);
                update();
                save(wang_plantable.TableToString(),planfilename);
                save(wang_dex.DexToString(),dexfilename);
            }
        });
    }
    private void update(){
        if(wang_plantable.WangingLenth==0){
            plan_hint.setVisibility(View.VISIBLE);
        }else{
            plan_hint.setVisibility(View.GONE);
        }
        layout_plan.removeAllViews();
        for(int i=0;i<wang_plantable.WangingLenth+wang_plantable.WangedLenth;i++){
            LinearLayout layout_wang_plan = new LinearLayout(this);
            layout_wang_plan.setId(i);
            layout_wang_plan.setOrientation(LinearLayout.VERTICAL);
            layout_wang_plan.setPadding(60,20,60,20);
            if(i<wang_plantable.WangingLenth){
                TextView tv_plan = new TextView(this);
                tv_plan.setText(wang_dex.GetWangThing(wang_plantable.GetWangPlan(i).wang_id).thing);
                tv_plan.setBackgroundColor(Color.rgb(0xFF,0xF8,0xE1));
                tv_plan.setTextSize(20);
                tv_plan.setPadding(10,20,10,20);
                layout_wang_plan.setOnTouchListener(new OnTouch_Clear());
                layout_wang_plan.addView(tv_plan);
            }else{
                TextView tv_plan = new TextView(this);
                tv_plan.setText(wang_dex.GetWangThing(wang_plantable.GetWangPlan(i).wang_id).thing);
                tv_plan.setBackgroundColor(Color.rgb(0xE8,0xF5,0xE9));
                tv_plan.setTextSize(20);
                tv_plan.setPadding(10,10,10,10);
                tv_plan.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                layout_wang_plan.addView(tv_plan);
                TextView tv_date = new TextView(this);
                tv_date.setText(wang_plantable.GetWangPlan(i).cleared+"  √");
                tv_date.setBackgroundColor(Color.rgb(0xA5,0xD6,0xA7));
                tv_date.setTextColor(Color.rgb(0xFF,0xFF,0xFF));
                tv_date.setTextSize(16);
                tv_date.setPadding(5,5,5,5);
                layout_wang_plan.setOnClickListener(new OnClick_del());
                layout_wang_plan.addView(tv_date);
            }
            layout_plan.addView(layout_wang_plan);
        }
    }
    private class OnTouch_Clear implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(!deling){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d("down","!");
                        clear_id=v.getId();
//                        LinearLayout layout_plan = (LinearLayout)findViewById(clear_id);
                        pb.setVisibility(View.VISIBLE);
                        clearing.setVisibility(View.VISIBLE);
                        objectAnimator.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("up","!");
                        objectAnimator.pause();
                        pb.setVisibility(View.INVISIBLE);
                        clearing.setVisibility(View.INVISIBLE);
                        break;
                }
            }
            else{
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        int del_id = v.getId();
                        wang_plantable.DeleteWangPlan(del_id);
                        update();
                        save(wang_plantable.TableToString(),planfilename);
                        save(wang_dex.DexToString(),dexfilename);
                    case MotionEvent.ACTION_UP:
                }
            }
            return true;
        }
    }
    private class OnClick_del implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(deling){
                int del_id = v.getId();
                if(del_id>=wang_plantable.WangingLenth){
                    wang_dex.GetWangThing(wang_plantable.GetWangPlan(del_id).wang_id).times--;
                }
                wang_plantable.DeleteWangPlan(del_id);
                update();
                save(wang_plantable.TableToString(),planfilename);
                save(wang_dex.DexToString(),dexfilename);
            }
        }
    }
}