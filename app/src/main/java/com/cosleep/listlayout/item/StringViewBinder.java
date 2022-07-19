package com.cosleep.listlayout.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cosleep.listlayout.OnItemClickListener;
import com.cosleep.listlayout.R;
import com.cosleep.listlayout.model.StringModel;
import com.cosleeplib.listlayout.ListLayout;
import com.cosleeplib.listlayout.multitype.ItemViewBinder;


public class StringViewBinder extends ItemViewBinder<StringModel, StringViewBinder.ViewHolder> {
    private final OnItemClickListener mOnItemClickListener;

    public StringViewBinder(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_view_string, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull StringModel item) {
        holder.vItem.setText(item.getValue());
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
        private final TextView vItem;

        public ViewHolder(View itemView) {
            super(itemView);
            vItem = itemView.findViewById(R.id.item);
        }
    }
}