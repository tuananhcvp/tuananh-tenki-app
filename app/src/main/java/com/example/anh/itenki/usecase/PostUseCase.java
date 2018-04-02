package com.example.anh.itenki.usecase;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableCompletableObserver;

/**
 * Created by anh on 2018/04/02.
 */

public abstract class PostUseCase<PARAM> extends UseCase {

    protected PostUseCase(Scheduler threadExecutor, Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    protected void execute(PARAM param) {
        Completable.create(emitter -> {
            background(param);
            emitter.onComplete();
        }).subscribeOn(threadExecutor).observeOn(postExecutionThread)
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        complete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        error(e);
                    }
                });
    }

    /**
     * ユースケースで実行する手続き
     *
     * @param param
     * @return
     * @throws Exception
     */
    abstract public void background(PARAM param) throws Exception;

    /**
     * 成功時に呼ばれる
     */
    abstract public void complete();

    /**
     * エラー発生時に呼ばれる
     *
     * @param throwable
     */
    abstract public void error(Throwable throwable);
}

