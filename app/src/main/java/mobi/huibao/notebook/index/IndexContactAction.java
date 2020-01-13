package mobi.huibao.notebook.index;

import com.blankj.utilcode.util.LogUtils;
import com.github.tamir7.contacts.Contact;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

import mobi.huibao.notebook.tools.rx.Action;

public class IndexContactAction implements Action {

    private Directory directory;
    private IndexWriterConfig indexWriterConfig;

    private List<Contact> contacts;

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public IndexContactAction(Directory directory, IndexWriterConfig indexWriterConfig) {
        this.directory = directory;
        this.indexWriterConfig = indexWriterConfig;
    }

    public enum Columns {
        id,
        displayName,
        companyName,
        companyTitle,
        phoneNumber,
        address,
        addTime,
        updateTime
    }

    private Document createContactDocument(Contact contact) {
        Document document = new Document();
        document.add(new LongField(Columns.id.toString(), contact.getId(), Field.Store.YES));
        document.add(new TextField(Columns.displayName.toString(), contact.getDisplayName(), Field.Store.YES));
        document.add(new TextField(Columns.companyName.toString(), contact.getCompanyName() != null ? contact.getCompanyName() : "", Field.Store.YES));
        document.add(new TextField(Columns.companyTitle.toString(), contact.getCompanyTitle() != null ? contact.getCompanyTitle() : "", Field.Store.YES));
        document.add(new TextField(Columns.phoneNumber.toString(), contact.getPhoneNumbers().size() > 0 ? contact.getPhoneNumbers().get(0).getNumber() : "", Field.Store.YES));
        document.add(new TextField(Columns.address.toString(), contact.getAddresses().size() > 0 ? contact.getAddresses().get(0).toString() : "", Field.Store.YES));
        document.add(new LongField(Columns.addTime.toString(), System.currentTimeMillis(), Field.Store.YES));
        document.add(new LongField(Columns.updateTime.toString(), System.currentTimeMillis(), Field.Store.YES));
        return document;
    }

    @Override
    public void run() {
        this.indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try {
            IndexWriter indexWriter = new IndexWriter(directory, this.indexWriterConfig);
            LogUtils.d(contacts.size());
            for (Contact c : contacts) {
                indexWriter.addDocument(createContactDocument(c));
            }
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            LogUtils.e(e);
        }
    }
}
