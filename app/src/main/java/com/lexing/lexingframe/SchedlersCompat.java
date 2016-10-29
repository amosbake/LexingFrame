package com.lexing.lexingframe;

import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author: mopel
 * Date : 2016/10/28
 */
public class SchedlersCompat {
    private static final Observable.Transformer computationTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object o) {
            return ((Observable) o).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private static final Single.Transformer singleComputationTransformer = new Single.Transformer() {
        @Override public Object call(Object observable) {
            return ((Single) observable).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private static final Observable.Transformer ioTransformer = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private static final Single.Transformer singleioTransformer = new Single.Transformer() {
        @Override public Object call(Object observable) {
            return ((Single) observable).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private static final Observable.Transformer newTransformer = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private static final Single.Transformer singleNewTransformer = new Single.Transformer() {
        @Override public Object call(Object observable) {
            return ((Single) observable).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private static final Observable.Transformer trampolineTransformer = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.trampoline())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private static final Single.Transformer singleTrampolineTransformer = new Single.Transformer() {
        @Override public Object call(Object observable) {
            return ((Single) observable).subscribeOn(Schedulers.trampoline())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private static final Observable.Transformer executorTransformer = new Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.from(ExecutorManager.eventExecutor))
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    private static final Single.Transformer singleExecutorTransformer = new Single.Transformer() {
        @Override public Object call(Object observable) {
            return ((Single) observable).subscribeOn(Schedulers.from(ExecutorManager.eventExecutor))
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };



    /**
     * Don't break the chain: use RxJava's compose() operator
     */
    public static <T> Observable.Transformer<T, T> applyComputationSchedulers() {
        return (Observable.Transformer<T, T>) computationTransformer;
    }
    public static <T> Single.Transformer<T, T> applySingleComputationSchedulers() {
        return (Single.Transformer<T, T>) singleExecutorTransformer;
    }
    public static <T> Observable.Transformer<T, T> applyIoSchedulers() {
        return (Observable.Transformer<T, T>) ioTransformer;
    }
    public static <T> Single.Transformer<T, T> applySingleIoSchedulers() {
        return (Single.Transformer<T, T>) singleExecutorTransformer;
    }
    public static <T> Observable.Transformer<T, T> applyNewSchedulers() {
        return (Observable.Transformer<T, T>) newTransformer;
    }
    public static <T> Single.Transformer<T, T> applySingleNewSchedulers() {
        return (Single.Transformer<T, T>) singleExecutorTransformer;
    }
    public static <T> Observable.Transformer<T, T> applyTrampolineSchedulers() {
        return (Observable.Transformer<T, T>) trampolineTransformer;
    }
    public static <T> Single.Transformer<T, T> applySingleTrampolineSchedulers() {
        return (Single.Transformer<T, T>) singleExecutorTransformer;
    }
    public static <T> Observable.Transformer<T, T> applyExecutorSchedulers() {
        return (Observable.Transformer<T, T>) executorTransformer;
    }
    public static <T> Single.Transformer<T, T> applySingleExecutorSchedulers() {
        return (Single.Transformer<T, T>) singleExecutorTransformer;
    }
}
