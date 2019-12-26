package mobi.huibao.notebook.index;

import android.content.Context;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import mobi.huibao.notebook.index.lucene.HanLPIndexAnalyzer;
import mobi.huibao.notebook.tools.rx.Action;

public class IndexAction implements Action {

    private Context context;

    public IndexAction(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        LogUtils.d(context.getDataDir().toPath().toAbsolutePath().toString());
        LogUtils.d(context.getDataDir().toPath().toUri());
        FileUtils.createOrExistsDir(new File(context.getDataDir().toPath().toString()));

        FileUtils.createOrExistsDir(context.getPackageCodePath() + "/index");
        Path path = Paths.get(context.getDataDir().toURI());
        try {
            Directory directory = FSDirectory.open(path);
            Analyzer analyzer = new HanLPIndexAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            Document document = new Document();
            document.add(new TextField("context", "这是一个中文分词的例子，你可以直接运行它！", Field.Store.NO));
            document.add(new LongPoint("add_time", System.currentTimeMillis()));
            LogUtils.d(document.toString());
            indexWriter.addDocument(document);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            LogUtils.e(e);
        }

    }
}
