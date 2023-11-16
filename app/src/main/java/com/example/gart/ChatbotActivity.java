package com.example.gart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.Random;

public class ChatbotActivity extends AppCompatActivity {

    private EditText inputForm;
    private TextView result;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        inputForm = findViewById(R.id.editTextText_inputForm);
        result = findViewById(R.id.textView_result);
        send = findViewById(R.id.button_send);

        buttonAction();
    }

    private void buttonAction() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentData = inputForm.getText().toString();
                String resultData = null;
                //java 조건문인 if사용
                if("안녕".equals(currentData)) {
                    resultData = "이번달 공연과 전시중 무엇을 추천해 드릴까요";
                }else if("공연추천".equals(currentData)) {
                    resultData = Selectexhibition();
                }else if("전시 추천".equals(currentData)){
                    resultData = selectshow();

                }else {
                    resultData = "잘 모르겠습니다";
                }
                result.setText(resultData);
            }
        });

    }

    private String selectshow() {
        String[] showlist = {"1","2","3","4","5"};
        Random random = new Random();
        String select = showlist[random.nextInt(showlist.length)];
        return select;
    }

    private String Selectexhibition(){
        String[] exhibitionlist = {"a","b","c","d"};
        Random random = new Random();
        String select =  exhibitionlist[random.nextInt(exhibitionlist.length)];
        return select;
    }
}