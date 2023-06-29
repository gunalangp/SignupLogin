package com.example.smartgladiatortask.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartgladiatortask.R;
import com.example.smartgladiatortask.databinding.ListHomeBinding;
import com.example.smartgladiatortask.model.home.ContentModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    View view;
    private Context mContext;
    private List<ContentModel> mServiceList;
    public OnItemClickListener itemClickListener;

    public HomeAdapter(Context context, List<ContentModel> dataList) {
        this.mContext = context;
        this.mServiceList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListHomeBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.list_home, parent, false);

        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ContentModel data = mServiceList.get(position);

        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mServiceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ListHomeBinding itemBinding;

        public MyViewHolder(ListHomeBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(ContentModel data) {
            if (data.getThumbnailUrl() != null) {
              /*  Glide.with(mContext)
                        .load(data.getThumbnailUrl())
                        .into(itemBinding.imgService);*/
                Picasso.get().load(data.getThumbnailUrl())
                        .into(itemBinding.imgService);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.person_icon)
                        .into(itemBinding.imgService);
            }
            if (data.getTitle() != null) {
                itemBinding.tvName.setText(data.getTitle());
            } else {
                itemBinding.tvName.setText("");
            }
            itemBinding.linear.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(data, getAdapterPosition());
                }
            });
        }
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        itemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ContentModel data, int position);
    }
}
