package com.bill.localmaventest;

import androidx.appcompat.app.AppCompatActivity;
import me.drakeet.support.toast.ToastCompat;

import android.os.Bundle;
import android.view.View;

import com.bill.mylibrary.TestUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestUtils.toast(getApplicationContext(), "Hello");

                // cn.bill.library:mylib:1.0.0 中传递过来的
//                ToastCompat.makeText(getApplicationContext(), "Hello", 0).show();
            }
        });
    }
}