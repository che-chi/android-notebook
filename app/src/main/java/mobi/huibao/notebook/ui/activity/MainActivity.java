package mobi.huibao.notebook.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.huibao.notebook.R;
import mobi.huibao.notebook.adapter.ContentViewPagerAdapter;
import mobi.huibao.notebook.listeners.MainPageChangeListener;
import mobi.huibao.notebook.ui.fragment.ContactsFragment;
import mobi.huibao.notebook.ui.fragment.NotebookFragment;
import mobi.huibao.notebook.ui.fragment.UserFragment;

public class MainActivity extends RealmActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.content_view_page)
    ViewPager contentViewPage;

    SearchView searchView;

    public Toolbar getToolbar() {
        return toolbar;
    }

    public SearchView getSearchView() {
        return searchView;
    }

    private void initOptionMenu(Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.menu_note_search).getActionView();
        searchView.setQueryHint("搜索联系人");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        initOptionMenu(menu);
        return true;
    }

    private void initViewPage() {
        Fragment[] fragments = new Fragment[]{ContactsFragment.newInstance(),
                new NotebookFragment(),
                new UserFragment()};

        ContentViewPagerAdapter adapter = new ContentViewPagerAdapter(getSupportFragmentManager(), fragments);
        contentViewPage.setAdapter(adapter);
        contentViewPage.addOnPageChangeListener(new MainPageChangeListener(this));
    }

    private void check() {

        new RxPermissions(this).requestEach(Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS)
                .subscribe(permission -> {

                    DialogInterface.OnClickListener onClickListener = (dialogInterface, i) ->
                            Toast.makeText(MainActivity.this, "确定按钮", Toast.LENGTH_LONG).show();

                    if (permission.granted) {
                        //授权执行的操作,比如读取用户通信录,对通信录进行索引操纵
                        new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("授权成功")
                                .setMessage("授权成功").setPositiveButton("确定（积极）", onClickListener).setNegativeButton("取消（消极）",
                                (dialogInterface, i) -> {
                                    Toast.makeText(MainActivity.this, "关闭按钮", Toast.LENGTH_LONG).show();
                                    dialogInterface.dismiss();
                                }).create().show();

                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //拒绝授权,给用户推荐相关联系人信息
                        new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("shouldShowRequestPermissionRationale")
                                .setMessage("shouldShowRequestPermissionRationale").setPositiveButton("确定（积极）", onClickListener).setNegativeButton("取消（消极）", (dialogInterface, i) -> {
                            Toast.makeText(MainActivity.this, "关闭按钮", Toast.LENGTH_LONG).show();
                            dialogInterface.dismiss();
                        }).create().show();
                    } else {
                        //用户拒绝授权,并且一户不在提醒,可以每次提醒用户授权
                        new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("shouldShowRequestPermissionRationale")
                                .setMessage("shouldShowRequestPermissionRationale").setPositiveButton("确定（积极）", onClickListener).setNegativeButton("取消（消极）", (dialogInterface, i) -> {
                            Toast.makeText(MainActivity.this, "关闭按钮", Toast.LENGTH_LONG).show();
                            dialogInterface.dismiss();
                        }).create().show();
                    }
                }).isDisposed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle("联系人(1020)");
        setSupportActionBar(toolbar);
        check();
        initViewPage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
