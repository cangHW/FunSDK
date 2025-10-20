package com.proxy.service.core.framework.collections.base.synchronize;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: cangHX
 * @data: 2025/9/30 12:32
 * @desc:
 */
public class SynchronizedSet<E> extends SynchronizedCollection<E> implements java.util.Set<E> {

    public SynchronizedSet(@NonNull java.util.Set<E> cn) {
        super(cn);
    }

    public SynchronizedSet(
            @NonNull java.util.Set<E> cn,
            ReentrantReadWriteLock.ReadLock read,
            ReentrantReadWriteLock.WriteLock write
    ) {
        super(cn, read, write);
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
}
