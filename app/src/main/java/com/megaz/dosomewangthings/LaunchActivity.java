package com.megaz.dosomewangthings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class LaunchActivity extends BaseActivity {
private boolean cancel = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_launch);
        if(BuildConfig.VERSION_CODE == 1){
            TextView text = (TextView)findViewById(R.id.launch_text_0);
            text.setVisibility(View.VISIBLE);
        }
        //TextView debug = (TextView)findViewById(R.id.tv_debug);
        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    sleep(2000);//使程序休眠五秒
                    Intent it=new Intent(getApplicationContext(),MainActivity.class);//启动MainActivity
                    if(!cancel)
                        startActivity(it);
                    else
                        Log.d("Finish","Before Main");
                    finish();//关闭当前活动
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancel = true;
    }
}