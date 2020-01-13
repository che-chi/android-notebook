package mobi.huibao.notebook.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mobi.huibao.notebook.R;
import mobi.huibao.notebook.adapter.ContentViewPagerAdapter;
import mobi.huibao.notebook.api.data.ContactsResult;
import mobi.huibao.notebook.events.SearchContactEvent;
import mobi.huibao.notebook.index.IndexTool;
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
        searchView = (SearchView) menu.findItem(R.id.menu_contact_search).getActionView();
        searchView.setQueryHint("搜索联系人");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                Observable.create((ObservableOnSubscribe<List<ContactsResult.ContactsItem>>) e -> {
                    List<ContactsResult.ContactsItem> contactsItemList = IndexTool.searchContact(text);
                    e.onNext(contactsItemList);
                }).subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(
                                contactsItemList -> {
                                    SearchContactEvent searchContactEvent = new SearchContactEvent(contactsItemList);
                                    EventBus.getDefault().post(searchContactEvent);
                                }
                        ).isDisposed();
                LogUtils.d("onQueryTextSubmit->" + text);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                LogUtils.d("onQueryTextChange->" + text);
                return true;
            }
        });
        return true;
    }

    @Override
    public void onPageScrolled(int index, float v, int i1) {
    }

    @Override
    public void onPageSelected(int index) {
        switch (index) {
            case 0:
                toolbar.setVisibility(View.VISIBLE);
                toolbar.setTitle("联系人");
                searchView.setQueryHint("搜索联系人");
                break;
            case 1:
                toolbar.setVisibility(View.VISIBLE);
                toolbar.setTitle("便签");
                searchView.setQueryHint("搜索便签");
                break;
            case 2:
                toolbar.setVisibility(View.GONE);
                searchView.setQueryHint("搜索用户");
                break;
        }

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
