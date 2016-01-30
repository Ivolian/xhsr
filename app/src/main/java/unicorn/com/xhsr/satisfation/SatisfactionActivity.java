package unicorn.com.xhsr.satisfation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

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


public class SatisfactionActivity extends BaseActivity {

    @Bind(R.id.numerator)
    TextView numerator;

    @Bind(R.id.denominator)
    TextView denominator;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @OnClick(R.id.more)
    public void moreOnClick(View click){


        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, click);

        droppyBuilder.addMenuItem(new DroppyMenuItem("快速滑动")
)
.build().show();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satisfaction);
        initData();
        initViewpager();

        SatisfactionOptionDao optionDao = SimpleApplication.getDaoSession().getSatisfactionOptionDao();
        SatisfactionOption option = optionDao.queryBuilder().where(SatisfactionOptionDao.Properties.OrderNo.eq(0)).unique();
        title.setText(option.getTitle());
        numerator.setText(option.getNumerator()+"");
        denominator.setText("/"+option.getDenominator()+"");

    }

    private void initViewpager() {
        viewPager.setAdapter(new SatisfactionPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SatisfactionOptionDao optionDao = SimpleApplication.getDaoSession().getSatisfactionOptionDao();
                SatisfactionOption option = optionDao.queryBuilder().where(SatisfactionOptionDao.Properties.OrderNo.eq(position)).unique();
                title.setText(option.getTitle());
                numerator.setText(option.getNumerator()+"");
                denominator.setText("/"+option.getDenominator()+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


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


}
