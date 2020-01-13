package mobi.huibao.notebook.index;

import android.content.Context;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.github.tamir7.contacts.Contact;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.lukhnos.portmobile.file.Paths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mobi.huibao.notebook.api.data.ContactsResult;
import mobi.huibao.notebook.index.lucene.IndexAnalyzer;
import mobi.huibao.notebook.tools.rx.RxTools;

import static mobi.huibao.notebook.api.data.ContactsResult.ContactsItem;


public class IndexTool {

    private static String INDEX_PATH;

    private static Directory directory;

    private static Analyzer analyzer;

    private static IndexWriterConfig indexWriterConfig;

    private static IndexContactAction indexContactAction;

    /**
     * 初始化,创建索引目录,初始化索引目录,中文分词
     *
     * @param context
     * @throws IOException
     */
    public static void init(Context context) throws IOException {
        INDEX_PATH = context.getDataDir().getAbsolutePath().concat("/index");
        FileUtils.createOrExistsDir(INDEX_PATH);
        directory = FSDirectory.open(Paths.get(INDEX_PATH));
        analyzer = new IndexAnalyzer();
        indexWriterConfig = new IndexWriterConfig(analyzer);
        indexContactAction = new IndexContactAction(directory, indexWriterConfig);
    }

    public static void indexContacts(List<Contact> contactsList) {
        indexContactAction.setContacts(contactsList);
        RxTools.runOnIoThread(indexContactAction);
    }

    public static List<ContactsResult.ContactsItem> searchContact(String keyword) {

        List<ContactsResult.ContactsItem> contactsItemList = new ArrayList<>();

        try {
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("displayName", analyzer);
            Query query = parser.parse(keyword);
            LogUtils.d("searchContact->" + keyword);
            ScoreDoc[] hits = searcher.search(query, 200).scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                Document contact = searcher.doc(scoreDoc.doc);
                ContactsItem item = new ContactsItem(
                        contact.get(IndexContactAction.Columns.id.toString()) != null ? Long.valueOf(contact.get(IndexContactAction.Columns.id.toString())) : 0L,
                        contact.get(IndexContactAction.Columns.id.toString()) != null ? contact.get(IndexContactAction.Columns.displayName.toString()) : "",
                        contact.get(IndexContactAction.Columns.phoneNumber.toString()) != null ? contact.get(IndexContactAction.Columns.phoneNumber.toString()) : "",
                        "http://static.kinkr.cn/test/timg.jpg",
                        contact.get(IndexContactAction.Columns.companyName.toString()) != null ? contact.get(IndexContactAction.Columns.companyName.toString()) : "",
                        contact.get(IndexContactAction.Columns.companyTitle.toString()) != null ? contact.get(IndexContactAction.Columns.companyTitle.toString()) : ""
                );
                contactsItemList.add(item);
            }
        } catch (IOException | ParseException e) {
            LogUtils.e(e);
        }

        return contactsItemList;
    }
}
