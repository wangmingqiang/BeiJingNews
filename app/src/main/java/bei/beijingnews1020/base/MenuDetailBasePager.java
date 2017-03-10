package bei.beijingnews1020.base;

import android.content.Context;
import android.view.View;

/**
 * Created by wangmingqiang on 2017/2/6.
 * 试图 的基类
 *  NewsMenuDetailPager、TopicMenuDetailPager、PhotosMenuDetailPager、InteractMenuDetailPager
 *  继承该类
 */

public abstract class MenuDetailBasePager {

    public final Context mContext;


    //代表各个菜单详情页面的实例，视图
    public View rootView;

    public MenuDetailBasePager(Context context){
        this.mContext=context;
        rootView=initView();
    }

    //子类实现该方法。初始化子类的视图
    public abstract View initView();

    /**
     绑定数据或者请求数据再绑定数据
     */
    public void  initData(){

    }


}
