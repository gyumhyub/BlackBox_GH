package com.example.blackbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListView extends AppCompatActivity {

    ListView listView;
    String folderName;
    String folderName1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_file);

        listView = findViewById(R.id.fileListView);

        CustomListViewAdapter adapter = new CustomListViewAdapter();
        folderName = getIntent().getStringExtra("folderName");
        folderName1 = getIntent().getStringExtra("fileName");
        listView.setAdapter(adapter);

        listRaw(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = adapter.getName(position);
                Uri uri = Uri.parse("storage/"+folderName+"/"+folderName1+"/"+filename);
                // 해당 위치의 uri를 가져옴
                Intent intent = new Intent(getApplicationContext(), VideoView.class);
                intent.putExtra("uri",uri.toString());
                // intent를 이용하여 넘길 값을 puExtra함(담기)
                // uri.toStriong()을 이용하여 주소값을 String형태로 가져와 uri라는 이름을 지정한 변수에 넣어 전달
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
                       // File f = new File("storage/"+folderName+"/"+folderName1+"/"+adapter.getName(position));
                       // f.delete();
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

    public void listRaw(CustomListViewAdapter adapter) {
        File file2 = new File("storage/"+folderName+"/"+folderName1);
        // 해당 위치에 있는 File을 가져옴
        File list[] = file2.listFiles();
        // File 배열을 만들어 앞에서 가져온 값들을 넣어줌
        for(int i = 0; i < list.length; i++){

            Bitmap bitmap  = ThumbnailUtils.createVideoThumbnail(list[i].getPath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            // CreateVideoThumbnail을 이용하여 영상의 첫 화면을 썸네일로 보일 수 있도록 함
            // 영상이 저장되어있는 path를 지정해주고, MediaStore.Video.Thumbnails를 이용하여 원하는 형태로 저장
            // FULL_SCREEN_KIND의 경우 영상의 사이즈 만큼 thumbnail이 저장되며 아래의 명령어를 사용할 경우 자동으로 정해진 규격이 적용됨
            // MINI_KIND: 512 x 384 thumbnail
            // MICRO_KIND: 96 x 96 thumbnail
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 200, 150);
            // 원하는 정도의 사이즈로 thumbnail이 표시될 수 있도록 정정
            adapter.addItem(thumbnail,list[i].getName());
            // CustomListViewAdapter에서 만들어낸 함수(addItem)를 이용하여 사용자가 입력한 값이
            // CustomListViewItem으로 전달될 수 있도록 함
            // 영상의 첫 장면(thumbnail)과 파일명(list[i].getName())을 전달함

        }
    }
}