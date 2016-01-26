package unicorn.com.xhsr.myorder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.base.BaseFragment;


public class WaitRepairFragment extends BaseFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_wait_repair;
    }

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(getLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new WaitRepairAdapter());
    }

    public LinearLayoutManager getLinearLayoutManager(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

}
