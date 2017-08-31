package jp.chamaecyparis.maximam_calculator;

import android.content.res.AssetManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.BufferedWriter;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText et;
    BufferedReader res;
    BufferedWriter cmd;
    boolean f;
    boolean alt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            tv = (TextView) findViewById(R.id.textView);
            et = (EditText) findViewById(R.id.editText);
            f=true;
            alt=false;
            maximainit();
            buttoninit();
            tv.setText("Show result here.");
            et.setText("Type formula here.");
        } catch (Exception e) {tv.setText("Error;");}
    }

    private void maximainit() {
        // assetsから読み込み、出力する
        String fileName="";
        String maxpath="";
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (Build.CPU_ABI.equals("x86")||Build.CPU_ABI.equals("x86_64")){
                fileName = "maxima.x86";
            }else{
                fileName = "maxima";
            }
        }else{
            if(Build.SUPPORTED_ABIS[0].equals("x86")||Build.SUPPORTED_ABIS[0].equals("x86_64")){
                fileName = "maxima.x86.pie";
            }else{
                fileName = "maxima.pie";
            }
        }
        AssetManager as = getResources().getAssets();
        maxpath=getFilesDir().toString()+"/"+fileName;
        try {
            if(!(new File(maxpath).exists())) {
                Log.d("copy",fileName);
                ZipInputStream zis = new ZipInputStream(as.open(fileName + ".zip"));
                ZipEntry ze = zis.getNextEntry();

                if (ze != null) {
                    String path = getFilesDir().toString() + "/" + ze.getName();
                    FileOutputStream fos = new FileOutputStream(path, false);
                    byte[] buf = new byte[1024];
                    int size = 0;

                    while ((size = zis.read(buf, 0, buf.length)) > -1) {
                        fos.write(buf, 0, size);
                    }
                    fos.close();
                    zis.closeEntry();
                }
                zis.close();
            }else{Log.d("copy","File exist");}

            Log.d("init","chmod start");
            ProcessBuilder builder = new ProcessBuilder("/system/bin/chmod", "744",maxpath);
            Process maxpro = builder.start();
            maxpro.waitFor();
            Log.d("init","chmod end");
            builder=new ProcessBuilder(maxpath,"--very-quiet");
            maxpro=builder.start();
            Log.d("init","set");
            cmd=new BufferedWriter(new OutputStreamWriter(maxpro.getOutputStream()));
            res=new BufferedReader(new InputStreamReader(maxpro.getInputStream()));
            Log.d("init","progress");
            cmd.write("display2d:false;\n");
            cmd.flush();
            res.readLine();
            Log.d("init","end");
        }catch(Exception e){tv.setText("Error");}
    }

    private void maximacmd(String str) {
        try{
            cmd.write(replacer(str)+";\n");
            Log.d("maxima",str);
            cmd.flush();
            tv.setText(res.readLine().replace("%pi","π"));
        } catch (Exception e) {
            tv.setText("Error");
        }
    }

    private String replacer(String str){
        return str.replace("Ans","_").replace("^","**").replace("π","%pi");
    }

    View.OnClickListener buttonNumberListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(f){
                f=false;tv.setText("");
                et.setText("");
            }
            Button button=(Button) view;
            et.getText().insert(et.getSelectionStart(),button.getText());
        }
    };

    View.OnClickListener buttonNumberListenerParenthesis= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(f){
                f=false;tv.setText("");
                et.setText("");
            }
            Button button=(Button) view;
            et.getText().insert(et.getSelectionStart(),button.getText()+"(");
        }
    };

    private void buttoninit(){
        findViewById(R.id.button_1).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_3).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_4).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_5).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_6).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_7).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_8).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_9).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_0).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_dot).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_add).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_multiply).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_divide).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_subtract).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_Right_Parenthesis).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_Left_Parenthesis).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_square).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_answer).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_pi).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_x).setOnClickListener(buttonNumberListener);

        findViewById(R.id.button_sin).setOnClickListener(buttonNumberListenerParenthesis);
        findViewById(R.id.button_cos).setOnClickListener(buttonNumberListenerParenthesis);
        findViewById(R.id.button_tan).setOnClickListener(buttonNumberListenerParenthesis);
        findViewById(R.id.button_ln).setOnClickListener(buttonNumberListenerParenthesis);
        findViewById(R.id.button_log).setOnClickListener(buttonNumberListenerParenthesis);
        findViewById(R.id.button_solve).setOnClickListener(buttonNumberListenerParenthesis);
        findViewById(R.id.button_sqrt).setOnClickListener(buttonNumberListenerParenthesis);

        findViewById(R.id.button_v).setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View view){
                                                               if(!alt)maximacmd("bfloat("+tv.getText().toString()+")");
                                                               else maximacmd("fpprec:"+et.getText().toString());
                                                           }
                                                       }
        );

        findViewById(R.id.button_c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("");
                et.setText("");

            }
        });

        findViewById(R.id.button_equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximacmd(et.getText().toString());
            }
        });

        findViewById(R.id.button_left_cursor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getSelectionStart()!=0)et.setSelection(et.getSelectionStart()-1);
            }
        });

        findViewById(R.id.button_right_coursor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getSelectionStart()!=et.getText().length())et.setSelection(et.getSelectionStart()+1);
            }
        });

        findViewById(R.id.button_back_space).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getSelectionStart()!=0)et.getText().delete(et.getSelectionStart()-1,et.getSelectionStart());
            }
        });

        findViewById(R.id.button_alt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alt=!alt;
                if(alt){
                    ((Button)findViewById(R.id.button_sin)).setText("asin");
                    ((Button)findViewById(R.id.button_cos)).setText("acos");
                    ((Button)findViewById(R.id.button_tan)).setText("atan");
                    ((Button)findViewById(R.id.button_v)).setText("prec");
                    ((Button)findViewById(R.id.button_ln)).setText("exp");
                }else{
                    ((Button)findViewById(R.id.button_sin)).setText("sin");
                    ((Button)findViewById(R.id.button_cos)).setText("cos");
                    ((Button)findViewById(R.id.button_tan)).setText("tan");
                    ((Button)findViewById(R.id.button_v)).setText("v");
                    ((Button)findViewById(R.id.button_ln)).setText("ln");
                }
            }
        });

    }

}
