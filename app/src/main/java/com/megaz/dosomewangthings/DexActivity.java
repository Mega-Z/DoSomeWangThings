package com.megaz.dosomewangthings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class DexActivity extends BaseActivity {

    private EditText edtx_athing;
    private EditText edtx_chg_thing;
    private LinearLayout dex_hint;
    private LinearLayout layout_add;
    private LinearLayout layout_dex;
    private LinearLayout layout_chg;
    private LinearLayout layout_del;
    private LinearLayout layout_root;
    private ScrollView layout_scroll_dex;
    private InputMethodManager Imm;
    private Toast toast;
    private WangDex wang_dex;
    private String string_dex;
    private WangTable wang_plantable;
    private String string_plantable;
    private boolean inputing = false;
    private boolean editing = false;
    private boolean chging = false;
    private boolean deling = false;
    private int editing_id = -1;
    private final String dexfilename="WangDex.txt";
    private final String planfilename="WangPlan.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dex);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        Button btn_add = (Button) findViewById(R.id.btn_add);
        dex_hint = (LinearLayout)findViewById(R.id.dex_hint);
        layout_add=(LinearLayout)findViewById(R.id.layout_add);
        Button btn_add_y = (Button) findViewById(R.id.btn_add_y);
        Button btn_add_n = (Button) findViewById(R.id.btn_add_n);
        layout_dex=(LinearLayout)findViewById(R.id.layout_dex);
        layout_chg = (LinearLayout)findViewById(R.id.layout_chg);
        layout_del = (LinearLayout)findViewById(R.id.layout_del);
        layout_root = (LinearLayout)findViewById(R.id.dex_root);
        layout_scroll_dex = (ScrollView)findViewById(R.id.layout_scroll_dex);
        Button btn_chg_y = (Button) findViewById(R.id.btn_chg_y);
        Button btn_chg_n = (Button) findViewById(R.id.btn_chg_n);
        Button btn_del_y = (Button) findViewById(R.id.btn_del_y);
        Button btn_del_n = (Button) findViewById(R.id.btn_del_n);
        edtx_athing=(EditText)findViewById(R.id.edtx_athing);
        edtx_chg_thing=(EditText)findViewById(R.id.edtx_chg_thing);
        Imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        Bundle bundle = getIntent().getExtras();
        string_dex=bundle.getString("dex");
        string_plantable=bundle.getString("plan");
        wang_dex = new WangDex(string_dex);
        wang_plantable = new WangTable(string_plantable);
        update();
        layout_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int screenHeight = layout_root.getRootView().getHeight();
                int myHeight = layout_root.getHeight();
                int heightDiff = screenHeight - myHeight;
                Log.e("Screen", "Height=" +screenHeight);
                Log.e("Layout", "Height=" +myHeight);
                if (heightDiff > 500) {
                    inputing = true;
                } else {
                    inputing = false;
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtx_athing.setText("");
                layout_add.setVisibility(View.VISIBLE);
                dex_hint.setVisibility(View.GONE);
                if(editing_id!=-1){
                    LinearLayout layout_wang_thing = (LinearLayout) findViewById(editing_id);
                    if(layout_chg.getVisibility()==View.GONE&&layout_del.getVisibility()==View.GONE)
                        layout_wang_thing.removeViewAt(2);
                    editing_id = -1;
                }
                layout_chg.setVisibility(View.GONE);
                layout_del.setVisibility(View.GONE);
                editing = false;
                chging = false;
                deling = false;
            }
        });
        btn_add_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_add.setVisibility(View.GONE);
                if(inputing)
                    Imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        btn_add_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_string = edtx_athing.getText().toString();
                if(input_string.equals("")){
                    toast.setText("啊哦 还没有输入呢");
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"啊哦 还没有输入呢",Toast.LENGTH_SHORT).show();
                }else if(input_string.length()>14) {
                    toast.setText("啊哦 这个汪事太长了");
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"啊哦 这个汪事太长了",Toast.LENGTH_LONG).show();
                }else{
                    wang_dex.AddWangThing(input_string);
                    save(wang_dex.DexToString(),dexfilename);
                    layout_add.setVisibility(View.GONE);
                    if(inputing)
                        Imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    update();
                }
            }
        });
        btn_chg_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_chg.setVisibility(View.GONE);
                editing = false;
                chging = false;
                editing_id = -1;
                if(inputing)
                    Imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        btn_chg_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_string = edtx_chg_thing.getText().toString();
                if(input_string.equals("")){
                    toast.setText("啊哦 还没有输入呢");
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"啊哦 还没有输入呢",Toast.LENGTH_SHORT).show();
                }else if(input_string.length()>14) {
                    toast.setText("啊哦 这个汪事太长了");
                    toast.show();
                    //Toast.makeText(getApplicationContext(),"啊哦 这个汪事太长了",Toast.LENGTH_LONG).show();
                }else{
                    wang_dex.GetWangThing(editing_id).thing = input_string;
                    save(wang_dex.DexToString(),dexfilename);
                    layout_chg.setVisibility(View.GONE);
                    editing = false;
                    chging = false;
                    editing_id = -1;
                    if(inputing)
                        Imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    update();
                }
            }
        });
        btn_del_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_del.setVisibility(View.GONE);
                editing = false;
                deling = false;
                editing_id = -1;
            }
        });
        btn_del_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wang_dex.DeleteWangThing(editing_id);
                wang_plantable.DelWangPlanByID(editing_id);
                save(wang_dex.DexToString(),dexfilename);
                save(wang_plantable.TableToString(),planfilename);
                layout_del.setVisibility(View.GONE);
                editing = false;
                deling = false;
                editing_id = -1;
                update();
            }
        });
    }

    private void update(){
        if(wang_dex.WangThingsNum==0){
            dex_hint.setVisibility(View.VISIBLE);
        }else{
            dex_hint.setVisibility(View.GONE);
        }
        layout_dex.removeAllViews();
        for(int i=0;i<wang_dex.WangThingsNum;i++){
            LinearLayout layout_wang_thing = new LinearLayout(this);
            layout_wang_thing.setId(i);
            layout_wang_thing.setOrientation(LinearLayout.VERTICAL);
            layout_wang_thing.setOnClickListener(new OnClick_Edit());
            layout_wang_thing.setPadding(24,24,24,20);
            layout_wang_thing.setBackgroundResource(R.drawable.bg_dex_item);
            TextView tv_thing = new TextView(this);
            TextView tv_times = new TextView(this);
            tv_thing.setText(wang_dex.GetWangThing(i).thing);
            tv_thing.setBackgroundColor(Color.rgb(0xE0,0xF7,0xFA));
            tv_thing.setTextSize(20);
            tv_thing.setPadding(10,10,10,10);
            tv_times.setText("汪次数："+ wang_dex.GetWangThing(i).times);
            tv_times.setBackgroundColor(Color.rgb(0x4D,0xD0,0xE1));
            tv_times.setTextColor(Color.rgb(0xFF,0xFF,0xFF));
            tv_times.setTextSize(16);
            tv_times.setPadding(5,5,5,5);
            layout_wang_thing.addView(tv_thing);
            layout_wang_thing.addView(tv_times);
            layout_dex.addView(layout_wang_thing);
        }
    }
    private class OnClick_Edit implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(!editing) {
                editing_id = v.getId();
                layout_add.setVisibility(View.GONE);
                LinearLayout layout_edit = new LinearLayout(DexActivity.this);
                LinearLayout layout_wang_thing = (LinearLayout) findViewById(editing_id);
                layout_edit.setOrientation(LinearLayout.HORIZONTAL);
                layout_edit.setPadding(10,10,10,10);
                Button btn_chg = new Button(DexActivity.this);
                btn_chg.setBackgroundResource(R.drawable.bg_edit_btn);
                btn_chg.setPadding(0,0,0,0);
                btn_chg.setText("编辑");
                btn_chg.setTextSize(20);
                btn_chg.setTextColor(Color.WHITE);
                Button btn_del = new Button(DexActivity.this);
                btn_del.setBackgroundResource(R.drawable.bg_edit_btn);
                btn_del.setPadding(0,0,0,0);
                btn_del.setX(40);
                btn_del.setText("删除");
                btn_del.setTextSize(20);
                btn_del.setTextColor(Color.WHITE);
                layout_edit.addView(btn_chg);
                layout_edit.addView(btn_del);
                btn_chg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtx_chg_thing.setText(wang_dex.GetWangThing(editing_id).thing);
                        layout_chg.setVisibility(View.VISIBLE);
                        layout_wang_thing.removeViewAt(2);
                        chging = true;
                    }
                });
                btn_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layout_del.setVisibility(View.VISIBLE);
                        layout_wang_thing.removeViewAt(2);
                        deling = true;
                    }
                });
                //Log.e("before adding", "y=" +(layout_wang_thing.getY()-layout_scroll_dex.getScrollY()));
                layout_wang_thing.addView(layout_edit);
                //Log.e("after adding", "y=" +(layout_wang_thing.getY()-layout_scroll_dex.getScrollY()));
                if((layout_wang_thing.getY()-layout_scroll_dex.getScrollY()-layout_root.getHeight()+500)>0)
                    layout_scroll_dex.scrollBy(0,(int)(layout_wang_thing.getY()-layout_scroll_dex.getScrollY()-layout_root.getHeight()+600));
                editing = true;
            }else if(!chging&&!deling){
                LinearLayout layout_edit = new LinearLayout(DexActivity.this);
                LinearLayout layout_wang_thing = (LinearLayout) findViewById(editing_id);
                layout_wang_thing.removeViewAt(2);
                editing_id = -1;
                editing = false;
            }
        }
    }
}