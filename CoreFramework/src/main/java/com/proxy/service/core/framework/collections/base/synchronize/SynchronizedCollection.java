package com.proxy.service.core.framework.collections.base.synchronize;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author: cangHX
 * @data: 2025/9/30 12:35
 * @desc:
 */
public class SynchronizedCollection<E> implements java.util.Collection<E>, Serializable {

    @java.io.Serial
    private static final long serialVersionUID = 3053995032091335093L;

    protected final ReentrantReadWriteLock.ReadLock read;
    protected final ReentrantReadWriteLock.WriteLock write;

    protected final java.util.Collection<E> real;

    public SynchronizedCollection(@NonNull java.util.Collection<E> real) {
        this.real = real;
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.read = lock.readLock();
        this.write = lock.writeLock();
    }

    public SynchronizedCollection(
            @NonNull java.util.Collection<E> real,
            ReentrantReadWriteLock.ReadLock read,
            ReentrantReadWriteLock.WriteLock write
    ) {
        this.real = real;
        this.read = read;
        this.write = write;
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
    public boolean contains(@Nullable Object o) {
        read.lock();
        try {
            return real.contains(o);
        } finally {
            read.unlock();
        }
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return real.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        read.lock();
        try {
            return real.toArray();
        } finally {
            read.unlock();
        }
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        read.lock();
        try {
            return real.toArray(a);
        } finally {
            read.unlock();
        }
    }

    @Override
    public boolean add(E e) {
        write.lock();
        try {
            return real.add(e);
        } finally {
            write.unlock();
        }
    }

    @Override
    public boolean remove(@Nullable Object o) {
        write.lock();
        try {
            return real.remove(o);
        } finally {
            write.unlock();
        }
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        read.lock();
        try {
            return real.containsAll(c);
        } finally {
            read.unlock();
        }
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> c) {
        write.lock();
        try {
            return real.addAll(c);
        } finally {
            write.unlock();
        }
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        write.lock();
        try {
            return real.removeAll(c);
        } finally {
            write.unlock();
        }
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        read.lock();
        try {
            return real.retainAll(c);
        } finally {
            read.unlock();
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
    @Override
    public void forEach(@NonNull Consumer<? super E> action) {
        write.lock();
        try {
            real.forEach(action);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean removeIf(@NonNull Predicate<? super E> filter) {
        write.lock();
        try {
            return real.removeIf(filter);
        } finally {
            write.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Spliterator<E> spliterator() {
        return real.spliterator();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Stream<E> stream() {
        return real.stream();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Stream<E> parallelStream() {
        return real.parallelStream();
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
