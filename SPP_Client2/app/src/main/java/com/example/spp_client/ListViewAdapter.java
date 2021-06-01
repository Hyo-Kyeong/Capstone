package com.example.spp_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public ListViewAdapter(){
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //i에 위치한 데이터를 화면에 출력하는데 사용할 View를 리턴
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos=i;
        final Context context = viewGroup.getContext();

        //"listview_item" Layout을 inflate하여 convertView 참조 획득
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_item, viewGroup, false);
        }

        //화면에 표시할 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView dateTextView = (TextView) view.findViewById(R.id.textView24);
        TextView storeTextView = (TextView) view.findViewById(R.id.textView25);
        TextView priceTextView = (TextView) view.findViewById(R.id.textView26);

        //Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(i);

        //아이템 내 각 위젯에 데이터 반영
        dateTextView.setText(listViewItem.getDate());
        storeTextView.setText(listViewItem.getStore());
        priceTextView.setText(listViewItem.getPrice());

        return view;
    }

    //아이템 데이터 추가를 위한 함수.
    public void addItem(String date, String store, String price){
        ListViewItem item = new ListViewItem();

        item.setDate(date);
        item.setStore(store);
        item.setPrice(price);

        listViewItemList.add(item);
    }

    public void deleteAllItems(){
        listViewItemList.clear();
    }
}
