package com.example.anh.itenki.usecase;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * 逐次処理を行うユースケース
 * PARAM：実行時のパラメータ
 * RESPONSE：完了後のレスポンス
 * <p>
 * use {@link SequentialUseCase2}
 */

public abstract class SequentialUseCase2<PARAM, RESPONSE> extends UseCase {

    private final CompositeDisposable compositeDisposable;

    protected SequentialUseCase2(Scheduler threadExecutor, Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        compositeDisposable = new CompositeDisposable();
    }

    public final void execute(UseCaseCallback<RESPONSE> callback, PARAM param) {
        Disposable disposable = buildUseCaseSingle(param)
                .subscribeOn(threadExecutor)
                .observeOn(postExecutionThread)
                .subscribe(
                        callback::onSuccess,
                        throwable -> {
//                            Timber.e(throwable);
                            this.onThrowable(throwable, callback);
                        });
        addDisposable(disposable);
    }

    protected abstract Single<RESPONSE> buildUseCaseSingle(PARAM param);

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public final void dispose() {
        compositeDisposable.clear();
    }

    protected void onThrowable(Throwable throwable, UseCaseCallback<RESPONSE> callback) {
        callback.onError(throwable);
    }

    public interface UseCaseCallback<RESPONSE> {

        void onSuccess(RESPONSE value);

        void onError(Throwable e);
    }
}
