package kh.nemo.galix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nemo.nsdk.inteface.IAPDelegate;
import com.nemo.nsdk.inteface.IGameOauthListener;
import com.nemo.nsdk.inteface.OnSingleClickListener;
import com.nemo.nsdk.object.IAPGameData;
import com.nemo.nsdk.oidc.GameCommont;
import com.nemo.nsdk.oidc.NemoSDK;
import com.nemo.nsdk.oidc.SdkConfig;
import com.nemo.nsdk.tracking.Tracking;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button btnDangNhap;
    private Button btnAccessToken;
    private Button btnShowInfoUser;
    private Button btnDangXuat;
    private Button btnShowIAP;

    NemoSDK nemoSDK = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        Tracking.getInstance(this).start("");

        nemoSDK = new NemoSDK();

        nemoSDK.sdkInitialize(this, new IGameOauthListener() {

            @Override
            public void onLoginSuccess(String access_token, String id_token) {

                Log.d("access_token", access_token);
                Log.d("id_token", id_token);

                btnDangNhap.setVisibility(View.GONE);
                btnDangXuat.setVisibility(View.VISIBLE);
                btnShowInfoUser.setVisibility(View.VISIBLE);
                btnShowIAP.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoginFail(String msg, String code) {
                Log.d("LoginFail", msg+"--"+code);
            }


            @Override
            public void onLogoutSuccess() {

                btnDangNhap.setVisibility(View.VISIBLE);
                btnDangXuat.setVisibility(View.GONE);
                btnAccessToken.setVisibility(View.GONE);
                btnShowInfoUser.setVisibility(View.GONE);
                btnShowIAP.setVisibility(View.GONE);
            }

            @Override
            public void onLogoutFail() {

            }
        });

        initView();

    }

    public void initView() {
        btnShowIAP = (Button) findViewById(R.id.btnShowIAP);
        btnShowIAP.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View var1) {
                IAPGameData iapGameData = new IAPGameData();
                iapGameData.roleId = "ABC XYA";
                iapGameData.serverId = "S1";
                iapGameData.amount = "20000";
                iapGameData.productID = "vn.gosu.atomarena.100kc";
                iapGameData.productName = "vn.gosu.atomarena.100kc";
                iapGameData.extraInfo = "vn.gosu.atomarena.100kc";
                iapGameData.orderID = "";
                nemoSDK.showIAP(iapGameData, new IAPDelegate() {
                    @Override
                    public void onSuccess(String code, String message) {
                        Log.d("IAP", code + "::" + message);
                    }

                    @Override
                    public void onFail(String action, String code, String message) {
                        Log.d("IAP", action + "::" + code + "::" + message);
                    }
                });
            }
        });

        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangNhap.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View var1) {
                nemoSDK.login();
            }
        });


        btnDangXuat = (Button) findViewById(R.id.btnDangXuat);
        btnDangXuat.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View var1) {
                nemoSDK.logout();
            }
        });

        btnDangNhap.setVisibility(View.VISIBLE);

        btnDangXuat.setVisibility(View.GONE);

        btnAccessToken = (Button)findViewById(R.id.btnAccessToken);
        btnAccessToken.setVisibility(View.GONE);


        btnShowInfoUser = (Button) findViewById(R.id.btnShowInfoUser);
        btnShowInfoUser.setVisibility(View.GONE);
        btnShowInfoUser.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View var1) {
                String info = nemoSDK.getUserInfo();

                Toast.makeText(MainActivity.this, info  ,Toast.LENGTH_LONG).show();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", info);
                clipboard.setPrimaryClip(clip);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(nemoSDK != null){
            nemoSDK.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(nemoSDK != null){
            nemoSDK.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(nemoSDK != null){
            nemoSDK.onStop();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(nemoSDK != null){
            nemoSDK.onDestroy();
        }
    }
}