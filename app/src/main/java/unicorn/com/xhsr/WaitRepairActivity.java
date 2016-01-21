package unicorn.com.xhsr;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.OnClick;
import unicorn.com.xhsr.base.BaseActivity;


public class WaitRepairActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_repair);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(getLinearLayoutManager(this));
        recyclerView.setAdapter(new WaitRepairAdapter());
    }

    public LinearLayoutManager getLinearLayoutManager(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }


    @OnClick(R.id.cancel)
    public void cancel() {
        finish();
    }




}
