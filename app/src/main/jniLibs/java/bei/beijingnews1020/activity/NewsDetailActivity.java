package bei.beijingnews1020.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import bei.beijingnews1020.R;
import bei.beijingnews1020.base.BasePager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewsDetailActivity extends AppCompatActivity {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_menu)
    ImageButton ibMenu;
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.ib_textsize)
    ImageButton ibTextsize;
    @InjectView(R.id.ib_share)
    ImageButton ibShare;
    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    private String url;
    private WebSettings webSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.inject(this);

        url = getIntent().getStringExtra("url");
        //Toast.makeText(this, "url==" + url, Toast.LENGTH_SHORT).show();

        tvTitle.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);

        //发现漏洞自己添加的功能用于隐藏图组右上角的图片
        new BasePager(NewsDetailActivity.this).ibSwicth.setVisibility(View.GONE);



        //WebView的使用
        webview.loadUrl(url);

        // webview.loadUrl("http://android.atguigu.com/");测试下面的缩放

        webSettings = webview.getSettings();

        //支持javaScript脚步语言
        webSettings.setJavaScriptEnabled(true);

        //添加缩放按钮-页面要支持
        webSettings.setBuiltInZoomControls(true);

        //支持双击变大变小-页面支持
        webSettings.setUseWideViewPort(true);

        //设置监听
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressbar.setVisibility(View.GONE);
            }

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);
//
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return true;
//            }
        });
    }

    @OnClick({R.id.ib_back, R.id.ib_textsize, R.id.ib_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
                //Toast.makeText(this, "设置文字大小", Toast.LENGTH_SHORT).show();
                showChangeTextSizeDialog();
                break;
            case R.id.ib_share:
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private int tempSize=2;
    private int realSize=tempSize;

    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");

        String[] items={"超大字体","大字体","正常字体","小字体","超小字体"};

        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize=which;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                realSize=tempSize;
                changeText(realSize);
            }
        });
        builder.show();
    }

    private void changeText(int realSize) {
        switch (realSize){
            case 0:
                webSettings.setTextZoom(200);
                break;
            case 1:
                webSettings.setTextZoom(150);
                break;
            case 2:
                webSettings.setTextZoom(100);
                break;
            case 3:
                webSettings.setTextZoom(75);
                break;
            case 4:
                webSettings.setTextZoom(50);
                break;
        }
    }
}
