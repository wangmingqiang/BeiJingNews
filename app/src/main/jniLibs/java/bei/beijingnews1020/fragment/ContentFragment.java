package bei.beijingnews1020.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import bei.beijingnews1020.R;
import bei.beijingnews1020.activity.MainActivity;
import bei.beijingnews1020.base.BaseFragment;
import bei.beijingnews1020.base.BasePager;
import bei.beijingnews1020.pager.HomePager;
import bei.beijingnews1020.pager.NewsCenterPager;
import bei.beijingnews1020.pager.SettingPager;
import bei.beijingnews1020.view.NoScrollViewPager;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wangmingqiang on 2017/2/6.
 */

public class ContentFragment extends BaseFragment {
    @InjectView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_content, null);
        //把view注入到ButterKnife
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();


        //初始化3个页面
        initPager();

        setAdapter();

        initListener();
    }

    private void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //先设置默认不可滑动
                MainActivity mainActivity= (MainActivity) mContext;
                mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                switch (checkedId){
                    case R.id.rb_home:
                        viewpager.setCurrentItem(0,false);
                        break;
                    case R.id.rb_news:
                        viewpager.setCurrentItem(1,false);
                        mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        break;
                    case R.id.rb_setting:
                        viewpager.setCurrentItem(2,false);
                        break;
                }
            }
        });
        rgMain.check(R.id.rb_news);
        //监听页面的选中
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        basePagers.get(1).initData();//孩子的视图和父类的FrameLayout结合
    }

    //得到新闻中心
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //调用initData
            basePagers.get(position).initData();//孩子的视图和父类的FrameLayout结合
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 设置ViewPager的适配器
     */
    private void setAdapter() {
        viewpager.setAdapter(new MyPagerAdapter());
    }
    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position);//HomePager、NewsCenterPager,SetttingPager

            View rootView = basePager.rootView;//代表不同页面的实例

//            //调用initData
//            basePager.initData();

            container.addView(rootView);

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void initPager() {
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(mContext));//主页
        basePagers.add(new NewsCenterPager(mContext));//新闻中心
        basePagers.add(new SettingPager(mContext));//设置中心
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
