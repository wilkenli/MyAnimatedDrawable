package com.facebook.executor.executorSupplier;

/**
 * Created by heshixiyang on 2017/3/30.
 */

import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Basic implementation of {@link ExecutorSupplier}.
 *
 * <p> Provides one thread pool for the CPU-bound operations and another thread pool for the
 * IO-bound operations.
 */
public class DefaultExecutorSupplier implements ExecutorSupplier {
    // Allows for simultaneous reads and writes.
    private static final int NUM_IO_BOUND_THREADS = 2;
    private static final int NUM_LIGHTWEIGHT_BACKGROUND_THREADS = 1;

    private final Executor mIoBoundExecutor;
    private final Executor mDecodeExecutor;
    private final Executor mBackgroundExecutor;
    private final Executor mLightWeightBackgroundExecutor;

    public DefaultExecutorSupplier(int numCpuBoundThreads) {
        ThreadFactory backgroundPriorityThreadFactory =
                new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);

        mIoBoundExecutor = Executors.newFixedThreadPool(NUM_IO_BOUND_THREADS);
        mDecodeExecutor = Executors.newFixedThreadPool(
                numCpuBoundThreads,
                backgroundPriorityThreadFactory);
        mBackgroundExecutor = Executors.newFixedThreadPool(
                numCpuBoundThreads,
                backgroundPriorityThreadFactory);
        mLightWeightBackgroundExecutor = Executors.newFixedThreadPool(
                NUM_LIGHTWEIGHT_BACKGROUND_THREADS,
                backgroundPriorityThreadFactory);

    }

    @Override
    public Executor forLocalStorageRead() {
        return mIoBoundExecutor;
    }

    @Override
    public Executor forLocalStorageWrite() {
        return mIoBoundExecutor;
    }

    @Override
    public Executor forDecode() {
        return mDecodeExecutor;
    }

    @Override
    public Executor forBackgroundTasks() {
        return mBackgroundExecutor;
    }

    @Override
    public Executor forLightweightBackgroundTasks() {
        return mLightWeightBackgroundExecutor;
    }
}
