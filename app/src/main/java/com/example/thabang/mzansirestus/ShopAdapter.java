package com.example.thabang.mzansirestus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thabang.mzansirestus.pojo.Shop;


import java.util.ArrayList;

public class ShopAdapter extends BaseAdapter {

    private ArrayList<Shop> listShops;
    private static LayoutInflater inflater=null;
    private Context context1;
    
    public ShopAdapter(Context context, ArrayList<Shop> shops) {
        listShops = shops;
        context1 = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return listShops.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView shopName = (TextView)vi.findViewById(R.id.tvShopName); // title
        TextView shopDesc = (TextView)vi.findViewById(R.id.tvDesc);
        ImageView logo = vi.findViewById(R.id.list_image_row);

        // artist name

        Shop shop = listShops.get(position);
        
        // Setting all values in listview
        shopName.setText(shop.getTradingName());
        shopDesc.setText(shop.getAbout());
        Glide.with(context1).load(shop.getSdownloadLLogol()).into(logo);
        return vi;
    }
}