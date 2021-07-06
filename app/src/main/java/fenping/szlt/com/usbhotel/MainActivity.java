package fenping.szlt.com.usbhotel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.evilbinary.tv.widget.BorderEffect;
import org.evilbinary.tv.widget.BorderView;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import View.NetView;
import View.VPView;
import View.TimeView;
import View.ScrollTextView;
import View.LoginDialog;
import View.ShoppingDialog;
import fenping.szlt.com.usbhotel.bean.userInfo.UserInfoJsonBean;
import fenping.szlt.com.usbhotel.recyc.DemoRecyclerViewActivity;
import utils.SettingUtils;
import utils.Tools;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BorderView border;
    private RelativeLayout main, rl_login;
    public static MainActivityHandler mMainActivityHandler = null;
    private static MainActivity instance = null;
    private VPView picAndVideo;
    private ScrollTextView scroll;
    private TimeView time;
    private boolean isstart;
    private String temp = "";
    private ViewGroup.LayoutParams layoutParams1;
    public static boolean captureLogThreadOpen = true;
    boolean isStop;
    public static boolean isLogin;
    ImageView app_1;
    ImageView app_2;
    ImageView app_3;
    ImageView app_4;
    ImageView app_5;
    ImageView app_6;
    ImageView app_7;
    ImageView app_8;
    NetView net;
    ArrayList<ImageView> imageViews;
    boolean isjiaoyan = false;
    ImageView clean;
    ImageView set;
    private LoginDialog loginDialog;
    private ShoppingDialog shoppingDialog;
    private ImageView iv_login;
    private TextView tv_login,tv_golden_bean,tv_golden_bean_count,tv_silver_bean,tv_silver_bean_count;

    public static void StratActivity(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getLog();
        startService(new Intent(this, DataService.class));
        isstart = true;
        this.instance = this;
        initView();
        initBorder();
//        getUserInfo();
    }

    private void getUserInfo() {
//        Log.e("首次更新", "getUserInfo: " );
        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
        String token = sp.getString("token","null");
//        Log.e("首次更新", "getUserInfo token: " + token );
        if(!token.equals("null")){
            RequestParams params = new RequestParams(Const.getUserUrl);
            params.addQueryStringParameter("token", token);
            params.addQueryStringParameter("appid", Const.APP_ID);
            params.addQueryStringParameter("secret", Const.APP_SECRET);
            params.addHeader("Content-Type", "application/x-www-form-urlencoded");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.d("xjj", "auto login success");
                    Gson gson = new Gson();
                    UserInfoJsonBean bean = gson.fromJson(result, UserInfoJsonBean.class);
                    String gold_bean = bean.getData().getGold_bean();
                    String silver_bean = bean.getData().getSilver_bean();
                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                    isLogin = true;
                    editor.putBoolean("isLogin",true);
                    editor.putString("gold_bean", gold_bean);
                    editor.putString("silver_bean", silver_bean);
                    editor.apply();
                    Message message = new Message();
                    message.what = 3;
                    mMainActivityHandler.sendMessage(message);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    isLogin = false;
                    Message message = new Message();
                    message.what = 3;
                    mMainActivityHandler.sendMessage(message);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    private void initUserLayout() {
        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
        if(!isLogin){
            iv_login.setImageResource(R.drawable.unlogin);
            tv_login.setVisibility(View.VISIBLE);
            tv_golden_bean.setVisibility(View.GONE);
            tv_golden_bean_count.setVisibility(View.GONE);
            tv_silver_bean.setVisibility(View.GONE);
            tv_silver_bean_count.setVisibility(View.GONE);
        }else{
            String gold_bean = sp.getString("gold_bean",0+"");
            String silver_bean = sp.getString("silver_bean",0+"");
            iv_login.setImageResource(R.drawable.login);
            tv_login.setVisibility(View.GONE);
            tv_golden_bean.setVisibility(View.VISIBLE);
            tv_golden_bean_count.setText(gold_bean);
            tv_golden_bean_count.setVisibility(View.VISIBLE);
            tv_silver_bean.setVisibility(View.VISIBLE);
            tv_silver_bean_count.setText(silver_bean);
            tv_silver_bean_count.setVisibility(View.VISIBLE);
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }

    private void initBorder() {
        border = new BorderView(this);
        border.setBackgroundResource(R.drawable.shape_app_focus);
        border.getEffect().setScale(1.04f);
        border.getEffect().setMargin(0);
        main = (RelativeLayout) findViewById(R.id.main);
        border.attachTo(main);
        border.attachTo(rl_login);
    }

    private void initView() {
        //处理主页
        mMainActivityHandler = new MainActivityHandler();
        time = (TimeView) findViewById(R.id.time);
        net = (NetView) findViewById(R.id.net);
        picAndVideo = (VPView) findViewById(R.id.ad);
        picAndVideo.setOnClickListener(this);
        scroll = (ScrollTextView) findViewById(R.id.scroll);
        app_1 = (ImageView) findViewById(R.id.app_1);
        app_2 = (ImageView) findViewById(R.id.app_2);
        app_3 = (ImageView) findViewById(R.id.app_3);
        app_4 = (ImageView) findViewById(R.id.app_4);
        app_5 = (ImageView) findViewById(R.id.app_5);
        app_6 = (ImageView) findViewById(R.id.app_6);
        app_7 = (ImageView) findViewById(R.id.app_7);
        app_8 = (ImageView) findViewById(R.id.app_8);
        clean = (ImageView) findViewById(R.id.clean);
        rl_login = findViewById(R.id.rl_login);
        iv_login = findViewById(R.id.iv_login);
        tv_login = findViewById(R.id.tv_login);
        tv_golden_bean = findViewById(R.id.tv_golden_bean);
        tv_golden_bean_count = findViewById(R.id.tv_golden_bean_count);
        tv_silver_bean = findViewById(R.id.tv_silver_bean);
        tv_silver_bean_count = findViewById(R.id.tv_silver_bean_count);
        clean.setOnClickListener(this);
        set = (ImageView) findViewById(R.id.set);
        set.setOnClickListener(this);
        imageViews = new ArrayList<>();
        imageViews.add(app_1);
        imageViews.add(app_2);
        imageViews.add(app_3);
        imageViews.add(app_4);
        imageViews.add(app_5);
        imageViews.add(app_6);
        imageViews.add(app_7);
        imageViews.add(app_8);
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(10);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setOnClickListener(this);
            //Glide.with(this).load(R.drawable.app2).apply(options).into(imageViews.get(i));
        }
        //判断是否有网
//        Log.e("IsGetNetWork", "initView: IsGetNetWork : " + Tools.IsGetNetWork(this) );
        if (Tools.IsGetNetWork(this)) {
//            Log.e("IsGetNetWork", "initView: IsGetNetWork"  );
            initData();
            isjiaoyan = true;


        }
        net.setCallBack(new NetView.CallBack() {
            @Override
            public void connect() {
                getUserInfo();
                if (!isjiaoyan) {
                    mMainActivityHandler.sendEmptyMessageDelayed(1, 5000);
                }
            }

            @Override
            public void disconnect() {

            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (isstart && hasFocus) {
            isstart = false;
            picAndVideo.requestFocus();
            border.getEffect().onFocusChanged(picAndVideo, picAndVideo, picAndVideo);
            border.getEffect().addOnFocusChanged(new BorderEffect.FocusListener() {
                @Override
                public void onFocusChanged(View oldFocus, View newFocus) {
                    if (newFocus.getId() == R.id.set || newFocus.getId() == R.id.clean) {
                        border.setBackgroundResource(R.color.transparent);
                    } else {
                        border.setBackgroundResource(R.drawable.shape_app_focus);
                    }
                }
            });
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 23) {
            //showMac();
        } else if (keyCode == 8 || keyCode == 9 || keyCode == 10 || keyCode == 13) {
            Log.i("key", "keyCode==" + keyCode);
            mMainActivityHandler.removeMessages(2);
            mMainActivityHandler.sendEmptyMessageDelayed(2, 2000);
            temp += (keyCode - 7);
            Log.i("key", "temp==" + temp);
            Log.i("key", "temp.length()==" + temp.length());
            if (temp.length() >= 3 && "123".equals(temp)) {
                temp = "";
                showMac();
            } else if (temp.length() >= 3 && "163".equals(temp)) {
                Log.i("key", "发送消息");
                DataService.mHander.removeMessages(Const.show);
                DataService.mHander.sendEmptyMessage(Const.show);
            } else if (temp.length() >= 3 && "361".equals(temp)) {
                Log.i("key", "发送消息");
                DataService.mHander.removeMessages(Const.disshow);
                DataService.mHander.sendEmptyMessage(Const.disshow);
            } else if (temp.length() >= 4 && "1221".equals(temp)) {
                Log.i("key", "发送消息");
                startActivity(new Intent(this, DemoRecyclerViewActivity.class));
            }
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showMac() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();//创建对话框
        dialog.setIcon(R.mipmap.ic_launcher);//设置对话框icon
        dialog.setTitle("MAC");//设置对话框标题
        dialog.setMessage("当前机器MAC为" + Tools.getMac());//设置文字显示内容
        //分别设置三个button
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭对话框
            }
        });
        dialog.show();
    }

    private void initData() {
        String oldData = SettingUtils.getOldData(getApplicationContext());
        if (!Tools.isEmpty(oldData)) {
            AD_data ad_data;

            try {
                ad_data = JSON.parseObject(oldData, AD_data.class);
            } catch (Exception e) {
                return;
            }
            if (!Tools.isEmpty(ad_data)) {
                List<AD_data.AdBean> ad = ad_data.getAd();
                ArrayList<AD_data.AdBean> addata = new ArrayList<>();
                ArrayList<AD_data.AdBean> scrolltext = new ArrayList<>();
                if (Tools.isEmpty(ad)) {
                    return;
                }
                for (int i = 0; i < ad.size(); i++) {
                    AD_data.AdBean adBean = ad.get(i);
                    String type = adBean.getType();
                    if (Const.PIC_AND_VIDEO.equals(type)) {
                        if (!addata.contains(adBean)) {
                            addata.add(adBean);
                        }
                    } else if (Const.TEXT_TYPE.equals(type)) {
                        if (!scrolltext.contains(adBean)) {
                            scrolltext.add(adBean);
                        }
                    }
                }
                picAndVideo.changeDatas(addata);
                scroll.setDatas(scrolltext);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    class MainActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://通知界面更新
                    Log.d("xjjj","case 1");
                    //
                    initData();
                    break;
                case 2:
                    //
                    temp = "";
                    break;
                case 10000://通知界面更新
                    if (!Tools.isEmpty(picAndVideo)) {
                        picAndVideo.fileDown((AD_data.AdBean) msg.obj);
                    }
                    break;
                case 3://gold_bean=10.00, lock_gold_bean='3970.00', silver_bean='3650'
//                    Log.e("更新数据", "handleMessage: "  );
                    initUserLayout();
                    break;
                    /*
                    * id=140365, name='c_84_85133', nickname='jjwan',
                    * head_image='https://gzoss.hehekeji.cn/data/users/head_images/16239175456029608.jpg',
                    * gold_bean=10.00, lock_gold_bean='3970.00', silver_bean='3650', state='1',
                    *  created_at='2021-05-27 11:12:13', deleted_at='null'}*/
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.app_1:
//                startWeb("https://v6test.hehekeji.cn/company/84#/surroundlife");
                myStartActivity(Const.apps[1]);
                break;
            case R.id.app_2:
                if(!isLogin){
                    loginDialog = new LoginDialog(MainActivity.this, R.style.MyDialog, new LoginDialog.LoginDialogListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.btn_dialog_authorized:
                                    break;
                                case R.id.btn_dialog_refresh:
                                    loginDialog.refreshQrCode();
                                    break;
                            }
                        }
                    }, new LoginDialog.RefreshDataListener() {
                        @Override
                        public void refreshPriorityUI() {
                            isLogin = true;
                            initUserLayout();
                        }
                    });
                    loginDialog.show();
                }
                break;
            case R.id.app_3:
//                app_3.setImageResource(R.drawable.app3_qr);
                if (shoppingDialog == null){
                    shoppingDialog = new ShoppingDialog(MainActivity.this,R.style.MyDialog);
                }
                if (!shoppingDialog.isShowing()){
                    shoppingDialog.show();
                }
                break;
            case R.id.app_4:
                myStartActivity(Const.apps[3]);
                break;
            case R.id.app_5:
                myStartActivity(Const.apps[4]);
                break;
            case R.id.app_6:
                myStartActivity(Const.apps[5]);
                break;
            case R.id.app_7:
                myStartActivity(Const.apps[6]);
                break;
            case R.id.app_8:
                myStartActivity(Const.apps[7]);
                break;
            case R.id.set:
                myStartActivity(Const.apps[8]);
                break;
            case R.id.clean:
                myStartActivity(Const.apps[9]);
                break;
            case R.id.ad:
                Intent intent = new Intent(MainActivity.this, FullScreenActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void startWeb(String url){
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
    }

    public void myStartActivity(String packname) {
        Intent intent = null;

        if ("com.android.tv.settings".equals(packname) || "com.android.settings".equals(packname)) {

            boolean b = openAPk(this, "com.android.tv.settings");
            if (!b) {
                openAPk(this, "com.android.settings");
            }
        } else if ("allapp".equals(packname)) {
            startActivity(new Intent(this, DemoRecyclerViewActivity.class));
        } else {
            intent = getPackageManager().getLaunchIntentForPackage(packname);
            if (intent != null)
                startActivity(intent);
        }
    }

    public static boolean openAPk(Context context, String packagename) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packagename);
        if (intent == null) {
            return false;
        } else {
            context.startActivity(intent);
        }
        return true;
    }

    private void getLog() {
        String logPath = "/sdcard/log.txt";
        File file = new File(logPath);
        if (file.exists()) {
            file.delete();
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(60 * 1000);
                    Log.d("xjj", "captureLogThreadOpen = false");
                    captureLogThreadOpen = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                super.run();
                FileOutputStream fos;
                File logFile;
                int logCount = 0;
                @SuppressLint("SdCardPath")
                String logPath = "/sdcard/";
                while (captureLogThreadOpen) {
//                    Log.d("xjj", "start write log");
                    try {
                        /*命令的准备*/
                        ArrayList<String> getLog = new ArrayList<String>();
                        getLog.add("logcat");
                        getLog.add("-d");
                        getLog.add("-v");
                        getLog.add("time");
                        ArrayList<String> clearLog = new ArrayList<String>();
                        clearLog.add("logcat");
                        clearLog.add("-c");
                        Process process = Runtime.getRuntime().exec(getLog.toArray(new String[getLog.size()]));//抓取当前的缓存日志
                        BufferedReader buffRead = new BufferedReader(new InputStreamReader(process.getInputStream()));//获取输入流
                        Runtime.getRuntime().exec(clearLog.toArray(new String[clearLog.size()]));//清除是为了下次抓取不会从头抓取
                        String str = null;
                        logFile = new File(logPath + "log.txt");//打开文件
                        fos = new FileOutputStream(logFile, true);//true表示在写的时候在文件末尾追加
                        String newline = System.getProperty("line.separator");//换行的字符串
                        while ((str = buffRead.readLine()) != null) {//循环读取每一行
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-");
                            Date date = new Date(System.currentTimeMillis());
                            String time = format.format(date);
                            fos.write((time + str).getBytes());//加上年
                            fos.write(newline.getBytes());//换行
                            logCount++;
                            if (!captureLogThreadOpen) {
                                fos.close();
                                break;
                            }
                        }
                        fos.close();
                        fos = null;
                        Runtime.getRuntime().exec(clearLog.toArray(new String[clearLog.size()]));
                    } catch (Exception e) {

                    }

                }
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        picAndVideo.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scroll.setStopScroll(true);
        scroll.clearDraw();
    }

    @Override
    protected void onStop() {
        super.onStop();
        picAndVideo.stop();
        isStop = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
