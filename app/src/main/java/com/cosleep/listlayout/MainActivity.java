package com.cosleep.listlayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.cosleep.listlayout.item.ImageCenterViewBinder;
import com.cosleep.listlayout.item.ImageLeftViewBinder;
import com.cosleep.listlayout.item.ImageRightViewBinder;
import com.cosleep.listlayout.item.NumberViewBinder;
import com.cosleep.listlayout.item.StringViewBinder;
import com.cosleep.listlayout.model.ImageModel;
import com.cosleep.listlayout.model.NumberModel;
import com.cosleep.listlayout.model.StringModel;
import com.cosleeplib.listlayout.ListLayout;
import com.cosleeplib.listlayout.multitype.ClassLinker;
import com.cosleeplib.listlayout.multitype.ItemViewBinder;
import com.cosleeplib.listlayout.multitype.Items;
import com.cosleeplib.listlayout.multitype.MultiTypeAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;


public class MainActivity extends AppCompatActivity {
    private SmartRefreshLayout vRefreshLayout;
    private ListLayout vListLayout;

    private final Items mListItems = new Items();
    private final MultiTypeAdapter mListAdapter = new MultiTypeAdapter(mListItems);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        bindView();
        setData();
    }

    private void findView() {
        vRefreshLayout = findViewById(R.id.refresh_layout);
        vListLayout = findViewById(R.id.list_layout);
    }

    private void bindView() {
        vRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
        vRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore();
            }
        });
        OnItemClickListener onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Object itemModel = mListItems.get(position);
                Toast.makeText(getApplicationContext(), itemModel.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        //多类型
        mListAdapter.register(StringModel.class, new StringViewBinder(onItemClickListener));
        mListAdapter.register(NumberModel.class, new NumberViewBinder(onItemClickListener));
        //一对多
        mListAdapter.register(ImageModel.class).to(
                new ImageCenterViewBinder(onItemClickListener),
                new ImageLeftViewBinder(onItemClickListener),
                new ImageRightViewBinder(onItemClickListener)
        ).withClassLinker(new ClassLinker<ImageModel>() {
            @NonNull
            @Override
            public Class<? extends ItemViewBinder<ImageModel, ?>> index(int position, @NonNull ImageModel model) {
                if (model.getType() == ImageModel.TYPE_CENTER) {
                    return ImageCenterViewBinder.class;
                } else if (model.getType() == ImageModel.TYPE_LEFT) {
                    return ImageLeftViewBinder.class;
                } else if (model.getType() == ImageModel.TYPE_RIGHT) {
                    return ImageRightViewBinder.class;
                }
                return ImageCenterViewBinder.class;
            }
        });
        vListLayout.setAdapter(mListAdapter);
    }

    private void setData() {
        refresh();
    }

    private void refresh() {
        mListItems.clear();
        //一对多
        mListItems.add(new ImageModel(ImageModel.TYPE_CENTER, R.mipmap.ic_launcher));
        mListItems.add(new ImageModel(ImageModel.TYPE_LEFT, R.mipmap.ic_launcher));
        mListItems.add(new ImageModel(ImageModel.TYPE_RIGHT, R.mipmap.ic_launcher));
        //多类型
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                mListItems.add(new StringModel("String：" + i));
            } else {
                mListItems.add(new NumberModel(i));
            }
        }
        mListAdapter.notifyDataSetChanged();
        vRefreshLayout.finishRefresh(true);
    }

    private void loadMore() {
        for (int i = 1; i <= 5; i++) {
            mListItems.add(new StringModel("LoadMore：" + i));
        }
        mListAdapter.notifyDataSetChanged();
        vRefreshLayout.finishLoadMore(true);
    }
}