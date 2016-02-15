package unicorn.com.xhsr.satisfaction;

import android.widget.EditText;

import org.simple.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.DialogUtils;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.base.BaseFragment;
import unicorn.com.xhsr.other.ClickHelp;


public class AdviceFragment extends BaseFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_advice;
    }

    @Override
    public void initViews() {
        // do nothing
    }

    @Bind(R.id.etAdvice)
    public EditText etAdvice;

    @OnClick(R.id.submit)
    public void submitOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        DialogUtils.showConfirm(getActivity(), "确认提交问卷？",
                new DialogUtils.Action() {
                    @Override
                    public void doAction() {
                        String advice = etAdvice.getText().toString();
                        EventBus.getDefault().post(advice, "submitOnClick");
                    }
                },
                new DialogUtils.Action() {
                    @Override
                    public void doAction() {
                        // do nothing
                    }
                }
        );
    }

}
