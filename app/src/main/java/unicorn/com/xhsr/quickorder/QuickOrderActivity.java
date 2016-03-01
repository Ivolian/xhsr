package unicorn.com.xhsr.quickorder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialcamera.MaterialCamera;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.viewanimator.ViewAnimator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.DialogUtils;
import unicorn.com.xhsr.utils.ImageUtils;
import unicorn.com.xhsr.utils.TextDrawableUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.utils.UploadUtils;
import unicorn.com.xhsr.volley.SimpleVolley;


public class QuickOrderActivity extends BaseActivity {


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qucik_order);
        initViews();
    }

    private void initViews() {
        ViewAnimator
                .animate(arrow)
                .rotation(90)
                .duration(0)
                .start();
        findViewById(R.id.photo).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_orange_300));
        findViewById(R.id.video).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_red_300));
//        findViewById(R.id.message).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_light_blue_300));
//        findViewById(R.id.history).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_brown_300));

    }

    JSONArray attachmentList = new JSONArray();

    // =============================== 下单说明 ===============================

    @Bind(R.id.etDescription)
    EditText etDescription;

    @Bind(R.id.arrow)
    ImageView arrow;


    // =============================== photo ===============================

    String photoPath;

    @OnClick(R.id.photo)
    public void photoOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        takePhoto();
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri randomUri = ImageUtils.getRandomPhotoUri();
        photoPath = randomUri.getPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, randomUri);
        startActivityForResult(intent, 2333);
    }


    @Subscriber(tag = "quickOrderActivity_onPhotoUploadFinish")
    private void onUploadFinish(String tempFileName) throws Exception {
        JSONObject attachmentPhoto = new JSONObject();
        attachmentPhoto.put("type", "Picture");
        attachmentPhoto.put("filename", tempFileName);
        attachmentList.put(attachmentPhoto);
        ToastUtils.show("照片上传成功!");
    }


    // =============================== onActivityResult ===============================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2333 && resultCode == RESULT_OK) {
            String compressPhotoPath = ImageUtils.compressPhoto(photoPath);
            UploadUtils.upload(new File(compressPhotoPath), "quickOrderActivity_onPhotoUploadFinish", DialogUtils.showMask2(this, "上传照片中", "请稍后"));
        }
        if (requestCode == 2334 && resultCode == RESULT_OK) {
            try {
                URL videoUrl = new URL(data.getDataString());
                UploadUtils.upload(new File(videoUrl.toURI()), "quickOrderActivity_onVideoUploadFinish", DialogUtils.showMask2(this, "上传视频中", "请稍后"));
            } catch (Exception e) {
                //
            }
        }
    }


    //

    @OnClick(R.id.video)
    public void videoOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        startCamera();
    }

    private void startCamera() {
        new MaterialCamera(this)
                .saveDir(ConfigUtils.getBaseDirPath())
                .showPortraitWarning(false)
                .allowRetry(true)
                .defaultToFrontFacing(false)
                .lengthLimitSeconds(20)
                .start(2334);
    }


    @Subscriber(tag = "quickOrderActivity_onVideoUploadFinish")
    private void onVideoUploadFinish(String tempFileName) throws Exception {
        JSONObject attachmentVideo = new JSONObject();
        attachmentVideo.put("type", "Video");
        attachmentVideo.put("filename", tempFileName);
        attachmentList.put(attachmentVideo);
        ToastUtils.show("视频上传成功");
    }


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.confirm)
    public void confirm() {
        if (ClickHelp.isFastClick()) {
            return;
        }

        // checkInput
        if (TextUtils.isEmpty(etDescription.getText())) {
            ToastUtils.show("下单说明不能为空");
            return;
        }

        DialogUtils.showConfirm(this, "确认下单？", new DialogUtils.Action() {
            @Override
            public void doAction() {
                commit();
            }
        }, new DialogUtils.Action() {
            @Override
            public void doAction() {

            }
        });
    }


    private void commit() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/quick";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show("下单成功");
                        finish();
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Cookie", "JSESSIONID=" + ConfigUtils.getJsessionId());
                // 不加这个会出现 Unsupported media type 415 错误
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject result = new JSONObject();
                    result.put("description", etDescription.getText().toString());
                    result.put("attachmentList", attachmentList);

                    String jsonString = result.toString();
                    return jsonString.getBytes("UTF-8");
                } catch (Exception e) {
                    //
                }
                return null;
            }
        };
        SimpleVolley.addRequest(stringRequest);
    }

}
