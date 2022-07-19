package com.cosleep.listlayout.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.cosleep.listlayout.OnItemClickListener;
import com.cosleep.listlayout.R;
import com.cosleep.listlayout.model.ImageModel;
import com.cosleeplib.listlayout.ListLayout;
import com.cosleeplib.listlayout.multitype.ItemViewBinder;


/**
 * 图片左对齐的条目
 */
public class ImageLeftViewBinder extends ItemViewBinder<ImageModel, ImageLeftViewBinder.ViewHolder> {
    private final OnItemClickListener mOnItemClickListener;

    public ImageLeftViewBinder(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_image_left, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ImageModel item) {
        holder.vImage.setImageResource(item.getImgResId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    static class ViewHolder extends ListLayout.ViewHolder {
        private final ImageView vImage;

        public ViewHolder(View itemView) {
            super(itemView);
            vImage = itemView.findViewById(R.id.image);
        }
    }
}