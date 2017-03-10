package bei.beijingnews1020.detailpager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import bei.beijingnews1020.R;
import bei.beijingnews1020.activity.MainActivity;
import bei.beijingnews1020.base.MenuDetailBasePager;
import bei.beijingnews1020.bean.NewsCenterBean;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by wangmingqiang on 2017/2/6.
 */

public class TopicMenuDetailPager extends MenuDetailBasePager {

    /**
     * 新闻详情页面的数据
     */
    private final List<NewsCenterBean.DataBean.ChildrenBean> childrenData;
    /**
     * 页签页面的集合
     */
    private ArrayList<TabDetailPager> tabDetailPagers;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;

    @InjectView(R.id.tabLayout)
    TabLayout tabLayout;

    @InjectView(R.id.ib_next)
    ImageButton ibNext;

    public TopicMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.childrenData = dataBean.getChildren();//12条
    }

    @Override
    public View initView() {
        //新闻详情页面的视图
        View view = View.inflate(mContext, R.layout.topic_menu_detail_pager, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //准备数据-页面
        tabDetailPagers = new ArrayList<>();
        //根据有多少数据创建多少个TabDetailPager，并且把数据传入到对象中
        for (int i = 0; i < childrenData.size(); i++) {
            tabDetailPagers.add(new TabDetailPager(mContext, childrenData.get(i)));
        }

        //设置适配器
        viewpager.setAdapter(new TopicMenuDetailPager.MyPagerAdapter());
//
//        //要在设置适配器之后
//        indicator.setViewPager(viewpager);
//        //监听页面的变化用TabPageIndicator
//        indicator.setOnPageChangeListener(new TopicMenuDetailPager.MyOnPageChangeListener());

        //设置
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //监听页面的变化用TabPageIndicator
        viewpager.addOnPageChangeListener(new TopicMenuDetailPager.MyOnPageChangeListener());

    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            MainActivity mainActivity= (MainActivity) mContext;

            if(position==0) {
                //可以滑动
                mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else {
                //不可以滑动
                mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @OnClick(R.id.ib_next)
    public void onClick() {
        //切换到下一个页面
        viewpager.setCurrentItem(viewpager.getCurrentItem()+1);
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return childrenData.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            tabDetailPager.initData();//不要忘记
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            return rootView;
        }
    }

}
