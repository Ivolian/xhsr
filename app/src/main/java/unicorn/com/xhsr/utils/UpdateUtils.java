package unicorn.com.xhsr.utils;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;
import unicorn.com.xhsr.volley.JSONObjectRequestWithSessionCheck;
import unicorn.com.xhsr.volley.SimpleVolley;


public class UpdateUtils {

    private static Activity currentActivity;

    private static void init(Activity activity) {
        currentActivity = activity;
    }

    private static void clear() {
        currentActivity = null;
    }

    //

    public static void checkUpdate(Activity activity) {
        init(activity);

        String url = ConfigUtils.getBaseUrl() + "/api/v1/system/apk/WorkOrderAppPublic/update";
        JsonObjectRequest jsonObjectRequest = new JSONObjectRequestWithSessionCheck(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final String versionName = response.getString("version");
                            final String code = response.getString("code");
                            if (!versionName.equals(AppUtils.getVersionName())) {
                                DialogUtils.showConfirm(currentActivity, "检测到新版本，是否立即更新？", new DialogUtils.Action() {
                                    @Override
                                    public void doAction() {
                                        String apkUrl = ConfigUtils.getBaseUrl() + "/apk/download/" + code;
                                        downloadApk(apkUrl);
                                    }
                                }, new DialogUtils.Action() {
                                    @Override
                                    public void doAction() {

                                    }
                                });
                            }
                        } catch (Exception e) {
                            //
                        }
                    }

                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonObjectRequest);
    }

    private static void downloadApk(String apkUrl) {
        final MaterialDialog mask = DialogUtils.showMask2(currentActivity, "下载安装包中", "请稍后");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(apkUrl, new FileAsyncHttpResponseHandler(currentActivity) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                mask.dismiss();
                installApk(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                mask.dismiss();
                ToastUtils.show("下载失败");
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                mask.setProgress((int) (bytesWritten * 100 / totalSize));
            }
        });
    }

    private static void installApk(File response) {
        String apkPath = ConfigUtils.getBaseDirPath() + "/xhsr.apk";
        File apk = new File(apkPath);
        if (apk.exists()) {
            apk.delete();
        }
        try {
            FileUtils.copyFile(response, apk);
        } catch (Exception e) {
            //
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        currentActivity.startActivity(intent);

        clear();
    }

}
