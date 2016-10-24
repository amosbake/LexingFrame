package com.lexing.lexingframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lexing.common.utils.L;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.json("{\n" +
                "  \"name\":\"yanhao\",\"sex\":1\n" +
                "}");
        L.v("verb");
        L.d("debug");
        L.a("assert");
        L.i("info");
        L.e("error");

    }

}
