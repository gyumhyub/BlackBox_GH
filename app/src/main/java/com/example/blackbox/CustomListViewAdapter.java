package com.example.blackbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<com.example.blackbox.CustomListViewItem> listViewItemList = new ArrayList<>() ;

    // Adapter에 사용되는 데이터의 개수를 리턴 : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴 : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_custom, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.thumbnail) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.CustomView) ;

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageBitmap(listViewItemList.get(position).getIcon());
        titleTextView.setText(listViewItemList.get(position).getFilename());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴 : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public String getName(int position) {
        return listViewItemList.get(position).getFilename();
    }

    // 아이템 데이터 추가를 위한 함수로 개발자가 원하는대로 작성 가능
    public void addItem(Bitmap icon, String filename) {

        com.example.blackbox.CustomListViewItem item = new com.example.blackbox.CustomListViewItem();

        item.setIcon(icon);
        item.setFilename(filename);

        listViewItemList.add(item);
    }

    // Adapter의 addItem을 이용하여 사용자가 쉽게 값을 전달할 수 있도록 설정
}