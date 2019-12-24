package mobi.huibao.notebook.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.huibao.notebook.R;
import mobi.huibao.notebook.adapter.ContentViewPagerAdapter;
import mobi.huibao.notebook.ui.fragment.ContactsFragment;
import mobi.huibao.notebook.ui.fragment.NotebookFragment;
import mobi.huibao.notebook.ui.fragment.UserFragment;

public class MainActivity extends RealmActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.content_view_page)
    ViewPager contentViewPage;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    SearchView searchView;

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initViewPage() {

        Fragment[] fragments = new Fragment[]{
                ContactsFragment.newInstance(),
                NotebookFragment.newInstance(),
                UserFragment.newInstance()};

        ContentViewPagerAdapter adapter = new ContentViewPagerAdapter(getSupportFragmentManager(), fragments);

        contentViewPage.setAdapter(adapter);
        contentViewPage.addOnPageChangeListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initViewPage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contacts_search, menu);
        initContactsMenu(menu);
        return true;
    }

    private void initContactsMenu(Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.menu_contact_search).getActionView();
        searchView.setQueryHint("搜索联系人");
    }

    private void initNoteMenu(Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.menu_note_search).getActionView();
        searchView.setQueryHint("搜索便签");
    }

    private void initUserMenu(Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.menu_user_search).getActionView();
        searchView.setQueryHint("搜索用户");
    }


    @Override
    public void onPageScrolled(int index, float v, int i1) {
    }

    @Override
    public void onPageSelected(int index) {
        startSupportActionMode(
                new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

                        switch (index) {
                            case 0:
                                actionMode.setTitle("联系人");
                                getMenuInflater().inflate(R.menu.contacts_search, menu);
                                initContactsMenu(menu);
                                break;
                            case 1:
                                actionMode.setTitle("便笺");
                                getMenuInflater().inflate(R.menu.note_search, menu);
                                initNoteMenu(menu);
                                break;
                            case 2:
                                actionMode.setTitle("用户");
                                getMenuInflater().inflate(R.menu.user_search, menu);
                                initUserMenu(menu);
                                break;
                        }

                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(
                            ActionMode actionMode, MenuItem menuItem) {
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode actionMode) {

                    }
                });

    }

    @Override
    public void onPageScrollStateChanged(int i) {
        LogUtils.i("onPageScrollStateChanged");
    }


    @Override
    protected void onDestroy() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
