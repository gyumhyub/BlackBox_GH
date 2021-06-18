package com.example.blackbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class FolderListView extends AppCompatActivity {

    ListView listView; // 안드로이드 위젯 'ListView'을 사용하도록 listView 변수 선언
    List<String> setFolderView = new ArrayList<>(); //setFolderView 변수에 메모리 공간들을 할당
    String folderName; // folderName 문자열 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_folder);

        listView = findViewById(R.id.FolderListView);
            // 'android.widget.ListView'를 FolderListView.xml 의 FolderListView 레이아웃과 연결
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, setFolderView);
            //
        listView.setAdapter(adapter);
            // ListView를 사용하기 위해 adapter를 이용하여 연결
        listRaw(setFolderView);
            // setFolderView의 메모리 공간에 할당된 변수를 listRaw 클래스에 선언


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = setFolderView.get(position);
                // 사용자가 원하는 위치의 영상을 클릭 하였을 때 getName(Position)을 이용하여
                // 해당 위치의 Item값을 가져와서 setFileView으로 전달함
                Intent intent = new Intent(getApplicationContext(), FileListView.class);
                // intent를 이용하여 VideoView.class와 연결하여 해당 class로 값이 넘어갈 수 있도록 함
                intent.putExtra("folderName",folderName);
                // intent를 이용하여 넘길 값을 puExtra함(담기)
                intent.putExtra("fileName",fileName);
                startActivity(intent);
                // 'intent' 변수의 링크로 액티비디 이동
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("확인");
                alert.setMessage(setFolderView.get(position) +" 폴더를 삭제하시겠습니까?");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), setFolderView.get(position) + " 폴더가 삭제되었습니다.",Toast.LENGTH_LONG).show();
                        setFolderView.remove(position);
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

    public void sdPath() {
        File file = new File("storage/");
        File[] listOfStorages = file.listFiles();

        for (File tmp : listOfStorages) {
            if (tmp.getName().contains("-")) {
                // tmp를 이용하여 getName을 진행 할 때 문자에 "-"가 들어있는지 확인하도록 contains를 사용
                folderName = tmp.getName();
                // 해당되는 값이 있다면 이를 folderName 변수에 담고 return을 이용하여 listRaw로 값을 되돌려 줌
                break;
            }
        }
    }

    public void listRaw(List<String> fileList) {
        sdPath();
        File file1 = new File("storage/"+folderName);
        File[] list = file1.listFiles();

        for( int i=0; i< list.length; i++)
        {
            fileList.add(list[i].getName());
        }

    }
}
