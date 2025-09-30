package com.proxy.service.core.framework.collections.base.synchronize;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author: cangHX
 * @data: 2025/9/30 12:32
 * @desc:
 */
public class SynchronizedSet<E> extends SynchronizedCollection<E> implements java.util.Set<E> {

    public SynchronizedSet(@NonNull java.util.Set<E> cn) {
        super(cn);
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
