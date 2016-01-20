package unicorn.com.xhsr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.florent37.viewanimator.ViewAnimator;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;
import unicorn.com.xhsr.groupselect.GroupSelectHelper;
import unicorn.com.xhsr.select.SelectObject;
import unicorn.com.xhsr.speech.JsonParser;
import unicorn.com.xhsr.utils.TextDrawableUtils;
import unicorn.com.xhsr.utils.ToastUtils;


public class QuickOrderActivity extends BaseActivity {


    // =============================== onCreate & onDestroy ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_quick_order);
        initViews();

        findViewById(R.id.message).setBackground(TextDrawableUtils.getRoundRectDrawable(this,R.color.md_light_blue_300));
        findViewById(R.id.takePhoto).setBackground(TextDrawableUtils.getRoundRectDrawable(this,R.color.md_orange_300));
        findViewById(R.id.video).setBackground(TextDrawableUtils.getRoundRectDrawable(this,R.color.md_red_300));
        findViewById(R.id.history).setBackground(TextDrawableUtils.getRoundRectDrawable(this,R.color.md_brown_300));

    }



    @Override
    public void onDestroy() {

        super.onDestroy();
        mIat.cancel();
        mIat.destroy();
    }

    private void initViews() {
        initEquipment();

        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

    }

    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");


        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");


        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");


        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
        // 注：该参数暂时只对在线听写有效
        mIat.setParameter(SpeechConstant.ASR_DWA, "0");
    }

    @OnClick(R.id.repairPerson)
    public void repairPersonOnClick() {
        Intent intent = new Intent(this, RepairPersonActivity.class);
        startActivity(intent);
    }


    // =============================== 设备 ===============================

    public int EQUIPMENT_RESULT_CODE = 1001;

    @Bind(R.id.tdEquipment)
    ImageView tdEquipment;

    SelectObject selectObjectEquipment;

    private void initEquipment() {
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        TextDrawable textDrawable = TextDrawable.builder().buildRound("修", colorPrimary);
        tdEquipment.setImageDrawable(textDrawable);
    }

    @OnClick(R.id.equipment)
    public void equipmentOnClick() {
        GroupSelectActivity.dataProvider = DataHelp.getEquipmentDataProvider();
        GroupSelectHelper.startGroupSelectActivity(this, "设备", 1, EQUIPMENT_RESULT_CODE);
    }

    @Bind(R.id.tvEquipment)
    TextView tvEquipment;


    public int ADDRESS_RESULT_CODE = 1002;


    // =============================== onActivityResult ===============================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EQUIPMENT_RESULT_CODE) {
            SelectObject selectObject = (SelectObject) data.getSerializableExtra(GroupSelectHelper.RESULT);
            selectObjectEquipment = selectObject;
            tvEquipment.setText(selectObject.value);
        }
        if (resultCode == ADDRESS_RESULT_CODE) {
            SelectObject selectObject = (SelectObject) data.getSerializableExtra(GroupSelectHelper.RESULT);
            selectObjectAddress = selectObject;
            tvFloor.setText(selectObject.value);
        }
    }


    SelectObject selectObjectAddress;

    @Bind(R.id.tvFloor)
    TextView tvFloor;

    @OnClick(R.id.floor)
    public void floorOnClick() {
        GroupSelectActivity.dataProvider = DataHelp.getFloorDataProvider();
        GroupSelectHelper.startGroupSelectActivity(this, "维修地址", 1, ADDRESS_RESULT_CODE);
    }

    // =============================== bottom sheet ===============================



    // =============================== 设备故障 ===============================

//    @Bind(R.id.tvBreakdown)
//    TextView tvBreakdown;
//
//    SelectObject soBreakdown;
//
//    @OnClick(R.id.breakdown)
//    public void selectBreakdown() {
//        showSelectSheet("选择设备故障", "onBreakdownSelect", soBreakdown == null ? -1 : soBreakdown.position);
//    }
//
//    @Subscriber(tag = "onBreakdownSelect")
//    private void onBreakdownSelect(SelectObject selectObjectWithPosition) {
//        soBreakdown = selectObjectWithPosition;
//        bottomSheet.dismissSheet();
//        String breakdown = (String) selectObjectWithPosition.value;
//        tvBreakdown.setText(breakdown);
//    }


    // =============================== 处理方式 ===============================




    @OnClick(R.id.processMode)
    public void selectHandleMode() {
        Intent intent = new Intent(this,ProcessModeActivity.class);
        startActivity(intent);
    }




    // =============================== 补充说明 ===============================

    @Bind(R.id.etDescription)
    EditText etDescription;

    private SpeechRecognizer mIat;

    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                ToastUtils.show("初始化失败，错误码：" + code);
            }
        }
    };


    @Bind(R.id.descriptionArrow)
    ImageView ivDescriptionArrow;

    @Bind(R.id.elDescription)
    ExpandableRelativeLayout elDescription;
    int ret = 0;

    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            ToastUtils.show("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            ToastUtils.show(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            ToastUtils.show("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {


            printResult(results);


        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            ToastUtils.show("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        etDescription.setText(resultBuffer.toString());
    }

    @OnClick(R.id.description)
    public void descriptionOnClick() {
        ViewAnimator
                .animate(ivDescriptionArrow)
                .rotation(elDescription.isExpanded() ? 0 : 90)
                .duration(300)
                .start();
        elDescription.toggle();

//        setParam();
//        ret = mIat.startListening(mRecognizerListener);
//        if (ret != ErrorCode.SUCCESS) {
//            ToastUtils.show("听写失败,错误码：" + ret);
//        } else {
//            ToastUtils.show("请开始说话");
//        }
    }




    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        finish();
    }



    @OnClick(R.id.confirm)
    public void confirm() {
        DataHelp.wait_repair = true;
        finish();
    }


}
