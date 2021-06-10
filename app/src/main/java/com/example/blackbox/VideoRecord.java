package com.example.blackbox;

import android.Manifest;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.os.Environment.DIRECTORY_MOVIES;

public class VideoRecord extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera camera;
    private MediaRecorder mediaRecorder;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean recording = false;

    private MediaScanner mMediaScanner;

    private Timer mTimer;
    private TimerTask mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_record);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mMediaScanner = MediaScanner.getInstance(getApplicationContext());
        // 영상 저장 후 미디어 스캐닝을 돌려줘야 갤러리에 반영됨.

        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        },MODE_PRIVATE);

        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);


        try {
            camera = Camera.open();
            //camera.setDisplayOrientation(90);
        } catch (Exception e) {
            e.printStackTrace();
        }
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);


        mTask = new TimerTask() {
            @Override
            public void run() {
                if(recording) {
                    mediaRecorder.release();
                    camera.lock();
                    recording=false;
                } else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mediaRecorder = new MediaRecorder();
                                camera.unlock();
                                mediaRecorder.setCamera(camera);
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                                mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
                                mediaRecorder.setOrientationHint(90);

                                String result = "";
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault());
                                Date curDate = new Date(System.currentTimeMillis());
                                String filename = formatter.format(curDate);
                                String strFolderName = Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES) + File.separator;
                                File file = new File(strFolderName);
                                if(!file.exists())
                                    file.mkdirs();
                                File f = new File(strFolderName + "/" + filename + ".mp4");
                                result = f.getPath();
                                mediaRecorder.setOutputFile(result);

                                mMediaScanner.mediaScanning(strFolderName + "/" + filename + ".mp4");
                                Toast.makeText(VideoRecord.this, "연속 녹화중 입니다.", Toast.LENGTH_SHORT).show();
                                mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                                recording = true;

                            } catch (Exception e) {
                                e.printStackTrace();
                                mediaRecorder.release();
                            }
                        }
                    });
                }
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 0, 5000);
    }

    @Override protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        try {
            if (camera == null) {
                camera.setPreviewDisplay(holder);
                camera.startPreview();

            }
        } catch (IOException e) {
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }
}
