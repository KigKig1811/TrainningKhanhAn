package com.vnpay.anlmk.networks;

import android.util.Log;

import com.google.gson.Gson;

import com.vnpay.anlmk.ui.entites.LicenseEntity;
import com.vnpay.anlmk.utils.AESService;
import com.vnpay.anlmk.utils.UIModel;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UtilitilsAdaptor {
    private static UtilitilsAdaptor instance;
    private final int BANK_CODE = 1900;
    private final OkHttpClient client;
    private final int TIMEOUT_CONNECT = 30;
    private final int TIMEOUT_HANDLER = 90;

    private ResCallBack callBack;
    private final LicenseEntity license;

    public static UtilitilsAdaptor getInstance() {
        if (instance == null)
            instance = new UtilitilsAdaptor();
        return instance;
    }

    private UtilitilsAdaptor() {
        license = UIModel.getInstance().provideGson().fromJson(AESService.getInstance().decrypt("58af60e12b084e0d", UIModel.getInstance().getLicenseVexemphim()),
                LicenseEntity.class);
        this.client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(this.license.getUserName(), this.license.getPassword()))
                .retryOnConnectionFailure(true).build();
    }

    public void sendRequest(String uri, String data, ResCallBack cb) {
        try {
            this.callBack = cb;
            OkHttpClient newClient = client.newBuilder()
                    .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_HANDLER, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_HANDLER, TimeUnit.SECONDS)
                    .build();
            String keyAES = license.getAesKey();
            String keyMac = license.getMacKey();
            String keyId = license.getKeyId();
            byte[] iv = AESService.getInstance().generateIV();
            byte[] encrypt = AESService.getInstance().encrypt(keyAES, data, iv);

            byte[] source = new byte[iv.length + encrypt.length];
            System.arraycopy(iv, 0, source, 0, iv.length);
            System.arraycopy(encrypt, 0, source, iv.length, encrypt.length);

            String encryptData = android.util.Base64.encodeToString(source, android.util.Base64.NO_WRAP);
            String inputMac =  BANK_CODE + encryptData + keyId;
            String mac = android.util.Base64.encodeToString(
                    AESService.getInstance().createMac(inputMac.getBytes("UTF-8"),
                            keyMac.getBytes()), android.util.Base64.NO_WRAP);
            RequestBody body = new FormBody.Builder()
                    .add("m", mac)
                    .add("b", String.valueOf(BANK_CODE))
                    .add("e", encryptData)
                    .add("k", keyId)
                    .build();

            Request request = new Request.Builder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .addHeader("Connection","keep-alive")
                    .url(getUrl(uri))
                    .post(body)
                    .build();
            final Call c = newClient.newCall(request);
            c.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.wtf("tags", e);
                    try {
                        callBack.onResponse(null);
                    } catch (Exception e1) {
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String body = response.body().string();
                        Log.d("EXP D", body);
                        JSONObject js = new JSONObject(body);
                        byte[] allEncryptByte = android.util.Base64.decode(js.getString("e"), android.util.Base64.NO_WRAP);
                        byte[] iv = new byte[AESService.getInstance().IV_LENGTH];
                        System.arraycopy(allEncryptByte, 0, iv, 0, iv.length);
                        byte[] dataByte = new byte[allEncryptByte.length - AESService.getInstance().IV_LENGTH];
                        System.arraycopy(allEncryptByte, iv.length, dataByte, 0, dataByte.length);
                        String dataReponse = AESService.getInstance().decrypt(license.getAesKey(), dataByte, iv);
                        if (callBack != null)
                            callBack.onResponse(dataReponse);

                    } catch (Exception e) {
                        Log.w("E", e);
                        if (callBack != null) {
                            try {
                                callBack.onResponse(null);
                            } catch (Exception e1) {
                            }
                        }
                    } finally {
                    }
                }
            });
        } catch (Exception e) {
            Log.wtf("D", e);
            if (callBack != null) {
                try {
                    callBack.onResponse(null);
                } catch (Exception e1) {
                }
            }
        } finally {
        }
    }

    private String getUrl(String uri) {
        if (uri == null)
            return license.getUrlRoot();
        if (license.getUrlRoot().endsWith("/") || uri.startsWith("/"))
            return license.getUrlRoot() + uri;

        return license.getUrlRoot() + "/" + uri;
    }

    public interface ResCallBack {
        void onResponse(String data) throws Exception;
    }
    private Gson provideGson() {
        return new Gson();
    }
     class BasicAuthInterceptor implements Interceptor {
        private String credentials;

        public BasicAuthInterceptor(String user, String password) {
            this.credentials = Credentials.basic(user, password);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder().header("Authorization", credentials).build();
            return chain.proceed(authenticatedRequest);
        }

    }
}
