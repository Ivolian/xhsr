package unicorn.com.xhsr.satisfaction;

import org.simple.eventbus.EventBus;

import butterknife.OnClick;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.base.BaseFragment;
import unicorn.com.xhsr.other.ClickHelp;
import unicorn.com.xhsr.utils.ToastUtils;


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
        ToastUtils.show("hehe");
        EventBus.getDefault().post(new Object(), "submitOnClick");
    }

}
