package io.github.tmheo.search;

import io.github.tmheo.search.entity.Blog;
import io.searchbox.client.JestClient;
import io.searchbox.core.MultiSearch;
import io.searchbox.core.MultiSearchResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * Created by taemyung on 2017. 6. 18..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JestTest {

    @Autowired
    private JestClient jestClient;

    @Test
    public void testQueryBlog() throws Exception {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("category", "tech"));

        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex("article")
                .addType("blog")
                .build();

        SearchResult searchResult = jestClient.execute(search);

        List<SearchResult.Hit<Blog, Void>> hits = searchResult.getHits(Blog.class);

        for (SearchResult.Hit hit : hits) {
            Blog blog = (Blog) hit.source;
            log.debug("query result : {}", blog);
        }

    }

    @Test
    public void testMultiQueryBlog() throws Exception {

        String query = "big";

        SearchSourceBuilder titleSearchSourceBuilder = new SearchSourceBuilder();
        titleSearchSourceBuilder.query(
                QueryBuilders
                        .queryStringQuery("*" + query + "*")
                        .field("title"))
                .highlighter(new HighlightBuilder()
                        .field("title")
                        .preTags("")
                        .postTags(""));

        Search titleSearch = new Search.Builder(titleSearchSourceBuilder.toString().replaceAll("\\n", ""))
                .addIndex("article")
                .addType("blog")
                .build();

        SearchSourceBuilder authorSearchSourceBuilder = new SearchSourceBuilder();
        authorSearchSourceBuilder.query(
                QueryBuilders
                        .queryStringQuery("*" + query + "*")
                        .field("author"))
                .highlighter(new HighlightBuilder()
                        .field("author")
                        .preTags("")
                        .postTags(""));

        Search authorSearch = new Search.Builder(authorSearchSourceBuilder.toString().replaceAll("\\n", ""))
                .addIndex("article")
                .addType("blog")
                .build();

        SearchSourceBuilder tagSearchSourceBuilder = new SearchSourceBuilder();
        tagSearchSourceBuilder.query(
                QueryBuilders
                        .queryStringQuery("*" + query + "*")
                        .field("tags"))
                .highlighter(new HighlightBuilder()
                        .field("tags")
                        .preTags("")
                        .postTags(""));

        Search tagSearch = new Search.Builder(tagSearchSourceBuilder.toString().replaceAll("\\n", ""))
                .addIndex("article")
                .addType("blog")
                .build();

        MultiSearch multiSearch = new MultiSearch.Builder(Arrays.asList(titleSearch, authorSearch, tagSearch)).build();
        MultiSearchResult multiSearchResult = jestClient.execute(multiSearch);

        List<MultiSearchResult.MultiSearchResponse> responses = multiSearchResult.getResponses();

        for (MultiSearchResult.MultiSearchResponse response : responses) {
            SearchResult searchResult = response.searchResult;

            List<SearchResult.Hit<Blog, Void>> hits = searchResult.getHits(Blog.class);

            for (SearchResult.Hit hit : hits) {
                Blog game = (Blog) hit.source;
                log.debug("multi query result : {}", game);

                Collection highlights = hit.highlight.values();
                log.debug("highlights : {}", highlights);

                Set<String> autoCompleteResult = new HashSet<>();

                for (Object key : hit.highlight.keySet()) {
                    log.debug("key : {}", key);
                    List<String> list = (List<String>) hit.highlight.get(key);
                    log.debug("list : {}", list);
                    autoCompleteResult.addAll(list);
                }

                log.debug("auto complete result : {}", autoCompleteResult);
            }
        }

    }

    @Test
    public void testSuggestBlog() throws Exception {

        String query = "ap";

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .query(
                        QueryBuilders.multiMatchQuery(query, "title", "author", "tags")
                                .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX)
                ).suggest(new SuggestBuilder()
                        .addSuggestion("title.suggest", new CompletionSuggestionBuilder("title.suggest")
                                .prefix(query))
                        .addSuggestion("author.suggest", new CompletionSuggestionBuilder("author.suggest")
                                .prefix(query))
                        .addSuggestion("tags.suggest", new CompletionSuggestionBuilder("tags.suggest")
                                .prefix(query))
        );

        Search suggestSearch = new Search.Builder(searchSourceBuilder.toString())
                .addIndex("article")
                .addType("blog")
                .build();

        SearchResult searchResult = jestClient.execute(suggestSearch);
        Map<String, Object> suggestResult = (Map<String, Object>) searchResult.getValue("suggest");
        log.debug("[{}] suggest result : {}", suggestResult.getClass().getName(), suggestResult);

    }

}
