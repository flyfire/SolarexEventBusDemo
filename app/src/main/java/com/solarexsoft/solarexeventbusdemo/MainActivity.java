package com.solarexsoft.solarexeventbusdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Solarex";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.ll_content1, new TopFragment());
        fragmentTransaction.add(R.id.ll_content2, new BottomFragment());
        fragmentTransaction.commit();
    }
}
