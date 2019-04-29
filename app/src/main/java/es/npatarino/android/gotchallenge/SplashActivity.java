package es.npatarino.android.gotchallenge;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import es.npatarino.android.gotchallenge.interfaces.IManagerEvents;
import es.npatarino.android.gotchallenge.models.GoTManager;

public class SplashActivity extends AppCompatActivity implements IManagerEvents
{
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = getBaseContext();
        GoTManager.getInstance(this).refreshData(this);
    }

    @Override
    public void onRecoveryDataFinishCallback() {
        goToHomeActivity(false);
    }

    @Override
    public void onRecoveryDataErrorCallback() {
        goToHomeActivity(true);
    }

    @Override
    public void onNetworkErrorCallback() {
        goToHomeActivity(true);
    }

    private void goToHomeActivity(final boolean networkFlag){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("NO_NETWORK", networkFlag);

                mContext.startActivity(intent);
            }
        });
    }
}
