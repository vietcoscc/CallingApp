package com.example.viet.callingapp.presenter;

import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

/**
 * Created by viet on 04/08/2017.
 */

public interface MainPresenter {

    void onStartCalling(SinchClient sinchClient, Call call, CallListener callListener);

}
