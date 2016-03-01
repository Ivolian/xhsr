package unicorn.com.xhsr.detailorder;

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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.florent37.viewanimator.ViewAnimator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
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
import unicorn.com.xhsr.utils.ConfigUtils;
import unicorn.com.xhsr.utils.ResultCodeUtils;
import unicorn.com.xhsr.utils.TextDrawableUtils;
import unicorn.com.xhsr.utils.ToastUtils;
import unicorn.com.xhsr.volley.JsonArrayRequestWithSessionCheck;
import unicorn.com.xhsr.volley.SimpleVolley;


public class DetailOrderActivity extends BottomSheetActivity {


    // =============================== onCreate ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        initViews();

//        findViewById(R.id.message).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_light_blue_300));
        findViewById(R.id.photo).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_orange_300));
        findViewById(R.id.video).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_red_300));
//        findViewById(R.id.history).setBackground(TextDrawableUtils.getRoundRectDrawable(this, R.color.md_brown_300));
    }

    private void initViews() {
        initIvEquipment();
        initProcessMode();
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
        Intent intent = new Intent(this, EquipmentSelectActivity.class);
        intent.putExtra("subId", equipmentId);
        startActivityForResult(intent, 2333);
    }


    // =============================== 设备图标 ===============================

    @Bind(R.id.ivEquipment)
    ImageView ivEquipment;

    private void initIvEquipment() {
        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        TextDrawable textDrawable = TextDrawable.builder().buildRound("修", colorPrimary);
        ivEquipment.setImageDrawable(textDrawable);
    }


    // =============================== 故障类型 ===============================

    String faultTypeId;

    List<SelectObject> faultTypeDataList;

    // TODO
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
            ToastUtils.show("请先选择待维修设备");
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

    private void notifyEquipmentChange() {
        faultTypeId = null;
        tvFaultType.setText("");
        fetchFaultTypeDataList();
    }

    private void fetchFaultTypeDataList() {
        SimpleVolley.addRequest(new JsonArrayRequestWithSessionCheck(
                ConfigUtils.getBaseUrl() + "/api/v1/hems/equipment/" + equipmentId + "/faultType",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responses) {
                        try {
                            List<SelectObject> dataList = new ArrayList<>();
                            for (int i = 0; i != responses.length(); i++) {
                                JSONObject jsonObject = responses.getJSONObject(i);
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show("该设备故障类型尚未录入");
                    }
                })
        );
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

    @Bind(R.id.tvPersonName)
    TextView tvPersonName;

    @OnClick(R.id.repairPerson)
    public void repairPersonOnClick() {
        Intent intent = new Intent(this, RepairPersonActivity.class);
        intent.putExtra("personName", personName);
        intent.putExtra("personCode", personCode);
        intent.putExtra("departmentId", departmentId);
        startActivityForResult(intent, 2333);
    }


    // =============================== 处理方式 ===============================

    String processModeId;

    String processTimeLimitId;

    String emergencyDegreeId;

    @Bind(R.id.tvProcessMode)
    TextView tvProcessMode;

    @OnClick(R.id.processMode)
    public void processModeOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        Intent intent = new Intent(this, ProcessModeActivity.class);
        intent.putExtra("processModeId", processModeId);
        intent.putExtra("processTimeLimitId", processTimeLimitId);
        intent.putExtra("emergencyDegreeId", emergencyDegreeId);
        startActivityForResult(intent, 2333);
    }

    private void notifyProcessModeChange() {
        String processModeText = DataHelp.getValue(DataHelp.getProcessModeDataProvider(), processModeId);
        String processTimeLimitText = DataHelp.getValue(DataHelp.getProcessTimeLimitDataProvider(), processTimeLimitId);
        String emergencyDegreeText = DataHelp.getValue(DataHelp.getEmergencyDegreeDataProvider(), emergencyDegreeId);
        tvProcessMode.setText(processModeText + " / " + processTimeLimitText + " / " + emergencyDegreeText);
    }

    private void initProcessMode() {
        processModeId = DataHelp.getProcessModeDataProvider().getDataList().get(0).objectId;
        processTimeLimitId = DataHelp.getProcessTimeLimitDataProvider().getDataList().get(0).objectId;
        emergencyDegreeId = DataHelp.getEmergencyDegreeDataProvider().getDataList().get(0).objectId;
        notifyProcessModeChange();
    }


    // =============================== 补充说明 ===============================

    @Bind(R.id.etDescription)
    EditText etDescription;

    @Bind(R.id.arrow)
    ImageView arrow;

    @Bind(R.id.erlDescription)
    ExpandableRelativeLayout erlDescription;

    @OnClick(R.id.description)
    public void descriptionOnClick() {
        ViewAnimator
                .animate(arrow)
                .rotation(erlDescription.isExpanded() ? 0 : 90)
                .duration(300)
                .start();
        erlDescription.toggle();
    }


    // =============================== onActivityResult ===============================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ResultCodeUtils.EQUIPMENT) {
            equipmentId = data.getStringExtra("subId");
            Equipment equipment = SimpleApplication.getDaoSession().getEquipmentDao().queryBuilder().where(EquipmentDao.Properties.ObjectId.eq(equipmentId)).unique();
            tvEquipment.setText(equipment.getFullName());
            notifyEquipmentChange();
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
            tvPersonName.setText(personName);
        }

        if (resultCode == ResultCodeUtils.PROCESS_MODE) {
            processModeId = data.getStringExtra("processModeId");
            processTimeLimitId = data.getStringExtra("processTimeLimitId");
            emergencyDegreeId = data.getStringExtra("emergencyDegreeId");
            notifyProcessModeChange();
        }
    }





    // =============================== 基础方法 ===============================

    @OnClick(R.id.cancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.confirm)
    public void confirm() {
        if (ClickHelp.isFastClick()){
            return;
        }
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
                map.put("Cookie", "JSESSIONID=" + ConfigUtils.getJsessionId());
                // 不加这个会出现 Unsupported media type 415 错误
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject result = new JSONObject();
                    // todo
                    result.put("address", "");
                    result.put("callNumber", "");
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
            ToastUtils.show("请选择待维修的设备");
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
            ToastUtils.show("请填写报修人员");
            return false;
        }
        return true;
    }

    private void addJsonObjectToResult(JSONObject result, String field, String objectId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("objectId", objectId);
        result.put(field, jsonObject);
    }


}
