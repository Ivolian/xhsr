package unicorn.com.xhsr.sheetSelect;

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
import unicorn.com.xhsr.base.BaseActivity;
import unicorn.com.xhsr.sheetSelect.model.SelectObject;


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
        bottomSheet = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int height = windowManager.getDefaultDisplay().getHeight();
        bottomSheet.setPeekSheetTranslation(height * 0.65f);
    }

    public void showSelectSheet(String name, List<SelectObject> dataList, String idSelected, String callbackTag) {
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
        int positionSelected = getPositionSelected(dataList, idSelected);
        if (positionSelected != -1) {
            recyclerView.smoothScrollToPosition(positionSelected);
        }
        recyclerView.setAdapter(new SheetSelectAdapter(dataList, positionSelected, callbackTag));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

        bottomSheet.showWithSheetView(rootView);
    }

    private int getPositionSelected(List<SelectObject> dataList, String idSelected) {
        for (SelectObject selectObject : dataList) {
            if (TextUtils.equals(idSelected, selectObject.objectId)) {
                return dataList.indexOf(selectObject);
            }
        }
        return -1;
    }

}
