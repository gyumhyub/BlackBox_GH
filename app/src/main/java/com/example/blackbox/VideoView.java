package com.example.blackbox;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import androidx.appcompat.app.AppCompatActivity;

public class VideoView extends AppCompatActivity {

    private android.widget.VideoView videoView;
        // 안드로이드 시스템상의 비디오 위젯 'android.widget.VideoView'을 videoView 변수에 저장

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // (안드로이드 시스템 기능) 동영상 출력 시 상단바 제거

        Uri uri = Uri.parse(getIntent().getStringExtra("uri"));
            // "uri"의 문자열을 uri 변수(폴더안의 파일 링크)와 연결

        videoView = findViewById(R.id.videoView);
            // 'android.widget.VideoView'를 videoview.xml 의 videoView 레이아웃과 연결
        videoView.setMediaController(new MediaController(this)); // MediaController 기능 추가
        videoView.setVideoURI(uri); // 비디오 기능을 uri 파일에 연결
        videoView.start(); // 비디오 시작(재생)

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                //'android.widget.VideoView'가 종료되면 다음 기능을 수행
            @Override
            public void onCompletion(MediaPlayer mp) {  // MediaPlayer 영상 종료 시 다음 기능을 수행
                onBackPressed(); // 강제로 이전 화면으로 돌아가게 함
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus); // 안드로이드 시스템 기능상 onWindowFocusChanged 상속
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    // 전체 화면(풀스크린)으로 레이아웃 설정(하단 네비게이션바 레이아웃 없앰)
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    // (위 기능에서 추가) 전체 화면으로 설정된 레이아웃에서 하단 네비게이션바 숨김
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    // (위 기능에서 추가) 클릭하면 네비게이션바와 미디어콘드롤러 동시에 나타나게함
    }
}
