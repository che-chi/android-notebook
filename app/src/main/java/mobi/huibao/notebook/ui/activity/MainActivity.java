package mobi.huibao.notebook.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;

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

    @BindView(R.id.content_view_page)
    ViewPager contentViewPage;

    SearchView searchView;

    public SearchView getSearchView() {
        return searchView;
    }

//    private void initOptionMenu(Menu menu) {
//        searchView = (SearchView) menu.findItem(R.id.menu_note_search).getActionView();
//        searchView.setQueryHint("搜索联系人");
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        initOptionMenu(menu);
//        return true;
//    }

    private void initViewPage() {

        Fragment[] fragments = new Fragment[]{ContactsFragment.newInstance(),
                new NotebookFragment(),
                new UserFragment()};

        ContentViewPagerAdapter adapter = new ContentViewPagerAdapter(getSupportFragmentManager(), fragments);

        contentViewPage.setAdapter(adapter);
        contentViewPage.addOnPageChangeListener(new MainPageChangeListener(this));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
