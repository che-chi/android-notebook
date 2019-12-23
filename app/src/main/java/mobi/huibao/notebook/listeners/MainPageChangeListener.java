package mobi.huibao.notebook.listeners;

import android.support.v4.view.ViewPager;

import com.blankj.utilcode.util.LogUtils;

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

    }

    @Override
    public void onPageScrollStateChanged(int i) {
        LogUtils.d("onPageScrollStateChanged");
    }

}
