/*
 * Copyright 2018 org.jfaster.derror.
 *   <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package org.jfaster.derror.cache.support;

import org.jfaster.derror.cache.CacheLoader;

import org.jfaster.derror.cache.LoadingCache;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.RequiredArgsConstructor;

/**
 * 二级缓存
 *
 * @author yangnan
 */
@RequiredArgsConstructor
public class DoubleCheckCache<K, V> implements LoadingCache<K, V> {

    private final CacheLoader<K, V> loader;
    private final ConcurrentMap<K, V> cache = new ConcurrentHashMap<>();
    private final ConcurrentMap<K, Object> locks = new ConcurrentHashMap<>();

    @Override
    public V get(K key) {
        V v = cache.get(key);
        if (v == null) {
            synchronized (getLock(key)) {
                if (v == null) {
                    v = loader.load(key);
                    if (v != null) {
                        cache.put(key, v);
                    }
                }
            }
        }

        return v;
    }

    /**
     * 获取锁对象
     *
     * @param key
     * @return
     */
    private Object getLock(K key) {
        Object lock = locks.get(key);
        if (lock == null) {
            lock = new Object();
            Object oldLock = locks.putIfAbsent(key, lock);
            //已经存在的lock
            if (oldLock != null) {
                lock = oldLock;
            }
        }
        return lock;
    }
}
