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


import java.util.ArrayList;
import java.util.Collection;

/**
 * 方便创建ArrayList的类，一般条目数据有很多种不同类型，直接用该类会比较方便
 *
 * @author drakeet
 */
public class Items extends ArrayList<Object> {

    /**
     * Constructs an empty Items with an initial capacity of ten.
     */
    public Items() {
        super();
    }


    /**
     * Constructs an empty Items with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the Items
     * @throws IllegalArgumentException if the specified initial capacity
     *                                  is negative
     */
    public Items(int initialCapacity) {
        super(initialCapacity);
    }


    /**
     * Constructs a Items containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this Items
     * @throws NullPointerException if the specified collection is null
     */
    public Items(Collection<?> c) {
        super(c);
    }
}
