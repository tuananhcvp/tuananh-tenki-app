package com.example.anh.itenki.usecase;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by anh on 2018/03/06.
 */

/**
 * 逐次処理を行うユースケース
 * PARAM：実行時のパラメータ
 * RESPONSE：完了後のレスポンス
 */

public abstract class SequentialUseCase<PARAM, RESPONSE> extends UseCase {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected SequentialUseCase(Scheduler threadExecutor, Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    protected void execute(final PARAM param) {
        Single<RESPONSE> responseSingle = Single.create(emitter -> emitter.onSuccess(background(param)));
        Disposable disposable = responseSingle
                .subscribeOn(threadExecutor)
                .observeOn(postExecutionThread)
                .subscribe(
                        this::onSuccess,
                        failed -> {
//                            Timber.e(failed);
                            onError(failed);
                        }
                );
    }

    public void dispose() {
        compositeDisposable.clear();
    }

    /**
     * ユースケースで実行する手続き
     *
     * @param param
     * @return
     * @throws Exception
     */
    abstract public RESPONSE background(PARAM param) throws Exception;

    /**
     * 成功した時に呼ばれる
     *
     * @param response
     */
    abstract public void onSuccess(RESPONSE response);

    /**
     * エラー発生時に呼ばれる
     *
     * @param throwable
     */
    abstract public void onError(Throwable throwable);

}
