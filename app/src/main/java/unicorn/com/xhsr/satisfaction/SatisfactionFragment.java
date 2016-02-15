package unicorn.com.xhsr.satisfaction;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.base.BaseFragment;
import unicorn.com.xhsr.data.greendao.SatisfactionOption;
import unicorn.com.xhsr.data.greendao.SatisfactionOptionDao;
import unicorn.com.xhsr.other.ClickHelp;


public class SatisfactionFragment extends BaseFragment {


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_satisfation;
    }


    // ================================ color ================================

    int white;

    int green;

    private void initColor() {
        white = ContextCompat.getColor(getActivity(), R.color.md_white);
        green = ContextCompat.getColor(getActivity(), R.color.colorAccent);
    }


    // ================================  position & option ================================

    SatisfactionOption option;


    // ================================ views ================================

    @Bind(R.id.content)
    TextView content;

    @Bind({R.id.rtvZero, R.id.rvOne, R.id.rtvTwo, R.id.rtvThree, R.id.rtvFour, R.id.rtvFive})
    public List<RoundTextView> roundTextViewList;

    @Bind({R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five})
    public List<PercentLinearLayout> percentLinearLayoutList;


    // ================================ onClick ================================

    @OnClick({R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five})
    public void scoreOnClick(PercentLinearLayout percentLinearLayout) {
        if (ClickHelp.isFastClick()) {
            return;
        }
        int index = percentLinearLayoutList.indexOf(percentLinearLayout);

        // index 等于评分
        select(index);
        option.setScore(index);
        SimpleApplication.getDaoSession().getSatisfactionOptionDao().update(option);
        EventBus.getDefault().post(option.getOrderNo(), "optionOnSelect");
    }


    // ================================ initViews ================================

    @Override
    public void initViews() {
        initColor();

        int position = getArguments().getInt("position");
        option = SimpleApplication.getDaoSession().getSatisfactionOptionDao().queryBuilder()
                .where(SatisfactionOptionDao.Properties.OrderNo.eq(position))
                .unique();
        content.setText(option.getContent());
        select(option.getScore());
    }

    private void select(int score) {
        for (RoundTextView roundTextView : roundTextViewList) {
            int index = roundTextViewList.indexOf(roundTextView);
            if (index == score) {
                roundTextView.setTextColor(white);
                roundTextView.getDelegate().setBackgroundColor(green);
            } else {
                roundTextView.setTextColor(green);
                roundTextView.getDelegate().setBackgroundColor(white);
            }
        }
    }


}
