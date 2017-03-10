package bei.beijingnews1020.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

/**
 * Created by wangmingqiang on 2017/2/10.
 * 三级缓存的工具类
 */
 /* 三级缓存设计步骤：
    从内存中取图片
    从本地文件中取图片
    向内存中保持一份
    请求网络图片，获取图片，显示到控件上
    向内存存一份
    向本地文件中存一份*/

public class BitmapCacheUtils {


    //网络缓存工具类
    private NetCacheUtils netCacheUtils;
    //本地缓存
    private LocalCacheUtils localCacheUtils;
    //内存缓存
    private MemoryCacheUtils memoryCacheUtils;

    public BitmapCacheUtils(Handler handler){

        memoryCacheUtils=new MemoryCacheUtils();

        localCacheUtils=new LocalCacheUtils(memoryCacheUtils);

        netCacheUtils=new NetCacheUtils(handler,localCacheUtils,memoryCacheUtils);
    }

    public Bitmap getBitmapFromNet(String url, int position) {

        //1.从内存中取图片
        if(memoryCacheUtils!=null) {
            Bitmap bitmap=memoryCacheUtils.getBitmapFromUrl(url);
            if(bitmap!=null) {
                Log.e("TAG", "内存缓存图片成功==" + position);
                return bitmap;
            }
        }


        //2.从本地文件中取图片
        if(localCacheUtils!=null) {
            Bitmap bitmap=localCacheUtils.getBitmapFromUrl(url);
            if(bitmap!=null) {
                Log.e("TAG", "本地缓存图片成功==" + position);
                return bitmap;
            }
        }

        //3.请求网络图片，获取图片，显示到控件上
        netCacheUtils.getBitmapFroment(url,position);
        return null;
    }
}
