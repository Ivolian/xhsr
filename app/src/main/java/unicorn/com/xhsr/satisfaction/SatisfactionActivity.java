package unicorn.com.xhsr.satisfaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.data.greendao.SatisfactionOption;
import unicorn.com.xhsr.data.greendao.SatisfactionOptionDao;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.utils.ToastUtils;


public class SatisfactionActivity extends BaseActivity {


    // ============================ views ============================

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.numerator)
    TextView numerator;

    @Bind(R.id.denominator)
    TextView denominator;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // ============================ onCreate ============================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satisfaction);
        initData();
        initViews();
    }

    private void initViews() {
        initViewpager();
        notifyPositionChange(0);
    }

    private void notifyPositionChange(int position) {
        SatisfactionOption option = SimpleApplication.getDaoSession().getSatisfactionOptionDao().queryBuilder()
                .where(SatisfactionOptionDao.Properties.OrderNo.eq(position))
                .unique();
        title.setText(option.getTitle());
        numerator.setText(option.getNumerator() + "");
        denominator.setText("/" + option.getDenominator() + "");
    }


    // ============================ initViewpager ============================

    SatisfactionPagerAdapter satisfactionPagerAdapter;

    private void initViewpager() {
        satisfactionPagerAdapter = new SatisfactionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(satisfactionPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == satisfactionPagerAdapter.getCount() - 1) {
                    title.setText("8.其他建议与意见");
                    numerator.setVisibility(View.INVISIBLE);
                    denominator.setVisibility(View.INVISIBLE);
                    return;
                }
                numerator.setVisibility(View.VISIBLE);
                denominator.setVisibility(View.VISIBLE);
                notifyPositionChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
    }


    // ============================ 更多操作 ============================

    @OnClick(R.id.more)
    public void moreOnClick(View view) {
        if (ClickHelp.isFastClick()) {
            return;
        }
        showPopupMenu();
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.more));
        popupMenu.inflate(R.menu.satisfaction);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.category) {
                    showCategoryDialog();
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void showCategoryDialog() {
        String[] items = {"1.保洁服务", "2.工程维修服务", "3.绿植服务", "4.电梯服务", "8.其他建议与意见"};
        final NormalListDialog normalListDialog = new NormalListDialog(SatisfactionActivity.this, items);
        normalListDialog.title("问卷目录");
        normalListDialog.titleTextSize_SP(20);
        normalListDialog.itemTextSize(16);
        normalListDialog.titleBgColor(ContextCompat.getColor(SatisfactionActivity.this, R.color.colorPrimary));
        normalListDialog.cornerRadius(0);
        normalListDialog.layoutAnimation(null);
        normalListDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case 1:
                        viewPager.setCurrentItem(10, true);
                        break;
                    case 2:
                        viewPager.setCurrentItem(10 + 10, true);
                        break;
                    case 3:
                        viewPager.setCurrentItem(10 + 10 + 2, true);
                        break;
                    case 4:
                        viewPager.setCurrentItem(10 + 10 + 2 + 3, true);
                }
                normalListDialog.dismiss();
            }
        });
        normalListDialog.show();
    }


    // ============================ 提交问卷 ============================

    @Subscriber(tag = "submitOnClick")
    public void submitOnClick(Object object) {
        SatisfactionOption option = SimpleApplication.getDaoSession().getSatisfactionOptionDao().queryBuilder()
                .where(SatisfactionOptionDao.Properties.Score.eq(-1))
                .orderAsc(SatisfactionOptionDao.Properties.OrderNo)
                .limit(1)
                .unique();
        if (option != null) {
            viewPager.setCurrentItem(option.getOrderNo(), true);
            ToastUtils.show("尚有条目未评分");
        } else {
            finish();
        }
    }


    // ============================ optionOnSelect ============================

    @Subscriber(tag = "optionOnSelect")
    public void optionOnSelect(Integer position) {
        viewPager.setCurrentItem(position + 1, true);
    }


    // ================================ initData ================================

    private void initData() {
        SatisfactionOptionDao optionDao = SimpleApplication.getDaoSession().getSatisfactionOptionDao();
        optionDao.deleteAll();
        List<SatisfactionOption> optionList = new ArrayList<>();
        addOptionToList("1.保洁服务", "1.1 保洁员形象（统一着装、仪容仪表、文明礼貌、主动问好）", 1, 10, optionList);
        addOptionToList("1.保洁服务", "1.2 保洁员守时守纪，不做与工作无关的事，如聊天、脱岗、睡岗、吃东西等", 2, 10, optionList);
        addOptionToList("1.保洁服务", "1.3 卫生间清理及时，无异味，地面台面镜子无水渍", 3, 10, optionList);
        addOptionToList("1.保洁服务", "1.4 诊室/病房清洁消毒及时，地面、桌面干净", 4, 10, optionList);
        addOptionToList("1.保洁服务", "1.5 公共区域清扫及时，候诊椅摆放整齐，椅面干净、无垃圾）", 5, 10, optionList);
        addOptionToList("1.保洁服务", "1.6 垃圾桶清理及时，垃圾量未超过桶量的2/3，垃圾桶表面洁净无污渍", 6, 10, optionList);
        addOptionToList("1.保洁服务", "1.7 保洁员每日按时（中午/下班前）对所负责区域进行巡视、清洁一遍", 7, 10, optionList);
        addOptionToList("1.保洁服务", "1.8 保洁员接受过专业培训，保洁操作规范，打扫过程中放置相关标识，入室保洁提前敲门", 8, 10, optionList);
        addOptionToList("1.保洁服务", "1.9 保洁员有节能意识，能随手关灯、节约用水、主动报修", 9, 10, optionList);
        addOptionToList("1.保洁服务", "1.10 保洁主管与科室有定期巡视和沟通，能及时解决日常问题", 10, 10, optionList);
        addOptionToList("2.工程维修服务", "2.1 员工形象（统一着装、仪容仪表、文明礼貌、主动问好", 1, 10, optionList);
        addOptionToList("2.工程维修服务", "2.2 员工态度（热情、主动、认真、负责，有服务意识）", 2, 10, optionList);
        addOptionToList("2.工程维修服务", "2.3 遵时守纪，不做与工作无关的事，如聊天、脱岗、睡岗、吃东西等", 3, 10, optionList);
        addOptionToList("2.工程维修服务", "2.4 工程报修反应速度快、一般报修当日受理，应急情况10分钟内到达现场", 4, 10, optionList);
        addOptionToList("2.工程维修服务", "2.5 维修前主动与科室医护人员沟通，入室维修提前确认", 5, 10, optionList);
        addOptionToList("2.工程维修服务", "2.6 维修质量合格、效率高、无大量返工情况出现", 6, 10, optionList);
        addOptionToList("2.工程维修服务", "2.7 维修完成后，主动对维修现场进行清洁", 7, 10, optionList);
        addOptionToList("2.工程维修服务", "2.8 维修计划，能主动安排周期性科内设施检修保养", 8, 10, optionList);
        addOptionToList("2.工程维修服务", "2.9 主动上门巡检，发现解决问题大于被动报修", 9, 10, optionList);
        addOptionToList("2.工程维修服务", "2.10 维修主管有定期巡视和回访，能及时解决日常问题", 10, 10, optionList);
        addOptionToList("3.绿植服务", "3.1 绿植定期养护，植株长势良好，叶片无枯黄，无灰尘杂质", 1, 2, optionList);
        addOptionToList("3.绿植服务", "3.2 维修主管有定期巡视和回访，能及时解决日常问题", 2, 2, optionList);
        addOptionToList("4.电梯服务", "4.1 员工形象（统一着装、文明礼貌）", 1, 3, optionList);
        addOptionToList("4.电梯服务", "4.2 电梯运行平稳无故障，梯内设施正常（报警通话、摁钮、照明）", 2, 3, optionList);
        addOptionToList("4.电梯服务", "4.3 有定期主动维保巡检，发生电梯故障5分钟内到场", 3, 3, optionList);
        optionDao.insertInTx(optionList);
    }

    int count = 0;

    private void addOptionToList(String title, String content, int numerator, int denominator, List<SatisfactionOption> optionList) {
        SatisfactionOption option = new SatisfactionOption();
        option.setObjectId(UUID.randomUUID().toString());
        option.setTitle(title);
        option.setContent(content);
        option.setNumerator(numerator);
        option.setDenominator(denominator);
        option.setScore(-1);
        option.setOrderNo(count++);
        optionList.add(option);
    }


    // ================================ cancel ================================

    @OnClick(R.id.cancel)
    public void cancelOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        finish();
    }

}
