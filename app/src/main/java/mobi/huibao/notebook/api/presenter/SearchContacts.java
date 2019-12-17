package mobi.huibao.notebook.api.presenter;

import io.reactivex.Observable;
import mobi.huibao.notebook.api.data.ContactsResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchContacts {

    @GET("/search.php")
    Observable<ContactsResult> searchContacts(@Query("page") int page, @Query("size") int size);
}
