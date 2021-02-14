package com.megaz.dosomewangthings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigActivity extends BaseActivity {
    private String string_dex;
    private String string_plantable;
    private Toast toast;
    private final String dexfilename="WangDex.txt";
    private final String planfilename="WangPlan.txt";
    private final String photofilename="PhotoUri.txt";
    private PopupWindow pop_chg_name;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        TextView config_1 = (TextView) findViewById(R.id.config_1);
        TextView config_2 = (TextView) findViewById(R.id.config_2);
        TextView config_3 = (TextView) findViewById(R.id.config_3);
        TextView config_4 = (TextView) findViewById(R.id.config_4);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        Bundle bundle = getIntent().getExtras();
        string_dex=bundle.getString("dex");
        string_plantable=bundle.getString("plan");
        sharedPreferences = getSharedPreferences("cfg", MODE_PRIVATE);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        config_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_sd(string_dex+" "+string_plantable,"WangData.txt");
                toast.setText("汪数据已导出");
                toast.show();
            }
        });
        config_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string_wang_data = read_sd("WangData.txt");
                if(string_wang_data==null){
                    toast.setText("汪数据不存在欸");
                }else{
                    String strings[] = string_wang_data.split(" ");
                    string_dex = strings[0];
                    string_plantable = strings[1];
                    save(string_dex,dexfilename);
                    save(string_plantable,planfilename);
                    toast.setText("汪数据已导入");
                }
                toast.show();
            }
        });
        config_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save("",photofilename);
                toast.setText("主页面汪照片已恢复默认");
                toast.show();
            }
        });
        config_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BuildConfig.VERSION_CODE == 1){
                    toast.setText("特别版不需要修改汪人名喔");
                    toast.show();
                }else{
                    View pop_view = getLayoutInflater().inflate(R.layout.layout_chg_name,null);
                    TextView name_chg_y = (TextView)pop_view.findViewById(R.id.name_chg_y);
                    TextView name_chg_n = (TextView)pop_view.findViewById(R.id.name_chg_n);
                    EditText name_chg_boy = (EditText)pop_view.findViewById(R.id.name_chg_boy);
                    EditText name_chg_girl = (EditText)pop_view.findViewById(R.id.name_chg_girl);
                    String boy_name = sharedPreferences.getString("boy_name","男的");
                    String girl_name = sharedPreferences.getString("girl_name","女的");
                    name_chg_boy.setText(boy_name);
                    name_chg_girl.setText(girl_name);
                    name_chg_n.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pop_chg_name.dismiss();
                        }
                    });
                    name_chg_y.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String new_boy_name = name_chg_boy.getText().toString();
                            String new_girl_name = name_chg_girl.getText().toString();
                            if(new_boy_name.equals("")){
                                toast.setText("啊哦 还没有输入男汪人呢");
                                toast.show();
                            }else if(new_girl_name.equals("")){
                                toast.setText("啊哦 还没有输入女汪人呢");
                                toast.show();
                            }else if(new_boy_name.length()>3 || new_girl_name.length()>3){
                                toast.setText("汪人名字不要超过三个字喔");
                                toast.show();
                            }else {
                                editor = sharedPreferences.edit();
                                editor.putString("boy_name", new_boy_name);
                                editor.putString("girl_name", new_girl_name);
                                editor.apply();
                                toast.setText("汪人名字已保存");
                                toast.show();
                                pop_chg_name.dismiss();
                            }
                        }
                    });
                    pop_chg_name = new PopupWindow(pop_view,config_4.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                    pop_chg_name.setOutsideTouchable(true);
                    pop_chg_name.setFocusable(true);
                    //pop_chg_name.showAtLocation(pop_view, 0,0,500);
                    pop_chg_name.showAsDropDown(config_4,0,-120);
                }
            }
        });
    }
    private void save_sd(String content, String filename){
        FileOutputStream fileOutputStream=null;
        try {
            File dir = new File(getExternalFilesDir(null).toString(),"WangData");
            //File dir = new File(Environment.getExternalStorageDirectory(),"DoSomeWangThings");
            if(!dir.exists()){
                boolean flag = dir.mkdirs();
                Log.e("success",String.valueOf(flag));
            }
            File file = new File(dir,filename);
            if(!file.exists()){
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String read_sd(String filename){
        FileInputStream fileInputStream=null;
        try {
            File file = new File(getExternalFilesDir(null).toString()+File.separator+"WangData",filename);
            fileInputStream = new FileInputStream(file);
            byte[] buff=new byte[1024];
            StringBuilder sb=new StringBuilder("");
            int len=0;
            while((len = fileInputStream.read(buff))>0){
                sb.append(new String(buff,0,len));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}