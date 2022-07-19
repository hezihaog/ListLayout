/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cosleeplib.listlayout.multitype;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cosleeplib.listlayout.ListLayout;

import java.util.List;

/***
 * 条目绑定类，一个类型对应一个Binder类
 * @author drakeet
 */
public abstract class ItemViewBinder<T, VH extends ListLayout.ViewHolder> {
    /* internal */ MultiTypeAdapter adapter;

    /**
     * 创建ViewHolder时回调
     */
    protected abstract VH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent);

    /**
     * 渲染条目
     */
    protected void onBindViewHolder(VH holder, T item, List<Object> payloads) {
        onBindViewHolder(holder, item);
    }

    /**
     * 渲染条目
     */
    protected abstract void onBindViewHolder(VH holder, T item);

    /**
     * 获取当前条目在列表中的位置
     */
    protected final int getPosition(final ListLayout.ViewHolder holder) {
        return holder.getAdapterPosition();
    }

    /**
     * 获取条目所在的列表的Adapter适配器
     */
    protected final MultiTypeAdapter getAdapter() {
        if (adapter == null) {
            throw new IllegalStateException("ItemViewBinder " + this + " not attached to MultiTypeAdapter. " +
                    "You should not call the method before registering the binder.");
        }
        return adapter;
    }

    /**
     * 返回条目Id，默认为-1
     */
    protected long getItemId(T item) {
        return ListLayout.NO_ID;
    }
}