package mobi.huibao.notebook.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.huibao.notebook.R;
import mobi.huibao.notebook.service.ContactsService;

public class NotebookFragment extends Fragment {

    @BindView(R.id.start_service)
    Button startService;

    @BindView(R.id.stop_service)
    Button stopService;

    public static NotebookFragment newInstance() {
        return new NotebookFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_notebook, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.start_service, R.id.stop_service})
    public void serviceButtonClickListener(View view) {
        switch (view.getId()) {
            case R.id.start_service:
                Intent intentStart = new Intent(getActivity(), ContactsService.class);
                getActivity().startService(intentStart);
                break;
            case R.id.stop_service:
                Intent intentStop = new Intent(getActivity(), ContactsService.class);
                getActivity().stopService(intentStop);
        }
    }
}
