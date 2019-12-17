package mobi.huibao.notebook.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mobi.huibao.notebook.R;
import mobi.huibao.notebook.adapter.ContactsItemAdapter;
import mobi.huibao.notebook.api.presenter.SearchPresenter;
import mobi.huibao.notebook.events.ContactsListEvent;

public class ContactsFragment extends Fragment {

    @BindView(R.id.contact_list)
    RecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    RefreshLayout refresh;

    final ContactsItemAdapter adapter = new ContactsItemAdapter(R.layout.item_contacts);

    private int page = 0;
    private int size = 10;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(ContactsListEvent event) {
        adapter.addData(event.getList());
        adapter.notifyDataSetChanged();
    }

    private void initRefreshListener(RefreshLayout refresh, ContactsItemAdapter adapter) {


        //刷新加载数据
        refresh.setOnRefreshListener(refreshLayout -> {
            page = 0;
            size = 10;
            SearchPresenter.searchContacts(page, size).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(
                            contactsResult -> {
                                adapter.getData().clear();
                                adapter.addData(contactsResult.data.list);
                                adapter.notifyDataSetChanged();
                            },
                            throwable -> {
                                Toasty.warning(getContext(), R.string.server_request_fail, Toast.LENGTH_SHORT, false).show();
                                refresh.finishRefresh();
                            }, refresh::finishRefresh).isDisposed();

        });

        //加载更多数据
        refresh.setOnLoadMoreListener(refreshLayout -> {
            page = page + 1;
            SearchPresenter.searchContacts(page, size).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(
                            contactsResult -> {
                                adapter.addData(adapter.getData().size(), contactsResult.data.list);
                                adapter.loadMoreComplete();
                            }, throwable -> {
                                refresh.finishLoadMore(false);
                                Toasty.warning(getContext(), R.string.server_request_fail, Toast.LENGTH_SHORT, false).show();
                            }, refresh::finishLoadMore).isDisposed();
        });
    }


    private void eventBusRegister() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initRecyclerView(View rootView) {
        recyclerView = rootView.findViewById(R.id.contact_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter.setEmptyView(R.layout.item_contacts_empty, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, rootView);
        eventBusRegister();
        initRecyclerView(rootView);
        initRefreshListener(refresh, adapter);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
