package com.solarexsoft.solarexeventbusdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 *    CreatAt: 16:50/2020-01-14
 *    Desc:
 * </pre>
 */

public class BottomFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_bottom,container,false);
        tv = view.findViewById(R.id.tv);
        return view;
    }

    @SolarexSubscribe(threadMode = SolarexThreadMode.POSTING)
    private void onPostEvent(PostEvent event) {
        String log = "post event from post thread = " + event.threadName + ",current thread = " + Thread.currentThread().getName();
        Log.d(MainActivity.TAG, log);
        SolarexEventBus.getDefault().post(new BottomEvent(log));
    }
    @SolarexSubscribe(threadMode = SolarexThreadMode.MAIN)
    private void onMainEvent(MainEvent event) {
        String log = "main event from post thread = " + event.threadName + ",current thread = " + Thread.currentThread().getName();
        Log.d(MainActivity.TAG, log);
        tv.setText("main event from top frament," + log);
        SolarexEventBus.getDefault().post(new BottomEvent(log));
    }

    @SolarexSubscribe(threadMode = SolarexThreadMode.ASYNC)
    private void onAsyncEvent(AsyncEvent event) {
        String log = "async event from post thread = " + event.threadName + ",current thread = " + Thread.currentThread().getName();
        Log.d(MainActivity.TAG, log);
        SolarexEventBus.getDefault().post(new BottomEvent(log));
    }
}
