package mobi.huibao.notebook.index;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

import mobi.huibao.notebook.index.lucene.IndexAnalyzer;
import mobi.huibao.notebook.tools.rx.Action;

public class IndexAction implements Action {

    private Context context;

    public IndexAction(Context context) {
        this.context = context;
    }

    @Override
    public void run() {

        try {
            Directory directory = FSDirectory.open(context.getDataDir());
            Analyzer analyzer = new IndexAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            Document document = new Document();
            document.add(new Field("context", "这是一个中文分词的例子，你可以直接运行它！", Field.Store.NO, Field.Index.ANALYZED));
//            document.add(new Field("add_time", System.currentTimeMillis(), Field.Index.ANALYZED));
            LogUtils.d(document.toString());
            indexWriter.addDocument(document);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            LogUtils.e(e);
        }

    }
}
