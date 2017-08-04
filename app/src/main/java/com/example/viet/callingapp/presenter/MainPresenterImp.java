package com.example.viet.callingapp.presenter;

import com.example.viet.callingapp.view.MainView;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;


/**
 * Created by viet on 04/08/2017.
 */

public class MainPresenterImp implements MainPresenter {

    private MainView mMainView;

    public MainPresenterImp(MainView mainView) {
        this.mMainView = mainView;
        mainView.diaplayInfoDialog();
    }

    @Override
    public void onStartCalling(SinchClient sinchClient, Call call, CallListener callListener) {
        if (sinchClient != null) {
            if (call == null) {
                call = sinchClient.getCallClient().callUser("b");
                call.addCallListener(callListener);
                mMainView.displayState("Hang Up");
            } else {
                call.hangup();
                call = null;
                mMainView.displayState("Call");
            }
        }
    }
}
