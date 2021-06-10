package com.example.blackbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoListView extends AppCompatActivity {

    ListView listView;
    ArrayList<String> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_video);

        listView = (ListView) findViewById(R.id.videoListView);
        CustomListViewAdapter adapter = new CustomListViewAdapter();
        listView.setAdapter(adapter);

        listRaw(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = adapter.getName(position);
                Uri uri = Uri.parse("sdcard/Movies/"+ filename);
                Intent intent = new Intent(getApplicationContext(), VideoView.class);
                intent.putExtra("uri", uri.toString());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("확인");
                alert.setMessage(adapter.getName(position).toString()+" 파일을 삭제하시겠습니까?");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),adapter.getName(position).toString() + " 파일이 삭제되었습니다.",Toast.LENGTH_LONG).show();
                        adapter.remove(position); // 확장된 BaseAdapter의 CustomListViewAdapter에서 메소드 제거
                        File f = new File("sdcard/Movies/"+adapter.getName(position));
                        f.delete();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                return true;
            }
        });
    }

    public void listRaw(CustomListViewAdapter adapter){
        File list_file = new File("sdcard/Movies/");
        File list[] = list_file.listFiles();

        for(int i = 0; i < list.length; i++){

            Bitmap bitmap  = ThumbnailUtils.createVideoThumbnail(list[i].getPath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 200, 150);
            adapter.addItem(thumbnail,list[i].getName());
        }
    }
}