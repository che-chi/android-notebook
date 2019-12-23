package mobi.huibao.notebook.tools.rx;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class RxTools {

    /**
     * 在android UI 主线程执行不耗时的任务
     *
     * @param action
     */
    public static void runOnMain(Action action) {
        Observable.create(e -> {
            action.run();
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    /**
     * 重新启动一个线程执行任务
     *
     * @param action
     */
    public static void runOnNewThread(Action action) {
        Observable.create(e -> {
            action.run();
        }).subscribeOn(Schedulers.newThread()).subscribe();
    }

    /**
     * 启动一个 IO 线程执行一个任务
     *
     * @param action
     */
    public static void runOnIoThread(Action action) {
        Observable.create(e -> {
            action.run();
        }).subscribeOn(Schedulers.io()).subscribe();
    }
}
