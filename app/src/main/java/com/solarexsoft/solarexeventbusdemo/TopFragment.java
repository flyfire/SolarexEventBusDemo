package com.solarexsoft.solarexeventbusdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.solarexsoft.solarexeventbus.SolarexEventBus;
import com.solarexsoft.solarexeventbus.SolarexSubscribe;
import com.solarexsoft.solarexeventbus.SolarexThreadMode;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 16:29/2020-01-14
 *    Desc:
 * </pre>
 */

public class TopFragment extends Fragment implements View.OnClickListener {
    Button postMain,post,main,async;
    TextView tv;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SolarexEventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SolarexEventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        postMain = view.findViewById(R.id.post_main);
        post = view.findViewById(R.id.post);
        main = view.findViewById(R.id.main);
        async = view.findViewById(R.id.async);
        tv = view.findViewById(R.id.tv);
        postMain.setOnClickListener(this);
        post.setOnClickListener(this);
        main.setOnClickListener(this);
        async.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.post_main) {
            SolarexEventBus.getDefault().post(new PostEvent(Thread.currentThread().getName()));
        } else if (id == R.id.post) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SolarexEventBus.getDefault().post(new PostEvent(Thread.currentThread().getName()));
                }
            }).start();
        } else if (id == R.id.main) {
            SolarexEventBus.getDefault().post(new MainEvent(Thread.currentThread().getName()));
        } else if (id == R.id.async) {
            SolarexEventBus.getDefault().post(new AsyncEvent(Thread.currentThread().getName()));
        }
    }

    @SolarexSubscribe(threadMode = SolarexThreadMode.MAIN)
    private void onBottomEvent(BottomEvent event) {
        tv.setText(event.getLog());
    }
}
