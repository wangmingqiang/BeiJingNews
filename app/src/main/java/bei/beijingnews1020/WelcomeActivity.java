package bei.beijingnews1020;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import bei.beijingnews1020.activity.GuideActivity;
import bei.beijingnews1020.activity.MainActivity;
import bei.beijingnews1020.utils.CacheUtils;

public class WelcomeActivity extends AppCompatActivity {

    private RelativeLayout activity_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        activity_welcome= (RelativeLayout) findViewById(R.id.activity_welcome);

        //设置旋转 渐变 缩放动画
        RotateAnimation ra=new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(2000);//设置持续时长
        ra.setFillAfter(true);//设置停留在旋转后的界面

        AlphaAnimation aa=new AlphaAnimation(0,1);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        ScaleAnimation sa=new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(2000);
        sa.setFillAfter(true);

        AnimationSet set=new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.addAnimation(ra);

        //开始播放动画
        activity_welcome.startAnimation(set);

        //监听动画完成
        set.setAnimationListener(new MyAnimationListener());

    }
    class MyAnimationListener implements Animation.AnimationListener{
        /**
         * 当动画开始播放的时候回调
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * 当动画结束的时候回调
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {

            boolean startMain= CacheUtils.getBoolean(WelcomeActivity.this,"start_main");
            Intent intent=null;
            if(startMain) {
                intent=new Intent(WelcomeActivity.this, MainActivity.class);
            }else {
                intent=new Intent(WelcomeActivity.this,GuideActivity.class);

            }
            startActivity(intent);
            finish();
        }

        /**
         * 当动画重复播放的时候回调
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
