package es.npatarino.android.gotchallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjiazhe.scrollparallaximageview.ScrollParallaxImageView;
import com.gjiazhe.scrollparallaximageview.parallaxstyle.VerticalMovingStyle;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    private Context mContext;

    private ScrollParallaxImageView ivp;
    private TextView tvn;
    private TextView tvd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mContext = this;

        ivp = findViewById(R.id.iv_photo);
        tvn = findViewById(R.id.tv_name);
        tvd = findViewById(R.id.tv_description);

        ivp.setParallaxStyles(new VerticalMovingStyle());

        final String d = getIntent().getStringExtra("description");
        final String n = getIntent().getStringExtra("name");
        final String i = getIntent().getStringExtra("imageUrl");

        Toolbar toolbar = findViewById(R.id.t);
        toolbar.setTitle(n);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                Bitmap bmp;
                try {
                    url = new URL(i);
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.got);
                }

                updateUI(bmp, n, d);
            }
        }).start();
    }

    private void updateUI(final Bitmap bmp, final String name, final String description){
        DetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivp.setImageBitmap(bmp);
                tvn.setText(name);
                tvd.setText(description);
            }
        });
    }
}
