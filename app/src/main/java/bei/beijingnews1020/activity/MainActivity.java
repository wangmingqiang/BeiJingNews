package bei.beijingnews1020.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.Timer;
import java.util.TimerTask;

import bei.beijingnews1020.R;
import bei.beijingnews1020.fragment.ContentFragment;
import bei.beijingnews1020.fragment.LeftMenuFragment;
import bei.beijingnews1020.utils.DensityUtil;

public class MainActivity extends SlidingFragmentActivity {

    public static final String LEFTMENU_TAG = "leftmenu_tag";
    public static final String CONTENT_TAG = "content_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActivityCompat.requestPermissions(MainActivity.this,new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        //解决虚拟按钮不能全屏的适配问题
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //1.设置主页面
        setContentView(R.layout.activity_main);

        //2.设置左侧菜单
        setBehindContentView(R.layout.leftmenu);

        //3.设置右侧菜单
        SlidingMenu slidingMenu=getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.leftmenu);

        //4.设置模式：左侧+主页，左侧+主页+右侧，主页+右侧
        slidingMenu.setMode(SlidingMenu.LEFT);

        //5.设置滑动模式：全屏，边缘，不可滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //6.设置主页占得宽度dip
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));

        verifyStoragePermissions(this);



        initFragment();
    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        boolean writeAccepted = false;
//        switch (requestCode) {
//            case 1:
//                writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                if (writeAccepted) {
//                    String state = Environment.getExternalStorageState();
//                    if (Environment.MEDIA_MOUNTED.equals(state)) {
//                        String dir= Environment.getExternalStorageDirectory()+ "/beijingnews/image/";
//                        File dirs = new File(dir);
//                        if (!dirs.exists()) {
//                            dirs.mkdirs();
//                        }
//                    }
//                }
//                break;
//        }
//    }



    //初始化fragment
    private void initFragment() {
        //1.得到事物
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        //2.替换
        ft.replace(R.id.fl_leftmenu,new LeftMenuFragment(), LEFTMENU_TAG);
        ft.replace(R.id.fl_content,new ContentFragment(), CONTENT_TAG);
        //3.提交
        ft.commit();
    }

    //得到左侧菜单
    public LeftMenuFragment getLeftMenuFragment() {

        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFTMENU_TAG);
    }

    //得到ContentFragment
    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(CONTENT_TAG);
    }
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }


}
