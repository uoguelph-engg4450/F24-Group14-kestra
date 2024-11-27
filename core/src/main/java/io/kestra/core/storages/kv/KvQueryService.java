package io.kestra.core.storages;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.util.ArrayList;
import java.util.List;

public class KvQueryService {
    private final Directory directory;
    private final StandardAnalyzer analyzer;

    public KvQueryService() {
        this.directory = new RAMDirectory(); // In-memory directory for the Lucene index
        this.analyzer = new StandardAnalyzer();
    }

    /**
     * Index a key-value pair into the Lucene index.
     *
     * @param key   The key of the data.
     * @param value The value of the data.
     * @throws Exception if indexing fails.
     */
    public void indexKv(String key, String value) throws Exception {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(directory, config)) {
            Document doc = new Document();
            doc.add(new StringField("key", key, org.apache.lucene.document.Field.Store.YES));
            doc.add(new TextField("value", value, org.apache.lucene.document.Field.Store.YES));
            writer.addDocument(doc);
        }
    }

    /**
     * Query the Lucene index for key-value pairs matching the given value.
     *
     * @param searchValue The value to search for.
     * @return A list of matching keys.
     * @throws Exception if querying fails.
     */
    public List<String> queryKv(String searchValue) throws Exception {
        List<String> results = new ArrayList<>();
        try (DirectoryReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);

            // Use QueryParser to search the value field
            QueryParser parser = new QueryParser("value", analyzer);
            Query query = parser.parse(searchValue);

            // Perform the search
            ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;
            for (ScoreDoc hit : hits) {
                Document doc = searcher.doc(hit.doc);
                results.add(doc.get("key"));
            }
        }
        return results;
    }
}


