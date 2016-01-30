package unicorn.com.xhsr.satisfation;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.SimpleApplication;
import unicorn.com.xhsr.base.BaseFragment;
import unicorn.com.xhsr.data.greendao.SatisfactionOption;
import unicorn.com.xhsr.data.greendao.SatisfactionOptionDao;


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
