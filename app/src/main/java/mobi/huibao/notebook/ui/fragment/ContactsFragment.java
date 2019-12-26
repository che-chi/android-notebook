package mobi.huibao.notebook.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mobi.huibao.notebook.R;
import mobi.huibao.notebook.adapter.ContactsItemAdapter;
import mobi.huibao.notebook.api.data.ContactsResult;
import mobi.huibao.notebook.api.presenter.SearchPresenter;
import mobi.huibao.notebook.events.ContactsListEvent;
import mobi.huibao.notebook.index.IndexAction;
import mobi.huibao.notebook.tools.rx.RxTools;

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

    private void check(ContactsItemAdapter adapter) {
        new RxPermissions(this).requestEach(Manifest.permission.READ_CONTACTS)
                .subscribe(permission -> {
                    DialogInterface.OnClickListener onClickListener = (dialogInterface, i) ->
                            Toast.makeText(getActivity(), "确定按钮", Toast.LENGTH_LONG).show();

                    if (permission.granted) {

                        IndexAction indexAction = new IndexAction(getContext());
                        RxTools.runOnIoThread(indexAction);

                        //授权执行的操作,比如读取用户通信录,对通信录进行索引操纵
                        List<Contact> contacts = Contacts.getQuery().find();
                        List<ContactsResult.ContactsItem> itemList = new ArrayList<>(contacts.size());
                        for (Contact contact : contacts) {
                            ContactsResult.ContactsItem item = new ContactsResult.ContactsItem(
                                    contact.getId(),
                                    contact.getDisplayName(),
                                    contact.getPhoneNumbers().get(0).getNumber(),
                                    "http://static.kinkr.cn/test/timg.jpg",
                                    contact.getCompanyName(),
                                    "测试职位");
                            itemList.add(item);
                        }
                        adapter.addData(itemList);

                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //拒绝授权,给用户推荐相关联系人信息
                        new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.ic_launcher).setTitle("shouldShowRequestPermissionRationale")
                                .setMessage("shouldShowRequestPermissionRationale").setPositiveButton("确定（积极）", onClickListener).setNegativeButton("取消（消极）", (dialogInterface, i) -> {
                            Toast.makeText(getActivity(), "关闭按钮", Toast.LENGTH_LONG).show();
                            dialogInterface.dismiss();
                        }).create().show();
                    } else {
                        //用户拒绝授权,并且一户不在提醒,可以每次提醒用户授权
                        new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.ic_launcher).setTitle("shouldShowRequestPermissionRationale")
                                .setMessage("shouldShowRequestPermissionRationale").setPositiveButton("确定（积极）", onClickListener).setNegativeButton("取消（消极）", (dialogInterface, i) -> {
                            Toast.makeText(getActivity(), "关闭按钮", Toast.LENGTH_LONG).show();
                            dialogInterface.dismiss();
                        }).create().show();
                    }
                }).isDisposed();
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
        check(adapter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

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
