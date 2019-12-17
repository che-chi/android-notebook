package mobi.huibao.notebook.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;

public class ContactsService extends Service {

    private final static String TAG = ContactsService.class.getSimpleName();

    public static void startCommand(Context context, int status) {
        Intent intent = new Intent(context, ContactsService.class);
        intent.putExtra("status", status);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d(TAG, "on bind");
        return null;
    }


    @Override
    public void onCreate() {
        LogUtils.d(TAG, "on create");
        super.onCreate();
    }

    @Override
    public ComponentName startService(Intent service) {
        LogUtils.d(TAG, "start service");
        return super.startService(service);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtils.d(TAG, "on start command", "flags:=>" + flags, "startId:=>" + startId);

        int status = intent.getIntExtra("status", -1);
        switch (status) {
            case 1:
                LogUtils.d(TAG, "start command 1");
                break;
            case 2:
                LogUtils.d(TAG, "start command 2");
                break;
            default:
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.d(TAG, "on unbind");
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        LogUtils.d(TAG, "on destroy");
        super.onDestroy();
    }
}
