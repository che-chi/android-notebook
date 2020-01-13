package mobi.huibao.notebook.events;

import java.util.List;

import mobi.huibao.notebook.api.data.ContactsResult;

public class SearchContactEvent {

    private List<ContactsResult.ContactsItem> list;

    public SearchContactEvent(List<ContactsResult.ContactsItem> list) {
        this.list = list;
    }


    public List<ContactsResult.ContactsItem> getList() {
        return list;
    }
}
