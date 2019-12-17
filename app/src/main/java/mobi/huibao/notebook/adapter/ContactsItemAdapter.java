package mobi.huibao.notebook.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import mobi.huibao.notebook.R;
import mobi.huibao.notebook.api.data.ContactsResult;


public class ContactsItemAdapter extends BaseQuickAdapter<ContactsResult.ContactsItem, BaseViewHolder> {


    public ContactsItemAdapter(@NonNull int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ContactsResult.ContactsItem item) {

        helper.setText(R.id.contacts_name, item.getName());
        helper.setText(R.id.contacts_phone, item.getPhone());
        helper.setText(R.id.contacts_company, item.getCompany());
        helper.setText(R.id.contacts_job_title, item.getJobTitle());

        SimpleDraweeView icon = helper.getView(R.id.contacts_icon);
        icon.setImageURI(Uri.parse(item.getIcon()));

        helper.addOnClickListener(R.id.contacts_name, R.id.contacts_name);
    }
}
