package com.megaz.dosomewangthings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NameActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Toast toast;
    private RelativeLayout layout_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        layout_root = (RelativeLayout)findViewById(R.id.name_root);
        Button btn_set = (Button)findViewById(R.id.btn_set);
        EditText edtx_boy_name = (EditText)findViewById(R.id.edtx_boy_name);
        EditText edtx_girl_name = (EditText)findViewById(R.id.edtx_girl_name);
        sharedPreferences = getSharedPreferences("cfg", MODE_PRIVATE);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        editor = sharedPreferences.edit();
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String boy_name = edtx_boy_name.getText().toString();
                String girl_name = edtx_girl_name.getText().toString();
                if(boy_name.equals("")){
                    toast.setText("啊哦 还没有输入男汪人呢");
                    toast.show();
                }else if(girl_name.equals("")){
                    toast.setText("啊哦 还没有输入女汪人呢");
                    toast.show();
                }else if(boy_name.length()>3 || girl_name.length()>3){
                    toast.setText("汪人名字不要超过三个字喔");
                    toast.show();
                }else{
                    editor.putString("boy_name", boy_name);
                    editor.putString("girl_name", girl_name);
                    editor.apply();
                    toast.setText("汪人名字已保存");
                    toast.show();
                    finish();
                    overridePendingTransition(R.animator.fade_in,R.animator.fade_out);
                }
            }
        });
    }

}