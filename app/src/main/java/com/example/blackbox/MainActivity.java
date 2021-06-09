package com.example.blackbox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("블랙박스 앱 구현중");

        verifyStoragePermissions(this);
            // verifyStoragePermissions 클래스(activity) 선언
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    }; // Manifest에 선언한 STORAGE 읽기/쓰기 접근 권한을 프로그램이 종료될때 까지 'PERMISSIONS_STORAGE' 메모리에 저장

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
        // 내부 저장소에 접근하기 위한(권한 구분을 위한) 상수 설정 (final을 이용하여 값이 변하지 않도록 함)

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // 사용자에게 STORAGE에 접근하여 파일 쓰기 권한을 확인

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE
             // 허용하지 않았다면 권한이 없으므로 사용자에게 다시 접근할 것인지 확인 메시지를 표시(권한에 대해 설명하는 대화 상자 표시)
            );
        }
    }

    public void VideoRecord(View view) { // onClick='button1' 클릭 버튼 연결
        Intent intent = new Intent(getApplicationContext(), VideoRecord.class);
        startActivity(intent);
    }

    public void VideoListView(View view) {
        Intent intent = new Intent(getApplicationContext(), VideoListView.class);
        startActivity(intent);
    }

    public void ListView(View view){
        Intent intent = new Intent(getApplicationContext(), FolderListView.class);
            // FolderListView.java 액티비티로 연결할 수 있게 Intent 함수를 사용하여 'intent' 변수에 저장
        startActivity(intent);
            // 'intent' 변수의 링크로 액티비디 이동
    }

    public void gps(View view) {
        Intent intent = new Intent(getApplicationContext(), GPSActivity.class);
        startActivity(intent);
    }


}