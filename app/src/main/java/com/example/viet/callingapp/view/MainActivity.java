package com.example.viet.callingapp.view;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viet.callingapp.R;
import com.example.viet.callingapp.presenter.MainPresenterImp;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

    public static final String APP_KEY = "545a1b7c-e7e5-4bf4-aefe-19e19c966950";
    public static final String APP_SEC = "I+/gV9nhzk+WJNULALcIVA==";
    public static final String APP_HOST = "sandbox.sinch.com";

    private SinchClient mSinchClient;
    private Call mCall;
    private TextView tvState;
    private Button btnCall;
    private MainPresenterImp mMainPresenterImp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPresenter();
        initViews();
    }

    private void initPresenter() {
        mMainPresenterImp = new MainPresenterImp(this);
    }

    private void initViews() {
        tvState = (TextView) findViewById(R.id.tvState);
        btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPresenterImp.onStartCalling(mSinchClient, mCall, new SinchCallListener());
            }
        });
    }

    @Override
    public void diaplayInfoDialog() {
        final EditText edt = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(edt);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                initSinchClient(edt.getText().toString());
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void initSinchClient(String userId) {
        mSinchClient = Sinch.getSinchClientBuilder()
                .context(MainActivity.this)
                .userId(userId)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SEC)
                .environmentHost(APP_HOST)
                .build();
        mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
        mSinchClient.startListeningOnActiveConnection();
        mSinchClient.setSupportCalling(true);
        mSinchClient.start();
    }

    @Override
    public void displayState(String state) {
        btnCall.setText(state);
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            Toast.makeText(MainActivity.this, "onCallEnded", Toast.LENGTH_SHORT).show();
            tvState.setText("");
            btnCall.setText("CALL");
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            tvState.setText("connected");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            Toast.makeText(MainActivity.this, "onCallEstablished", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            tvState.setText("ringing");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            Toast.makeText(MainActivity.this, "onCallProgressing", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {

        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            Toast.makeText(MainActivity.this, "onIncomingCall", Toast.LENGTH_SHORT).show();
            mCall = incomingCall;
            mCall.answer();
            mCall.addCallListener(new SinchCallListener());
            btnCall.setText("Hang Up");
        }
    }

}
