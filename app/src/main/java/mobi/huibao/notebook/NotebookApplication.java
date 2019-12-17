package mobi.huibao.notebook;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.tamir7.contacts.Contacts;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NotebookApplication extends Application {

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            return new MaterialHeader(context);
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setDrawableSize(20));
    }

    @Override
    public void onCreate() {

        super.onCreate();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().
                name("notebook.realm").
                schemaVersion(1).
                build();

        Realm.setDefaultConfiguration(config);

        Fresco.initialize(this);

        Contacts.initialize(this);
    }
}
