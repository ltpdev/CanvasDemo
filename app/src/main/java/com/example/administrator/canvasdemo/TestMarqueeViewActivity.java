package com.example.administrator.canvasdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestMarqueeViewActivity extends AppCompatActivity {
    private MarqueeView marqueeView;
    private Button start;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_marquee_view);
        marqueeView = (MarqueeView) findViewById(R.id.marqueeView);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marqueeView.startAnimation();
            }
        });
    }
}
