package com.example.blackbox;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.os.Environment.DIRECTORY_MOVIES;

public class VideoMapActivity extends AppCompatActivity implements SurfaceHolder.Callback, OnMapReadyCallback {
    private Camera camera;
    private MediaRecorder mediaRecorder;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean recording = false;

    private GoogleMap gMap;
    private MapFragment mapFrag;

    private MediaScanner mMediaScanner;

    private Timer mTimer;
    private TimerTask mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_map_record);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mMediaScanner = MediaScanner.getInstance(getApplicationContext());
        // ?????? ?????? ??? ????????? ???????????? ???????????? ???????????? ?????????.

        /*
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        },MODE_PRIVATE);
        */

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        //surfaceView = new SurfaceView(this);
        //FrameLayout frame = (FrameLayout)findViewById(R.id.fragment);
        //frame.addView(surfaceView);

        try {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
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
                                Toast.makeText(VideoMapActivity.this, "?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
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

        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // ?????? ????????? ?????? ?????? : getSystemService()???????????? ???????????? ?????? ??????

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VideoMapActivity.this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    1);
            // SDK??? ????????? 23 ????????????, Manifest?????? ACCESS_FINE_LOCATION(?????? ??????)??? ????????????????????? ???????????? ?????? if???
            // Manifest?????? ?????? ????????? ???????????? ???????????? ???????????? ??????
            // ActivityCompat.requestPermissions ( permission??? ????????? Activity, targets, num ) ??? ????????? ?????? ?????? ????????? ??????
            // ?????? ????????? ????????? ???????????? ??????????????? String permissions[]??? ???????????? ?????????, ???????????? ?????? ????????? ???????????? ???????????? ??? ?????? ???????????? ??????
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    // ?????? ???????????? ?????? ?????? ?????? ?????? (?????? ???)
                    1,
                    // ?????? ???????????? ?????? ?????? ?????? (??????)
                    GPSLocationListener);
            // GPSLocationListener??? LocationListener??? ???????????? ???????????? ???????????? ??????
        }
    }

    final LocationListener GPSLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // ?????? ?????? ????????? ???????????? GPSLocationListener??? ?????? ???????????? ??????????????????, ?????? ????????? ?????? ?????? ????????? ?????????????????? ????????????
            // ????????? ?????? ????????? ?????????
            String provider = location.getProvider();
            // provider=?????? ????????? gps??? ??????????????? network(wi-fi)??? ??????????????? ??????
        }
    };

    @Override
    public void onMapReady(GoogleMap map) {
        gMap = map;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(37.43150, 127.12890), 15));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "?????? ??????");
        menu.add(0, 2, 0, "?????? ??????");
        menu.add(0, 3, 0, "??????????????? ????????????");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case 2:
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case 3:
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        37.43150, 127.12890), 15));
                return true;
        }
        return false;
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
