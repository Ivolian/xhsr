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
import unicorn.com.xhsr.utils.ToastUtils;


public class SatisfactionFragment extends BaseFragment {

    int white;
    int green;
    SatisfactionOption option;

    SatisfactionOptionDao satisfactionOptionDao;

    @Bind(R.id.content)
    TextView content;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_satisfation;
    }

    @Bind({R.id.rtvZero, R.id.rvOne, R.id.rtvTwo, R.id.rtvThree, R.id.rtvFour, R.id.rtvFive})
    public List<RoundTextView> roundTextViewList;

    @Bind({R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five})
    public List<PercentLinearLayout> percentLinearLayoutList;

    @OnClick({R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five})
    public void fiveOnClick(PercentLinearLayout percentLinearLayout) {
        if (ClickHelp.isFastClick()) {
            return;
        }

        int clickIndex = percentLinearLayoutList.indexOf(percentLinearLayout);
        for (RoundTextView roundTextView : roundTextViewList) {
            int rtvIndex = roundTextViewList.indexOf(roundTextView);
            if (rtvIndex == clickIndex) {
                roundTextView.setTextColor(white);
                roundTextView.getDelegate().setBackgroundColor(green);
            } else {
                roundTextView.setTextColor(green);
                roundTextView.getDelegate().setBackgroundColor(white);
            }
        }
        option.setScore(clickIndex);
        satisfactionOptionDao.update(option);


        int position = getArguments().getInt("position");
        ToastUtils.show(position);
        EventBus.getDefault().post(position, "optionOnSelect");
    }

    @Override
    public void initViews() {
        white = ContextCompat.getColor(getActivity(), R.color.md_white);
        green = ContextCompat.getColor(getActivity(), R.color.colorAccent);
        satisfactionOptionDao = SimpleApplication.getDaoSession().getSatisfactionOptionDao();
        int position = getArguments().getInt("position");
        option = satisfactionOptionDao.queryBuilder().where(SatisfactionOptionDao.Properties.OrderNo.eq(position)).unique();
        content.setText(option.getContent());
        int score = option.getScore();
        if (score != -1) {
            RoundTextView roundTextView = roundTextViewList.get(score);
            roundTextView.setTextColor(white);
            roundTextView.getDelegate().setBackgroundColor(green);
        }
    }


}
