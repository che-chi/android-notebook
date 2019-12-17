package mobi.huibao.notebook.events;

import java.util.List;

import mobi.huibao.notebook.api.data.ContactsResult;

public class ContactsListEvent {

    private List<ContactsResult.ContactsItem> list;

    public ContactsListEvent(List<ContactsResult.ContactsItem> list) {
        this.list = list;
    }


    public List<ContactsResult.ContactsItem> getList() {
        return list;
    }
}
