package com.example.smartgladiatortask.util.imagepicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.smartgladiatortask.R;
import com.example.smartgladiatortask.util.imagepicker.models.Album;

import java.io.File;
import java.util.ArrayList;


public class CustomAlbumSelectAdapter extends CustomGenericAdapter<Album> {

    public CustomAlbumSelectAdapter(Activity activity, Context context, ArrayList<Album> albums) {
        super(activity, context, albums);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_view_item_album_select, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.image_view_album_image);
            viewHolder.textView = convertView.findViewById(R.id.text_view_album_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.getLayoutParams().width = size;
        viewHolder.imageView.getLayoutParams().height = size;

        viewHolder.textView.setText(arrayList.get(position).name);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.back_icon);

        if (arrayList.get(position).name != null && arrayList.get(position).name.equals("Take Photo")) {
            Glide.with(context).load(arrayList.get(position).cover).apply(options).into(viewHolder.imageView);
        } else {
            final Uri uri = Uri.fromFile(new File(arrayList.get(position).cover));
            Glide.with(context).load(uri).apply(options).into(viewHolder.imageView);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
