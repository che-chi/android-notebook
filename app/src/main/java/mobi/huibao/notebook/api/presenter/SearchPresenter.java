package mobi.huibao.notebook.api.presenter;

import io.reactivex.Observable;
import mobi.huibao.notebook.api.net.HttpClient;
import mobi.huibao.notebook.api.data.ContactsResult;

public class SearchPresenter {

    public static Observable<ContactsResult> searchContacts(int page, int size) {
        return new HttpClient<SearchContacts>().getApi(SearchContacts.class).searchContacts(page, size);
    }

}
