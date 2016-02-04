package unicorn.com.xhsr.satisfaction;

import org.simple.eventbus.EventBus;

import butterknife.OnClick;
import unicorn.com.xhsr.DialogUtils;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.base.BaseFragment;
import unicorn.com.xhsr.other.ClickHelp;


public class AdviseFragment extends BaseFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_advise;
    }

    @Override
    public void initViews() {

    }

    @OnClick(R.id.submit)
    public void submitOnClick() {
        if (ClickHelp.isFastClick()) {
            return;
        }
        DialogUtils.showConfirm(getActivity(), "确认提交问卷？",
                new DialogUtils.Action() {
                    @Override
                    public void doAction() {
                        EventBus.getDefault().post(new Object(), "submitOnClick");
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
