package unicorn.com.xhsr.quickorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.florent37.viewanimator.ViewAnimator;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.ProcessModeActivity;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.base.BottomSheetActivity;
import unicorn.com.xhsr.data.DataHelp;
import unicorn.com.xhsr.data.greendao.Building;
import unicorn.com.xhsr.data.greendao.BuildingDao;
import unicorn.com.xhsr.data.greendao.Equipment;
import unicorn.com.xhsr.data.greendao.EquipmentDao;
import unicorn.com.xhsr.groupselect.GroupSelectHelper;
import unicorn.com.xhsr.groupselect.equipment.EquipmentSelectActivity;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.select.SelectAdapter;
import unicorn.com.xhsr.select.SelectObject;
import unicorn.com.xhsr.speech.JsonParser;
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.ResultCodeUtils;
import unicorn.com.xhsr.utils.TextDrawableUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.volley.SimpleVolley;
import unicorn.com.xhsr.volley.StringRequestWithSessionCheck;


public class QuickOrderActivity extends BottomSheetActivity {


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_order);
        initViews();

        findViewById(R.id.message).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_light_blue_300));
        findViewById(R.id.takePhoto).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_orange_300));
        findViewById(R.id.video).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_red_300));
        findViewById(R.id.history).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_brown_300));
    }

    private void initViews() {
        initEquipment();

        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        processModeId = DataHelp.getProcessModeDataProvider().getDataList().get(0).objectId;
        processTimeLimitId = DataHelp.getProcessTimeLimitDataProvider().getDataList().get(0).objectId;
        emergencyDegreeId = DataHelp.getEmergencyDegreeDataProvider().getDataList().get(0).objectId;

        setProcessModeText();

    }


    // =============================== 设备 ===============================

    String equipmentId;

    @Bind(R.id.tvEquipment)
    TextView tvEquipment;

    @OnClick(R.id.equipment)
    public void equipmentOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }

        EquipmentSelectActivity.setDataProvider(DataHelp.getEquipmentDataProvider());
        Intent intent = new Intent(this, EquipmentSelectActivity.class);
        intent.putExtra("name", "设备");
        intent.putExtra("subId", equipmentId);
        intent.putExtra("resultCode", ResultCodeUtils.EQUIPMENT);
        this.startActivityForResult(intent, 2333);
//        GroupSelectHelper.startGroupSelectActivity(this, DataHelp.getEquipmentDataProvider(), "设备", equipmentId, ResultCodeUtils.EQUIPMENT);
    }


    // =============================== 设备图标 ===============================

    @Bind(R.id.ivEquipment)
    ImageView ivEquipment;

    private void initEquipment() {
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        TextDrawable textDrawable = TextDrawable.builder().buildRound("修", colorPrimary);
        ivEquipment.setImageDrawable(textDrawable);
    }


    // =============================== 故障类型 ===============================

    String faultTypeId;

    List<SelectObject> faultTypeDataList;

    SelectAdapter.DataProvider dpFaultType = new SelectAdapter.DataProvider() {
        @Override
        public List<SelectObject> getDataList() {
            return faultTypeDataList;
        }
    };

    @Bind(R.id.tvFaultType)
    TextView tvFaultType;

    @OnClick(R.id.faultType)
    public void faultTypeOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        if (equipmentId == null) {
            ToastUtils.show("请先选择维修设备");
            return;
        }
        showSelectSheet("故障类型", dpFaultType, faultTypeId, "onFaultTypeSelect");
    }

    @Subscriber(tag = "onFaultTypeSelect")
    private void onFaultTypeSelect(String objectId) {
        faultTypeId = objectId;
        tvFaultType.setText(DataHelp.getValue(dpFaultType, objectId));
        bottomSheet.dismissSheet();
    }

    private void fetchFaultType() {
        faultTypeId = null;
        tvFaultType.setText("");
        fetchFaultTypeDataList();
    }

    private void fetchFaultTypeDataList() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/equipment/" + equipmentId + "/faultType";
        StringRequest jsonArrayRequest = new StringRequestWithSessionCheck(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        try {
                            List<SelectObject> dataList = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(responses);
                            for (int i = 0; i != jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String objectId = jsonObject.getString("objectId");
                                String name = jsonObject.getString("name");
                                SelectObject selectObject = new SelectObject();
                                selectObject.objectId = objectId;
                                selectObject.value = name;
                                dataList.add(selectObject);
                            }
                            faultTypeDataList = dataList;
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }


    // =============================== 维修地址 ===============================

    String buildingId;

    @Bind(R.id.tvBuilding)
    TextView tvBuilding;

    @OnClick(R.id.building)
    public void buildingOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        GroupSelectHelper.startGroupSelectActivity(this, DataHelp.getBuildingDataProvider(), "维修地址", buildingId, ResultCodeUtils.BUILDING);
    }


    // =============================== 报修人员 ===============================

    String personName;

    String personCode;

    String departmentId;

    @Bind(R.id.tvRepairPerson)
    TextView tvRepairPerson;

    @OnClick(R.id.repairPerson)
    public void repairPersonOnClick() {
        Intent intent = new Intent(this, RepairPersonActivity.class);
        intent.putExtra("personName", personName);
        intent.putExtra("personCode", personCode);
        intent.putExtra("departmentId", departmentId);
        startActivityForResult(intent, 2333);
    }


    // =============================== 处理方式 ===============================

    public static int PROCESS_MODE_RESULT_CODE = 1004;

    String processModeId;

    String processTimeLimitId;

    String emergencyDegreeId;

    @OnClick(R.id.processMode)
    public void processModeOnClick() {
        if (!ClickHelp.isFastClick()) {
            Intent intent = new Intent(this, ProcessModeActivity.class);
            intent.putExtra("processModeId", processModeId);
            intent.putExtra("processTimeLimitId", processTimeLimitId);
            intent.putExtra("emergencyDegreeId", emergencyDegreeId);
            startActivityForResult(intent, 2333);
        }
    }

    @Bind(R.id.tvProcessMode)
    TextView tvProcessMode;

    private void setProcessModeText() {
        String text1 = DataHelp.getValue(DataHelp.getProcessModeDataProvider(), processModeId);
        String text2 = DataHelp.getValue(DataHelp.getProcessTimeLimitDataProvider(), processTimeLimitId);
        String text3 = DataHelp.getValue(DataHelp.getEmergencyDegreeDataProvider(), emergencyDegreeId);
        String text = text1 + " / " + text2 + " / " + text3;
        tvProcessMode.setText(text);
    }


    // =============================== 补充说明 ===============================

    @Bind(R.id.etDescription)
    EditText etDescription;

    @Bind(R.id.descriptionArrow)
    ImageView descriptionArrow;

    @Bind(R.id.elDescription)
    ExpandableRelativeLayout erlDescription;

    @OnClick(R.id.description)
    public void descriptionOnClick() {
        ViewAnimator
                .animate(descriptionArrow)
                .rotation(erlDescription.isExpanded() ? 0 : 90)
                .duration(300)
                .start();
        erlDescription.toggle();

//        setParam();
//        ret = mIat.startListening(mRecognizerListener);
//        if (ret != ErrorCode.SUCCESS) {
//            ToastUtils.show("听写失败,错误码：" + ret);
//        } else {
//            ToastUtils.show("请开始说话");
//        }
    }


    // =============================== onActivityResult ===============================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ResultCodeUtils.EQUIPMENT) {
            equipmentId = data.getStringExtra("subId");
            Equipment equipment = SimpleApplication.getDaoSession().getEquipmentDao().queryBuilder().where(EquipmentDao.Properties.ObjectId.eq(equipmentId)).unique();
            tvEquipment.setText(equipment.getFullName());
            fetchFaultType();
        }

        if (resultCode == ResultCodeUtils.BUILDING) {
            buildingId = data.getStringExtra("subId");
            Building building = SimpleApplication.getDaoSession().getBuildingDao().queryBuilder().where(BuildingDao.Properties.ObjectId.eq(buildingId)).unique();
            tvBuilding.setText(building.getFullName());
        }

        if (resultCode == ResultCodeUtils.REPAIR_PERSON) {
            personName = data.getStringExtra("personName");
            personCode = data.getStringExtra("personCode");
            departmentId = data.getStringExtra("departmentId");
            tvRepairPerson.setText(personName);
        }

        if (resultCode == PROCESS_MODE_RESULT_CODE) {
            processModeId = data.getStringExtra("processModeId");
            processTimeLimitId = data.getStringExtra("processTimeLimitId");
            emergencyDegreeId = data.getStringExtra("emergencyDegreeId");
            setProcessModeText();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mIat.cancel();
        mIat.destroy();
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


    private SpeechRecognizer mIat;

    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                ToastUtils.show("初始化失败，错误码：" + code);
            }
        }
    };


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


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.confirm)
    public void confirm() {

        if (!checkInput()) {
            return;
        }

        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/issue";
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
                map.put("Cookie", "JSESSIONID=" + ConfigUtils.getSessionId());
                // 不加这个会出现 Unsupported media type 415 错误
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject result = new JSONObject();
                    result.put("address", "");
                    result.put("callNumber", 123);
                    addJsonObjectToResult(result, "building", buildingId);
                    result.put("description", etDescription.getText().toString());
                    addJsonObjectToResult(result, "emergencyDegree", emergencyDegreeId);
                    addJsonObjectToResult(result, "equipment", equipmentId);
                    addJsonObjectToResult(result, "faultType", faultTypeId);
                    addJsonObjectToResult(result, "processingMode", processModeId);
                    addJsonObjectToResult(result, "processingTimeLimit", processTimeLimitId);
                    addJsonObjectToResult(result, "requestDepartment", departmentId);
                    result.put("requestTime", new Date().getTime());
                    result.put("requestUser", personName);
                    result.put("requestUserNo", personCode);
                    addJsonObjectToResult(result, "type", "97c6be7f-449a-4371-b310-1e40b42e544f");

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

    private boolean checkInput() {
        if (equipmentId == null) {
            ToastUtils.show("请选择需要维修的设备");
            return false;
        }
        if (faultTypeId == null) {
            ToastUtils.show("请选择故障类型");
            return false;
        }
        if (buildingId == null) {
            ToastUtils.show("请选择维修地址");
            return false;
        }
        if (departmentId == null) {
            ToastUtils.show("请填写报修人员信息");
            return false;
        }

        return true;
    }

    private void addJsonObjectToResult(JSONObject result, String tag, String objectId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("objectId", objectId);
        result.put(tag, jsonObject);
    }


}
