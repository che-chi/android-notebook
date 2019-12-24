package mobi.huibao.notebook.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.huibao.notebook.R;
import mobi.huibao.notebook.service.ContactsService;

public class UserFragment extends Fragment {


    @BindView(R.id.start_command_1)
    Button startCommand1;

    @BindView(R.id.start_command_2)
    Button startCommand2;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.start_command_1, R.id.start_command_2})
    public void setStartCommand(View view) {
        switch (view.getId()) {
            case R.id.start_command_1:
                ContactsService.startCommand(getActivity(), 1);
                break;
            case R.id.start_command_2:
                ContactsService.startCommand(getActivity(), 2);
                break;
            default:
                break;
        }
    }
}
