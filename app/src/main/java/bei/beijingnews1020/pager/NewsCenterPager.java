package bei.beijingnews1020.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import bei.beijingnews1020.activity.MainActivity;
import bei.beijingnews1020.base.BasePager;
import bei.beijingnews1020.base.MenuDetailBasePager;
import bei.beijingnews1020.bean.NewsCenterBean;
import bei.beijingnews1020.detailpager.InteractMenuDetailPager;
import bei.beijingnews1020.detailpager.NewsMenuDetailPager;
import bei.beijingnews1020.detailpager.PhotosMenuDetailPager;
import bei.beijingnews1020.detailpager.TopicMenuDetailPager;
import bei.beijingnews1020.fragment.LeftMenuFragment;
import bei.beijingnews1020.utils.CacheUtils;
import bei.beijingnews1020.utils.Constants;

/**
 * Created by wangmingqiang on 2017/2/6.
 */

public class NewsCenterPager extends BasePager {
    private ArrayList<MenuDetailBasePager> menuDetailBasePagers;
    //左侧菜单对应的数据
    private List<NewsCenterBean.DataBean> dataBeanList;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //显示菜单按钮
        ib_menu.setVisibility(View.VISIBLE);
        //设置标题
        tv_title.setText("新闻");

        //实例视图
        TextView textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setText("新闻中心");
        textView.setTextColor(Color.RED);

        //和父类的FrameLayout结合
        fl_main.addView(textView);

        String savaJson=CacheUtils.getString(mContext,Constants.NEWSCENTER_PAGER_URL);
        if(!TextUtils.isEmpty(savaJson)) {
            processData(savaJson);
        }

        //联网请求
        getDataFroment();
    }

    private void getDataFroment() {

        RequestParams params=new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(mContext,Constants.NEWSCENTER_PAGER_URL,result);
                Log.e("TAG","请求成功=="+result);
                //处理数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","请求失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG","onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG","onFinished==");

            }
        });
    }

    //解析数据 绑定数据
    private void processData(String json) {
        //1.解析数据：手动解析（用系统的Api解析）和第三方解析json的框架（Gson,fastjson）
        Gson gson=new Gson();
        NewsCenterBean centerBean=gson.fromJson(json,NewsCenterBean.class);
        dataBeanList = centerBean.getData();
        Log.e("TAG","新闻中心解析成功="+dataBeanList.get(0).getChildren().get(0).getTitle());

        //把新闻中心的数据传递给左侧的菜单
        MainActivity mainActivity= (MainActivity) mContext;
        //得到左侧菜单
        LeftMenuFragment leftMunuFragment=mainActivity.getLeftMenuFragment();

        //调用LeftMunuFragment的setData
        leftMunuFragment.setData(dataBeanList);

        //2.绑定数据
        menuDetailBasePagers=new ArrayList<>();
        menuDetailBasePagers.add(new NewsMenuDetailPager(mainActivity,dataBeanList.get(0)));
        menuDetailBasePagers.add(new TopicMenuDetailPager(mainActivity,dataBeanList.get(0)));
        menuDetailBasePagers.add(new PhotosMenuDetailPager(mainActivity,dataBeanList.get(2)));
        menuDetailBasePagers.add(new InteractMenuDetailPager(mainActivity));




        switchPager(0);
    }

    //根据位置切换到不同的详情页面
    public void switchPager(int prePosition) {
        //设置标题
        tv_title.setText(dataBeanList.get(prePosition).getTitle());

        MenuDetailBasePager menuDetailBasePager=menuDetailBasePagers.get(prePosition);
        menuDetailBasePager.initData();
        //视图
        View rootView=menuDetailBasePager.rootView;
        fl_main.removeAllViews();//移除之前的
        fl_main.addView(rootView);

        if(prePosition==2) {
            ibSwicth.setVisibility(View.VISIBLE);
            //设置点击事件
            ibSwicth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PhotosMenuDetailPager photosMenuDetailPager = (PhotosMenuDetailPager) menuDetailBasePagers.get(2);
                    photosMenuDetailPager.swichListGrid(ibSwicth);

                }
            });
        }else {
            ibSwicth.setVisibility(View.GONE);
        }
    }
}
