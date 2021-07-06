package View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import fenping.szlt.com.usbhotel.Const;
import fenping.szlt.com.usbhotel.MainActivity;
import fenping.szlt.com.usbhotel.R;
import fenping.szlt.com.usbhotel.bean.login.LoginJsonBean;
import fenping.szlt.com.usbhotel.bean.userInfo.UserInfoJsonBean;
import utils.Tools;

import static android.content.Context.MODE_PRIVATE;
import static fenping.szlt.com.usbhotel.MainActivity.mMainActivityHandler;


public class LoginDialog extends Dialog implements View.OnClickListener {

    private Activity context;
    private Button btn_dialog_authorized, btn_dialog_refresh;
    private ImageView iv_qr;
    private ProgressBar pb_dialog;
    private TextView tv_time;
    private TextView tv_refresh;
    private LinearLayout ll_text;
    private LoginDialogListener listener;
    private RefreshDataListener dataListener;
    private CountDownTimer timer;
    private CountDownTimer getUserTimer;
    private String token;

    public interface LoginDialogListener {
        void onClick(View view);
    }

    public interface RefreshDataListener{
        public void refreshPriorityUI();
    }

    public LoginDialog(@NonNull Activity context, int themeResId, LoginDialogListener listener,RefreshDataListener dataListener) {
        super(context, themeResId);
        this.context = context;
        this.listener = listener;
        this.dataListener = dataListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_login);
        initView();
        initState();
        getQrCode();
    }

    private void initView() {
        iv_qr = findViewById(R.id.iv_dialog_login_qrCode);
        pb_dialog = findViewById(R.id.pb_dialog);
        tv_time = findViewById(R.id.tv_time);
        ll_text = findViewById(R.id.ll_text);
        tv_refresh = findViewById(R.id.tv_refresh);
        btn_dialog_authorized = findViewById(R.id.btn_dialog_authorized);
        btn_dialog_refresh = findViewById(R.id.btn_dialog_refresh);
    }

    private void initState() {
        tv_refresh.setVisibility(View.GONE);
        ll_text.setVisibility(View.INVISIBLE);
        pb_dialog.setVisibility(View.VISIBLE);
        btn_dialog_authorized.setOnClickListener(this);
        btn_dialog_refresh.setOnClickListener(this);
    }

    private void getQrCode() {
        RequestParams params = new RequestParams(Const.loginUrl);
        params.addQueryStringParameter("code", Tools.getMac());
        params.addQueryStringParameter("appid", Const.APP_ID);
        params.addQueryStringParameter("secret", Const.APP_SECRET);
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("xjj", "loginUrl: onSuccess");
                Gson gson = new Gson();
                LoginJsonBean bean = gson.fromJson(result, LoginJsonBean.class);
                Message message = new Message();
                message.what = 1;
                message.obj = bean;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("xjj", "loginUrl: onError"+ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d("xjj", "loginUrl: onCancelled");
            }

            @Override
            public void onFinished() {
                Log.d("xjj", "loginUrl: onFinished");
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    LoginJsonBean bean = (LoginJsonBean) msg.obj;
                    String qrCode = bean.getData().getQrcode();
                    token = bean.getData().getToken();
                    Log.d("xjj", "token:" + token);
                    Log.d("xjj", "qrCode:" + qrCode);
                    pb_dialog.setVisibility(View.GONE);
                    iv_qr.setVisibility(View.VISIBLE);
                    Glide.with(context).load(qrCode).into(iv_qr);
                    ll_text.setVisibility(View.VISIBLE);
                    startTimer();
                    getUserInfo();
                    break;
            }
        }
    };

    private void getUserInfo() {
        getUserTimer = new CountDownTimer(60 * 1000, 5 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                RequestParams params = new RequestParams(Const.getUserUrl);
                params.addQueryStringParameter("token", token);
                params.addQueryStringParameter("appid", Const.APP_ID);
                params.addQueryStringParameter("secret", Const.APP_SECRET);
                params.addHeader("Content-Type", "application/x-www-form-urlencoded");
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("xjj", "getUserUrl: onSuccess");
                        getUserTimer.cancel();
                        dismiss();
                        Gson gson = new Gson();
                        UserInfoJsonBean bean = gson.fromJson(result, UserInfoJsonBean.class);
                        String gold_bean = bean.getData().getGold_bean()+"";
                        String silver_bean = bean.getData().getSilver_bean();
                        SharedPreferences.Editor editor = context.getSharedPreferences("user", MODE_PRIVATE).edit();
                        editor.putBoolean("isLogin",true);
                        editor.putString("gold_bean", gold_bean);
                        editor.putString("silver_bean", silver_bean);
                        editor.putString("token", token);
                        editor.apply();
                        Toast.makeText(context,"恭喜你 登陆成功！",Toast.LENGTH_LONG).show();
                        dataListener.refreshPriorityUI();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.d("xjj", "getUserUrl: onError"+ex.toString());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Log.d("xjj", "getUserUrl: onCancelled");
                    }

                    @Override
                    public void onFinished() {
                        Log.d("xjj", "getUserUrl: onFinished");
                    }
                });
            }

            @Override
            public void onFinish() {

            }
        };
        getUserTimer.start();
    }

    private void startTimer() {
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String time = null;
                if (millisUntilFinished / 1000 < 10) {
                    time = "0" + millisUntilFinished / 1000;
                } else {
                    time = millisUntilFinished / 1000 + "";
                }
                tv_time.setText(time);
            }

            @Override
            public void onFinish() {
                iv_qr.setVisibility(View.GONE);
                tv_refresh.setVisibility(View.VISIBLE);
                ll_text.setVisibility(View.INVISIBLE);
            }
        };
        timer.start();
    }

    public void refreshQrCode() {
        if (timer != null) {
            timer.cancel();
        }
        if (getUserTimer != null) {
            getUserTimer.cancel();
        }
        iv_qr.setImageDrawable(null);
        pb_dialog.setVisibility(View.VISIBLE);
        ll_text.setVisibility(View.INVISIBLE);
        tv_refresh.setVisibility(View.GONE);
        getQrCode();
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }
}
