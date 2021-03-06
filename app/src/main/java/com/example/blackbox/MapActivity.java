package com.example.blackbox;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    MapFragment mapFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_map_record);

        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 위치 관리자 객체 참조 : getSystemService()메소드를 사용하여 객체 참조

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
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

    final LocationListener GPSLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 앞서 위치 정보가 갱신되면 GPSLocationListener로 값을 전달하게 되어있으므로, 이를 받아서 현재 위치 정보가 변경되었는지 확인하고
            // 변경된 위치 정보를 전달함
            String provider = location.getProvider();
            // provider=현재 위치를 gps로 가져오는지 network(wi-fi)로 가져오는지 확인
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
        menu.add(0, 1, 0, "위성 지도");
        menu.add(0, 2, 0, "일반 지도");
        menu.add(0, 3, 0, "하이미디어 바로가기");
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
}
