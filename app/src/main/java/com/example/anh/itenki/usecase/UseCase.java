package com.example.anh.itenki.usecase;

import io.reactivex.Scheduler;

/**
 * Created by anh on 2018/03/06.
 */

public abstract class UseCase {

    protected Scheduler threadExecutor;
    protected Scheduler postExecutionThread;

    protected UseCase(Scheduler threadExecutor,
                      Scheduler postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }
}
