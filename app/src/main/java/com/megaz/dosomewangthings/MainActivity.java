package com.megaz.dosomewangthings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity {

    private TextView title;
    private Button btn0;
    private Button btn1;
    private Button btn2;
    private Button btnc;
    private ImageView main_img;
    private ImageView main_bg_gif;
    private final String dexfilename="WangDex.txt";
    private final String planfilename="WangPlan.txt";
    private final String photofilename="PhotoUri.txt";
    private String string_dex;
    private String example_dex="@一起去看一场汪电影#0@一起去恰火锅#0@一起去猫咖rua猫#0@在一只狗狗面前抱抱#0" +
            "@一起去超市获得大量零食并吃掉#0@一起学唱一首歌#0@持续接吻五分钟#0@一起做一个蛋糕并吃掉#0@持续一天" +
            "对话用啵代替所有动词#0@穿对方的衣服持续一天#0@为对方操作一次发型#0@一起度过一个晚上#0@一起想想这个app可以如何改进#0";
    private String string_plan;
    private String example_plan="/";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        title = (TextView)findViewById(R.id.text_title);
//        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/nana.ttf");
//        title.setTypeface(typeFace);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},0);
        sharedPreferences = getSharedPreferences("cfg", MODE_PRIVATE);
        btn0 = (Button)findViewById(R.id.btn_0);
        btn1 = (Button)findViewById(R.id.btn_1);
        btn2 = (Button)findViewById(R.id.btn_2);
        btnc = (Button)findViewById(R.id.btn_c);
        main_img = (ImageView)findViewById(R.id.main_img);
        main_bg_gif = (ImageView)findViewById(R.id.main_bg_gif);
        Glide.with(this).load(R.drawable.sakura).into(main_bg_gif);

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(), CardActivity.class);//启动CardActivity
                Bundle bundle = new Bundle();
                bundle.putString("plan",string_plan);
                bundle.putString("dex",string_dex);
                it.putExtras(bundle);
                startActivity(it);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(), ArrangeActivity.class);//启动CardActivity
                Bundle bundle = new Bundle();
                bundle.putString("plan",string_plan);
                bundle.putString("dex",string_dex);
                it.putExtras(bundle);
                startActivity(it);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(), DexActivity.class);//启动DexActivity
                Bundle bundle = new Bundle();
                bundle.putString("plan",string_plan);
                bundle.putString("dex",string_dex);
                it.putExtras(bundle);
                startActivity(it);
            }
        });
        btnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(), ConfigActivity.class);//启动ConfigActivity
                Bundle bundle = new Bundle();
                bundle.putString("plan",string_plan);
                bundle.putString("dex",string_dex);
                it.putExtras(bundle);
                startActivity(it);
            }
        });
        main_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_PICK);
                it.setType("image/*");
                startActivityForResult(it,1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(read(dexfilename)==null){
            string_dex = example_dex;
        }else{
            string_dex = read(dexfilename);
        }
        if(read(planfilename)==null){
            string_plan = example_plan;
        }else{
            string_plan = read(planfilename);
        }
        if(read(photofilename)==null){
            main_img.setImageResource(R.drawable.futari);
        }else{
            if(read(photofilename).equals("")){
                main_img.setImageResource(R.drawable.futari);
            }else{
                Uri uri = Uri.parse(read(photofilename));
                Glide.with(this).load(uri).into(main_img);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//如果结果码等于RESULT_OK
            if (requestCode == 1) {
                //String result = data.getExtras().getString("result");
                Uri uri = data.getData();
                save(uri.toString(),photofilename);
                Glide.with(this).load(uri).into(main_img);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(BuildConfig.VERSION_CODE != 1 && !(sharedPreferences.contains("boy_name")&&sharedPreferences.contains("girl_name"))){
                        Intent it=new Intent(getApplicationContext(), NameActivity.class);//启动NameActivity
                        startActivity(it);
                    }
                } else {
                    finish();
                }
                break;
            }
        }
    }
}