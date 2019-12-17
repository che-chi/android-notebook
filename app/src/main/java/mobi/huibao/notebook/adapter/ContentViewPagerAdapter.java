package mobi.huibao.notebook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ContentViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] contentFragments;

    public ContentViewPagerAdapter(FragmentManager manager, Fragment[] contentFragments) {
        super(manager);
        this.contentFragments = contentFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return contentFragments[position];
    }

    @Override
    public int getCount() {
        return contentFragments.length;
    }
}
