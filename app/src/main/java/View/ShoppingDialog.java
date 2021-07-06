package View;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import fenping.szlt.com.usbhotel.R;

public class ShoppingDialog extends Dialog{

    private Activity context;
    private ImageView iv_qr;
    private ProgressBar pb_dialog;
//    private String token;

    public ShoppingDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_shopping);
        initView();
        initState();
//        getQrCode();
    }

    private void initView() {
        iv_qr = findViewById(R.id.iv_dialog_shopping_qrCode);
//        pb_dialog = findViewById(R.id.pb_dialog);
    }

    private void initState() {
//        pb_dialog.setVisibility(View.VISIBLE);
    }

    /*private void getQrCode() {
        RequestParams params = new RequestParams(Const.loginUrl);
        params.addQueryStringParameter("code", 84);
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
                    break;
            }
        }
    };
*/
}
