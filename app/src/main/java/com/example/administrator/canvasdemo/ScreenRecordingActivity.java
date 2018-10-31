package com.example.administrator.canvasdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecordingActivity extends AppCompatActivity {
public static final String TAG="ScreenRecordingActivity";
private static final int STORAGE_REQUEST_CODE=101;
private static final int RECORD_REQUEST_CODE=201;
private MediaProjectionManager projectionManager;
private MediaProjection mediaProjection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_screen_recording);
        projectionManager= (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            Log.i(TAG,"该设备为5.0以下接口");
            finish();
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED&&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED){
                     screenRecording();
            }else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},STORAGE_REQUEST_CODE);
            }
        }else {
            //  5.0 和 5.1  进行录屏
            screenRecording();
        }
    }


    private void screenRecording() {
        Intent intent=projectionManager.createScreenCaptureIntent();
        startActivityForResult(intent,RECORD_REQUEST_CODE);
    }

   @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_REQUEST_CODE:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    screenRecording();
                }else {
                    Toast.makeText(this,"用户未授权无法录制",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
          if (requestCode==RECORD_REQUEST_CODE&&resultCode==RESULT_OK){
              Log.i(TAG,"可以进行录屏");
              mediaProjection=projectionManager.getMediaProjection(resultCode,data);
              DisplayMetrics metrics=new DisplayMetrics();
              getWindowManager().getDefaultDisplay().getMetrics(metrics);
              int orientation=getResources().getConfiguration().orientation;
              RecorderView.getInstance().showRecord().setRecorderConfig(mediaProjection,metrics,orientation).startRecorder();
              finish();
          }else {
              Log.i(TAG,"用户取消了录屏");
              finish();
          }
    }
}
