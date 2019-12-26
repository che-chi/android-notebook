package mobi.huibao.notebook.index.lucene;

import com.hankcs.hanlp.HanLP;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import java.io.Reader;
import java.util.Set;

public class IndexAnalyzer extends Analyzer {

    private boolean pstemming;
    private Set<String> filter;

    /**
     * @param filter    停用词
     * @param pstemming 是否分析词干
     */
    public IndexAnalyzer(Set<String> filter, boolean pstemming) {
        this.filter = filter;
        this.pstemming = pstemming;
    }

    /**
     * @param pstemming 是否分析词干.进行单复数,时态的转换
     */
    public IndexAnalyzer(boolean pstemming) {
        this.pstemming = pstemming;
    }

    public IndexAnalyzer() {
        super();
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return new IndexTokenizer(HanLP.newSegment().enableIndexMode(true), filter, pstemming,reader);
    }

}
