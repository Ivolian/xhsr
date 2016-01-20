package unicorn.com.xhsr;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import unicorn.com.xhsr.base.BaseActivity;

/**
 * Created by Administrator on 2016/1/19.
 */
public class WaitRepairActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_repair);

        initRecyclerView();
    }

    AssistAdapter assistAdapter;
    private void initRecyclerView() {
        recyclerView.setLayoutManager(getLinearLayoutManager(this));
        assistAdapter = new AssistAdapter();
        recyclerView.setAdapter(assistAdapter);
    }

    public  LinearLayoutManager getLinearLayoutManager(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }



}
