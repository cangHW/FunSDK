package com.proxy.service.core.framework.collections.base.synchronize;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author: cangHX
 * @data: 2025/9/30 12:50
 * @desc:
 */
public class SynchronizedMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Serializable {

    @java.io.Serial
    private static final long serialVersionUID = 1978198479659022715L;

    private final ReentrantReadWriteLock.ReadLock read;
    private final ReentrantReadWriteLock.WriteLock write;

    private final Map<K, V> real;

    public SynchronizedMap(Map<K, V> real) {
        this.real = real;
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.read = lock.readLock();
        this.write = lock.writeLock();
    }

    public SynchronizedMap(
            Map<K, V> real,
            ReentrantReadWriteLock.ReadLock read,
            ReentrantReadWriteLock.WriteLock write
    ) {
        this.real = real;
        this.read = read;
        this.write = write;
    }

    public ReentrantReadWriteLock.ReadLock getReadLock() {
        return read;
    }

    public ReentrantReadWriteLock.WriteLock getWriteLock() {
        return write;
    }

    @Override
    public int size() {
        read.lock();
        try {
            return real.size();
        } finally {
            read.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        read.lock();
        try {
            return real.isEmpty();
        } finally {
            read.unlock();
        }
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        read.lock();
        try {
            return real.containsKey(key);
        } finally {
            read.unlock();
        }
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        read.lock();
        try {
            return real.containsValue(value);
        } finally {
            read.unlock();
        }
    }

    @Nullable
    @Override
    public V get(@Nullable Object key) {
        read.lock();
        try {
            return real.get(key);
        } finally {
            read.unlock();
        }
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        write.lock();
        try {
            return real.put(key, value);
        } finally {
            write.unlock();
        }
    }

    @Nullable
    @Override
    public V remove(@Nullable Object key) {
        write.lock();
        try {
            return real.remove(key);
        } finally {
            write.unlock();
        }
    }

    @Override
    public void putAll(@NonNull Map<? extends K, ? extends V> m) {
        write.lock();
        try {
            real.putAll(m);
        } finally {
            write.unlock();
        }
    }

    @Override
    public void clear() {
        write.lock();
        try {
            real.clear();
        } finally {
            write.unlock();
        }
    }

    private transient Set<K> keySet;

    @NonNull
    @Override
    public Set<K> keySet() {
        write.lock();
        try {
            if (keySet == null) {
                keySet = new SynchronizedSet<>(real.keySet(), read, write);
            }
            return keySet;
        } finally {
            write.unlock();
        }
    }

    private transient Collection<V> values;

    @NonNull
    @Override
    public Collection<V> values() {
        write.lock();
        try {
            if (values == null) {
                values = new SynchronizedCollection<>(real.values(), read, write);
            }
            return values;
        } finally {
            write.unlock();
        }
    }

    private transient Set<Map.Entry<K, V>> entrySet;

    @NonNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        write.lock();
        try {
            if (entrySet == null) {
                entrySet = new SynchronizedSet<>(real.entrySet(), read, write);
            }
            return entrySet;
        } finally {
            write.unlock();
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        write.lock();
        try {
            return real.equals(obj);
        } finally {
            write.unlock();
        }
    }

    @Override
    public int hashCode() {
        write.lock();
        try {
            return real.hashCode();
        } finally {
            write.unlock();
        }
    }

    @NonNull
    @Override
    public String toString() {
        write.lock();
        try {
            return real.toString();
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
        read.lock();
        try {
            return real.getOrDefault(key, defaultValue);
        } finally {
            read.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void forEach(@NonNull BiConsumer<? super K, ? super V> action) {
        write.lock();
        try {
            real.forEach(action);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void replaceAll(@NonNull BiFunction<? super K, ? super V, ? extends V> function) {
        write.lock();
        try {
            real.replaceAll(function);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public V putIfAbsent(K key, V value) {
        write.lock();
        try {
            return real.putIfAbsent(key, value);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean remove(@Nullable Object key, @Nullable Object value) {
        write.lock();
        try {
            return real.remove(key, value);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean replace(K key, @Nullable V oldValue, V newValue) {
        write.lock();
        try {
            return real.replace(key, oldValue, newValue);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public V replace(K key, V value) {
        write.lock();
        try {
            return real.replace(key, value);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public V computeIfAbsent(K key, @NonNull Function<? super K, ? extends V> mappingFunction) {
        write.lock();
        try {
            return real.computeIfAbsent(key, mappingFunction);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public V computeIfPresent(K key, @NonNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        write.lock();
        try {
            return real.computeIfPresent(key, remappingFunction);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public V compute(K key, @NonNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        write.lock();
        try {
            return real.compute(key, remappingFunction);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public V merge(K key, @NonNull V value, @NonNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        write.lock();
        try {
            return real.merge(key, value, remappingFunction);
        } finally {
            write.unlock();
        }
    }

    @java.io.Serial
    private void writeObject(ObjectOutputStream s) throws IOException {
        write.lock();
        try {
            s.defaultWriteObject();
        } finally {
            write.unlock();
        }
    }
}
