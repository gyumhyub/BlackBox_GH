package com.example.blackbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GPSActivity extends AppCompatActivity {

    TextView txtResult;
    double longitude = 0;
    double latitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textview_gps);
        txtResult = findViewById(R.id.gps_location);

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 위치 관리자 객체 참조 : getSystemService()메소드를 사용하여 객체 참조

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GPSActivity.this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                            1);
                    // SDK의 버전이 23 이상이고, Manifest안에 ACCESS_FINE_LOCATION(위치 권한)을 설정해두었는지 확인하기 위한 if문
                    // Manifest안에 권한 설정이 되어있지 않는다면 진행되지 않음
                    // ActivityCompat.requestPermissions ( permission이 적용될 Activity, targets, num ) 의 형태로 위험 권한 부여를 요청
                    // 많은 권한이 필요한 경우에는 전역변수로 String permissions[]를 설정해도 되지만, 현재처럼 단일 권한을 요청하는 경우에는 위 처럼 작성해도 무방
        } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            // 위치 업데이트 간의 최소 시간 간격 (밀리 초)
                            1,
                            // 위치 업데이트 간의 최소 거리 (미터)
                            GPSLocationListener);
                             // GPSLocationListener는 LocationListener를 가져와서 상세하게 설정해서 사용
        }
    }

    // 위치 리스너 구현 위치 정보를 전달할 때 호출됨
    final LocationListener GPSLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 앞서 위치 정보가 갱신되면 GPSLocationListener로 값을 전달하게 되어있으므로, 이를 받아서 현재 위치 정보가 변경되었는지 확인하고
            // 변경된 위치 정보를 전달함
            String provider = location.getProvider();
            // provider=현재 위치를 gps로 가져오는지 network(wi-fi)로 가져오는지 확인
            longitude = location.getLongitude();
            // longitude=위도
            latitude = location.getLatitude();
            // latitude=경도

            txtResult.setText("위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude);
            // 레이아웃에서 만들어 놓았던 텍스트뷰에 위치정보, 위도, 경도를 표시함

            Toast.makeText(getApplicationContext(), "구글맵으로 이동합니다.", Toast.LENGTH_SHORT).show();
            // Toast 메세지를 이용하여 위도, 경도를 이용한 구글맵(사이트) 이동 안내
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/maps/@" + latitude + "," + longitude + ",14z"));
            // Intent에 해당 Uri을 이용하여 위도, 경도를 입력하고 이동
            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            // FLAG를 만들어서 해당 창을 새 창으로 띄워줌 (지도를 보고 난 후 어플로 돌아올 수 있도록)
            startActivity(intent);
            // intent 시작
        }
    };
}