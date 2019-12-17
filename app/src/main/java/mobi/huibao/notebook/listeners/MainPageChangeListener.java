package mobi.huibao.notebook.listeners;

import android.support.v4.view.ViewPager;
import com.blankj.utilcode.util.LogUtils;
import mobi.huibao.notebook.R;
import mobi.huibao.notebook.ui.activity.MainActivity;

public class MainPageChangeListener implements ViewPager.OnPageChangeListener {

    private final MainActivity activity;

    public MainPageChangeListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        LogUtils.d("onPageScrolled");
    }

    @Override
    public void onPageSelected(int i) {
        switch (i) {
            case 0:
                activity.getToolbar().setTitle(R.string.contacts_title);
                activity.getSearchView().setQueryHint(this.activity.getString(R.string.search_contacts_query_hint));
                break;
            case 1:
                activity.getToolbar().setTitle(R.string.notebook_title);
                this.activity.getSearchView().setQueryHint(this.activity.getString(R.string.search_notebook_query_hint));
                break;
            case 2:
                activity.getToolbar().setTitle(R.string.user_title);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        LogUtils.d("onPageScrollStateChanged");
    }

}
