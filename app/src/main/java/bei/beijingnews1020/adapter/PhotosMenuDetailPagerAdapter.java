package bei.beijingnews1020.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bei.beijingnews1020.R;
import bei.beijingnews1020.activity.PicassoSampleActivity;
import bei.beijingnews1020.bean.PhotosMenuDetailPagerBean;
import bei.beijingnews1020.utils.BitmapCacheUtils;
import bei.beijingnews1020.utils.Constants;
import bei.beijingnews1020.utils.NetCacheUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wangmingqiang on 2017/2/9.
 */

public class PhotosMenuDetailPagerAdapter extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapter.ViewHolder> {


    private final RecyclerView recyclerview ;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetCacheUtils.SECUSS://请求成功

                    int position=msg.arg1;
                    Bitmap bitmap= (Bitmap) msg.obj;
                    if(recyclerview.isShown()) {
                        ImageView ivIcon= (ImageView) recyclerview.findViewWithTag(position);
                        if(ivIcon!=null&& bitmap!=null) {
                            Log.e("TAG","网络缓存图片显示成功"+position);
                            ivIcon.setImageBitmap(bitmap);
                        }
                    }

                    break;
                case NetCacheUtils.FAIL://失败
                    position=msg.arg1;
                    Log.e("TAG","网络缓存图片显示成功"+position);
                    break;
            }
        }
    };
    private final Context mContext;
    private final List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas;

    private BitmapCacheUtils bitmapCacheUtils;



    public PhotosMenuDetailPagerAdapter(Context mContext, List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> news, RecyclerView recyclerview) {
        this.mContext = mContext;
        this.datas = news;
        this.recyclerview=recyclerview;
        bitmapCacheUtils=new BitmapCacheUtils(handler);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = View.inflate(mContext, R.layout.item_photosmenu_detail_pager, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //根据位置得到数据
        PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity=datas.get(position);
        holder.tvTitle.setText(newsEntity.getTitle());
        //1.Glide加载图片
       /* Glide.with(mContext).load(Constants.BASE_URL+newsEntity.getListimage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.news_pic_default)
                .error(R.drawable.news_pic_default)
                .into(holder.ivIcon);*/
        //2.Bitmap加载图片
        holder.ivIcon.setTag(position);
        Bitmap bitmap=bitmapCacheUtils.getBitmapFromNet(Constants.BASE_URL+newsEntity.getListimage(),position);
        if(bitmap!=null) {//内存或者本地
            Log.e("TAG","我是从本地得到的"+bitmap);
            holder.ivIcon.setImageBitmap(bitmap);

        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);

            //设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, PicassoSampleActivity.class);
                    intent.putExtra("url",Constants.BASE_URL+datas.get(getLayoutPosition()).getListimage());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
