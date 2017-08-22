package jp.chamaecyparis.maximam_calculator;

import android.content.res.AssetManager;
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


public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText et;
    final String[] cmd = {"/data/data/jp.chamaecyparis.maximam_calculator/files/maxima.x86.pie","--very-quiet",""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            tv = (TextView) findViewById(R.id.textView);
            et = (EditText) findViewById(R.id.editText);
            copy2Local("maxima.x86.pie");
            tv.setText("Show result here.");
            et.setText("Type formula here.");

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

            findViewById(R.id.button_v).setOnClickListener(new View.OnClickListener() {
                   @Override
                    public void onClick(View view){
                       maximacmd("float("+tv.getText().toString()+")");
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
        } catch (Exception e) {tv.setText("Error;");}
    }

    private void copy2Local(String fileName) {
        // assetsから読み込み、出力する
        AssetManager as = getResources().getAssets();
        try {
            InputStream input = as.open(fileName);
            FileOutputStream output = openFileOutput(fileName, Context.MODE_PRIVATE);
            int DEFAULT_BUFFER_SIZE = 1024 * 4;
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            output.close();
            input.close();
            ProcessBuilder builder = new ProcessBuilder("/system/bin/chmod", "744", cmd[0]);
            Process maxpro = builder.start();
            maxpro.waitFor();
        }catch(Exception e){tv.setText("Error");}
    }

    private void maximacmd(String str) {
        try{
            cmd[2]="--batch-string=display2d:false;"+str+";";
            ProcessBuilder builder = new ProcessBuilder(cmd);
            Process maxpro = builder.start();
            maxpro.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(maxpro.getInputStream()));
            String line="";
            for (int i = 0; i < 4; i++) {
                line = br.readLine();
                Log.d("maxima",line);
            }
            tv.setText(line.trim());
            br.close();
        } catch (IOException | InterruptedException e) {
            tv.setText("Error");
        }
    }

    View.OnClickListener buttonNumberListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button=(Button) view;
            et.append(button.getText());
        }
    };


}
