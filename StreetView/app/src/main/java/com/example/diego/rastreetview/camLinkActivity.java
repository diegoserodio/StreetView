package com.example.diego.rastreetview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class camLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_link);
        fullscreenStyle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fullscreenStyle();
    }

    protected void fullscreenStyle(){
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }
}
