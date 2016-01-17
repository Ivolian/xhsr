package unicorn.com.xhsr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.ppamorim.dragger.DraggerActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.greendao.Equipment;
import unicorn.com.xhsr.greendao.EquipmentCategory;
import unicorn.com.xhsr.greendao.EquipmentCategoryDao;
import unicorn.com.xhsr.greendao.EquipmentDao;
import unicorn.com.xhsr.greendao.ProcessingMode;
import unicorn.com.xhsr.groupselect.GroupSelectActivity;
import unicorn.com.xhsr.groupselect.GroupSelectHelper;
import unicorn.com.xhsr.select.SelectAdapter;
import unicorn.com.xhsr.select.SelectObject;
import unicorn.com.xhsr.select.SelectObjectWithPosition;
import unicorn.com.xhsr.speech.JsonParser;
import unicorn.com.xhsr.utils.ToastUtils;


public class QuickOrderActivity extends DraggerActivity {


    // =============================== onCreate & onDestroy ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_quick_order);
        ActivityHelp.initActivity(this);
        initViews();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        mIat.cancel();
        mIat.destroy();
    }

    private void initViews() {
        initEquipment();
        initBottomSheet();
        setProcessModeDefaultValue();
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


    // =============================== 选择需要维修的设备 ===============================

    @Bind(R.id.tdEquipment)
    ImageView tdEquipment;

    private void initEquipment() {
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        TextDrawable textDrawable = TextDrawable.builder().buildRound("修", colorPrimary);
        tdEquipment.setImageDrawable(textDrawable);
    }

    @OnClick(R.id.equipment)
    public void equipmentOnClick() {

        GroupSelectActivity.dataProvider = new GroupSelectActivity.DataProvider() {
            @Override
            public List<SelectObject> getMainDataList() {

                // todo 目前可以无视 level 参数

                EquipmentCategoryDao equipmentCategoryDao = SimpleApplication.getDaoSession().getEquipmentCategoryDao();
                final List<EquipmentCategory> equipmentCategoryList = equipmentCategoryDao.queryBuilder()
                        .orderAsc(EquipmentCategoryDao.Properties.OrderNo)
                        .list();
                List<SelectObject> dataList = new ArrayList<>();
                for (EquipmentCategory equipmentCategory : equipmentCategoryList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = equipmentCategory.getObjectId();
                    selectObject.value = equipmentCategory.getName();
                    dataList.add(selectObject);
                }
                return dataList;
            }

            @Override
            public List<SelectObject> getSubDataList(SelectObject so) {
                EquipmentDao equipmentDao = SimpleApplication.getDaoSession().getEquipmentDao();
                List<Equipment> equipmentList = equipmentDao.queryBuilder()
                        .where(EquipmentDao.Properties.CategoryId.eq(so.objectId))
                        .list();
                final List<SelectObject> dataList = new ArrayList<>();
                for (Equipment equipment : equipmentList) {
                    SelectObject selectObject = new SelectObject();
                    selectObject.objectId = equipment.getObjectId();
                    selectObject.value = equipment.getName();
                    dataList.add(selectObject);
                }
                return dataList;
            }

            @Override
            public List<SelectObject> getSearchResultDataList(String query) {

                List<SelectObject> dataList = new ArrayList<>();

                EquipmentCategoryDao equipmentCategoryDao = SimpleApplication.getDaoSession().getEquipmentCategoryDao();
                List<EquipmentCategory> equipmentCategoryList = equipmentCategoryDao.queryBuilder()
                        .where(EquipmentCategoryDao.Properties.Name.like("%" + query + "%"))
                        .orderAsc(EquipmentCategoryDao.Properties.OrderNo)
                        .list();
                for (EquipmentCategory equipmentCategory : equipmentCategoryList) {
                    for (Equipment equipment : equipmentCategory.getEquipmentList()) {
                        SelectObject selectObject = new SelectObject();
                        selectObject.objectId = equipment.getObjectId();
                        selectObject.value = equipmentCategory.getName() + " / " + equipment.getName();
                        dataList.add(selectObject);
                    }
                }

                // todo

                return dataList;
            }
        };
        GroupSelectHelper.startGroupSelectActivity(this, "设备", 1, EQUIPMENT_RESULT_CODE);
    }

    @Bind(R.id.tvEquipment)
    TextView tvEquipment;

    public int EQUIPMENT_RESULT_CODE = 1001;

    public int ADDRESS_RESULT_CODE = 1002;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EQUIPMENT_RESULT_CODE) {
            String result = data.getStringExtra(GroupSelectHelper.RESULT);
            tvEquipment.setText(result);
        }
        if (resultCode == ADDRESS_RESULT_CODE) {
            String result = data.getStringExtra(GroupSelectHelper.RESULT);
            tvAddress.setText(result);
        }
    }

    @Bind(R.id.tvAddress)
    TextView tvAddress;

    @OnClick(R.id.address)
    public void addressOnClick() {
        GroupSelectHelper.startGroupSelectActivity(this, "维修地址", 1, ADDRESS_RESULT_CODE);
    }

    // =============================== bottom sheet ===============================

    @Bind(R.id.bottomsheet)
    BottomSheetLayout bottomSheet;

    @SuppressWarnings("deprecation")
    private void initBottomSheet() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int height = windowManager.getDefaultDisplay().getHeight();
        bottomSheet.setPeekSheetTranslation(height * 0.65f);
    }


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
//    private void onBreakdownSelect(SelectObject selectObject) {
//        soBreakdown = selectObject;
//        bottomSheet.dismissSheet();
//        String breakdown = (String) selectObject.value;
//        tvBreakdown.setText(breakdown);
//    }


    // =============================== 处理方式 ===============================

    @Bind(R.id.tvProcessMode)
    TextView tvProcessMode;

    SelectObjectWithPosition sopProcessMode;

    @OnClick(R.id.processMode)
    public void selectHandleMode() {
        showSelectSheet("选择处理方式", "onProcessModeSelect", sopProcessMode == null ? -1 : sopProcessMode.position);
    }

    @Subscriber(tag = "onProcessModeSelect")
    private void onHandleModeSelect(SelectObjectWithPosition selectObjectProcessMode) {
        sopProcessMode = selectObjectProcessMode;
        bottomSheet.dismissSheet();
        tvProcessMode.setText(selectObjectProcessMode.value);
    }

    private void setProcessModeDefaultValue() {
        List<ProcessingMode> processingModeList = DataHelp.getProcessModeList();
        ProcessingMode processingMode = processingModeList.get(0);
        tvProcessMode.setText(processingMode.getName());
        sopProcessMode = new SelectObjectWithPosition();
        sopProcessMode.value = processingMode.getName();
        sopProcessMode.objectId = processingMode.getObjectId();
        sopProcessMode.position = 0;
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

        setParam();
        ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            ToastUtils.show("听写失败,错误码：" + ret);
        } else {
            ToastUtils.show("请开始说话");
        }
    }


    // =============================== showSelectSheet ===============================

    private void showSelectSheet(String sheetTitle, String callbackTag, int positionSelected) {
        View rootView = LayoutInflater.from(this).inflate(R.layout.select_sheet, bottomSheet, false);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.title);
        tvTitle.setText(sheetTitle);
        rootView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SelectAdapter(callbackTag, positionSelected));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        recyclerView.scrollToPosition(positionSelected);
        bottomSheet.showWithSheetView(rootView);
    }


    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        closeActivity();
    }


}
