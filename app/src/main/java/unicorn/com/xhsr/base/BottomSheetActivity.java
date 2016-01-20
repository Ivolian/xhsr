package unicorn.com.xhsr.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import unicorn.com.xhsr.R;
import unicorn.com.xhsr.select.SelectAdapter;
import unicorn.com.xhsr.select.SelectObject;


public class BottomSheetActivity extends BaseActivity {

    @Bind(R.id.bottomsheet)
    public BottomSheetLayout bottomSheet;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initBottomSheet();
    }

    @SuppressWarnings("deprecation")
    private void initBottomSheet() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int height = windowManager.getDefaultDisplay().getHeight();
        bottomSheet.setPeekSheetTranslation(height * 0.65f);
    }

    public void showSelectSheet(String name, SelectAdapter.DataProvider dataProvider, String objectIdSelected, String eventTag) {
        View rootView = LayoutInflater.from(this).inflate(R.layout.select_sheet, bottomSheet, false);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.title);
        String title = "选择" + name;
        tvTitle.setText(title);
        rootView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int positionSelected = getPositionSelected(dataProvider, objectIdSelected);
        if (positionSelected != -1) {
            recyclerView.smoothScrollToPosition(positionSelected);
        }
        recyclerView.setAdapter(new SelectAdapter(dataProvider, positionSelected, eventTag));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

        bottomSheet.showWithSheetView(rootView);
    }

    private int getPositionSelected(SelectAdapter.DataProvider dataProvider, String objectIdSelected) {
        List<SelectObject> selectObjectList = dataProvider.getDataList();
        for (SelectObject selectObject : selectObjectList) {
            if (TextUtils.equals(objectIdSelected,selectObject.objectId)) {
                return selectObjectList.indexOf(selectObject);
            }
        }
        return -1;
    }

}
