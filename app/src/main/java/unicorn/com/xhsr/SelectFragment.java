package unicorn.com.xhsr;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flipboard.bottomsheet.commons.BottomSheetFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

public class SelectFragment extends BottomSheetFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_select, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String eventTag = getArguments().getString("eventTag");
        SelectObject selectObject = (SelectObject) getArguments().getSerializable("selectObject");
        recyclerView.setAdapter(new SelectAdapter(eventTag,selectObject));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());

        if (selectObject!=null){
//            recyclerView.scrollToPosition(selectObject.position);

        }

        return rootView;
    }




}
