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


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import java.util.Collections;
import java.util.List;

import static com.cosleeplib.listlayout.multitype.Preconditions.checkNotNull;

import com.cosleeplib.listlayout.ListLayout;

/**
 * 多类型通用Adapter
 *
 * @author drakeet
 */
public class MultiTypeAdapter extends ListLayout.Adapter<ListLayout.ViewHolder> {
    private static final String TAG = "MultiTypeAdapter";

    private
    List<?> items;
    private
    TypePool typePool;

    public MultiTypeAdapter() {
        this(Collections.emptyList());
    }

    public MultiTypeAdapter(List<?> items) {
        this(items, new MultiTypePool());
    }

    public MultiTypeAdapter(List<?> items, int initialCapacity) {
        this(items, new MultiTypePool(initialCapacity));
    }

    public MultiTypeAdapter(List<?> items, TypePool pool) {
        checkNotNull(items);
        checkNotNull(pool);
        this.items = items;
        this.typePool = pool;
    }

    /**
     * 注册ItemViewBinder，一种模型对应一个ItemViewBinder
     */
    public <T> void register(Class<? extends T> clazz, ItemViewBinder<T, ?> binder) {
        checkNotNull(clazz);
        checkNotNull(binder);
        checkAndRemoveAllTypesIfNeeded(clazz);
        register(clazz, binder, new DefaultLinker<>());
    }

    /**
     * 注册模型类和对应的ItemViewBinder，以及链接器
     */
    <T> void register(
            Class<? extends T> clazz,
            ItemViewBinder<T, ?> binder,
            Linker<T> linker) {
        typePool.register(clazz, binder, linker);
        binder.adapter = this;
    }

    /**
     * 注册，返回一对多
     */

    public <T> OneToManyFlow<T> register(Class<? extends T> clazz) {
        checkNotNull(clazz);
        checkAndRemoveAllTypesIfNeeded(clazz);
        return new OneToManyBuilder<>(this, clazz);
    }

    /**
     * 注册指定TypePool中的信息到Adapter
     */
    public void registerAll(final TypePool pool) {
        checkNotNull(pool);
        final int size = pool.size();
        for (int i = 0; i < size; i++) {
            registerWithoutChecking(
                    pool.getClass(i),
                    pool.getItemViewBinder(i),
                    pool.getLinker(i)
            );
        }
    }

    /**
     * 设置Adapter的数据
     */
    public void setItems(List<?> items) {
        checkNotNull(items);
        this.items = items;
    }

    /**
     * 获取Adapter的数据
     */
    public List<?> getItems() {
        return items;
    }

    /**
     * 绑定一个TypePool
     */
    public void setTypePool(TypePool typePool) {
        checkNotNull(typePool);
        this.typePool = typePool;
    }

    /**
     * 获取绑定的TypePool
     */
    public TypePool getTypePool() {
        return typePool;
    }

    /**
     * 获取条目对应的类型
     */
    @Override
    public final int getItemViewType(int position) {
        Object item = items.get(position);
        return indexInTypesOf(position, item);
    }

    @Override
    public void onBindViewHolder(ListLayout.ViewHolder holder, int position) {
        onBindViewHolder(holder, position, Collections.emptyList());
    }

    /**
     * 创建条目的ViewHolder
     */
    @Override
    public final ListLayout.ViewHolder onCreateViewHolder(ViewGroup parent, int indexViewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //从类型池中，通过type类型，查找对应的ItemViewBinder实例
        ItemViewBinder<?, ?> binder = typePool.getItemViewBinder(indexViewType);
        //通知ItemViewBinder创建ViewHolder
        return binder.onCreateViewHolder(inflater, parent);
    }

    /**
     * 渲染条目时调用
     */
    @SuppressWarnings("unchecked")
    public final void onBindViewHolder(ListLayout.ViewHolder holder, int position, List<?> payloads) {
        //获取条目数据
        Object item = items.get(position);
        //从类型池中，通过type类型，查找对应的ItemViewBinder实例
        ItemViewBinder binder = typePool.getItemViewBinder(holder.getItemViewType());
        //通知ItemViewBinder渲染条目
        binder.onBindViewHolder(holder, item, payloads);
    }

    /**
     * 获取条目数量
     */
    @Override
    public final int getItemCount() {
        return items.size();
    }

    /**
     * 获取条目Id
     */
    @Override
    @SuppressWarnings("unchecked")
    public final long getItemId(int position) {
        Object item = items.get(position);
        int itemViewType = getItemViewType(position);
        ItemViewBinder binder = typePool.getItemViewBinder(itemViewType);
        return binder.getItemId(item);
    }

    /**
     * 通过ViewHolder，获取对应的ItemViewBinder
     */
    private ItemViewBinder getRawBinderByViewHolder(ListLayout.ViewHolder holder) {
        return typePool.getItemViewBinder(holder.getItemViewType());
    }

    /**
     * 通过position查找条目对应的类型
     */
    int indexInTypesOf(int position, Object item) throws BinderNotFoundException {
        int index = typePool.firstIndexOf(item.getClass());
        if (index != -1) {
            @SuppressWarnings("unchecked")
            Linker<Object> linker = (Linker<Object>) typePool.getLinker(index);
            return index + linker.index(position, item);
        }
        throw new BinderNotFoundException(item.getClass());
    }

    /**
     * 移除某个模型的ItemViewBinder
     */
    private void checkAndRemoveAllTypesIfNeeded(Class<?> clazz) {
        if (typePool.unregister(clazz)) {
            Log.w(TAG, "You have registered the " + clazz.getSimpleName() + " type. " +
                    "It will override the original binder(s).");
        }
    }

    /**
     * 安全的注册方法，在注册前，先移除再注册，确保只注册一次
     */
    @SuppressWarnings("unchecked")
    private void registerWithoutChecking(Class clazz, ItemViewBinder binder, Linker linker) {
        checkAndRemoveAllTypesIfNeeded(clazz);
        register(clazz, binder, linker);
    }
}