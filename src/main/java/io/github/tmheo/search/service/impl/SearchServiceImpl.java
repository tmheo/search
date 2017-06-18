package io.github.tmheo.search.service.impl;

import io.github.tmheo.search.entity.Blog;
import io.github.tmheo.search.model.AutoCompleteResponse;
import io.github.tmheo.search.model.SearchResponse;
import io.github.tmheo.search.service.SearchService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by taemyung on 2017. 6. 18..
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private JestClient jestClient;

    @Override
    public AutoCompleteResponse searchAutoComplete(String query) {

        log.debug("search auto complete request for query[{}]", query);

        AutoCompleteResponse autoCompleteResponse = new AutoCompleteResponse();

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

        SearchResult searchResult = null;

        try {
            searchResult = jestClient.execute(suggestSearch);
        } catch (IOException e) {
            log.error("error when completion suggest by query[{}]", query, e);
        }

        Map<String, Object> suggestResult = (Map<String, Object>) searchResult.getValue("suggest");

        // title auto complete search result
        Set<String> titleSuggestList = getAutoCompleteSearchResult((List<Map<String, Object>>) suggestResult.get("title.suggest"));
        autoCompleteResponse.setTitleSuggestList(titleSuggestList);

        // author auto complete search result
        Set<String> authorSuggestList = getAutoCompleteSearchResult((List<Map<String, Object>>) suggestResult.get("author.suggest"));
        autoCompleteResponse.setAuthorSuggestList(authorSuggestList);

        // tag auto complete search result
        Set<String> tagSuggestList = getAutoCompleteSearchResult((List<Map<String, Object>>) suggestResult.get("tags.suggest"));
        autoCompleteResponse.setTagSuggestList(tagSuggestList);

        log.debug("search auto complete response for query[{}] : {}", query, autoCompleteResponse);

        return autoCompleteResponse;

    }

    private Set<String> getAutoCompleteSearchResult(List<Map<String, Object>> suggests) {

        Set<String> autoCompleteSearchResult = new HashSet<>();

        List<Map<String, Object>> options = (List<Map<String, Object>>) suggests.get(0).get("options");
        if (options.size() > 0) {
            for (Map<String, Object> option : options) {
                log.debug("option : {}", option);
                autoCompleteSearchResult.add(option.get("text").toString());
            }
        }

        return autoCompleteSearchResult;

    }

    @Override
    public SearchResponse search(String query) {

        log.debug("search request for query[{}]", query);

        String titleQuery = BlogQueryBuilder.buildByTitle(query);

        Search titleSearch = new Search.Builder(titleQuery)
                .addIndex("article")
                .addType("blog")
                .build();

        String authorQuery = BlogQueryBuilder.buildByAuthor(query);

        Search authorSearch = new Search.Builder(authorQuery)
                .addIndex("article")
                .addType("blog")
                .build();

        String tagsQuery = BlogQueryBuilder.buildByTag(query);

        Search tagsSearch = new Search.Builder(tagsQuery)
                .addIndex("article")
                .addType("blog")
                .build();

        MultiSearch multiSearch = new MultiSearch.Builder(Arrays.asList(titleSearch, authorSearch, tagsSearch)).build();
        MultiSearchResult multiSearchResult = null;
        try {
            multiSearchResult = jestClient.execute(multiSearch);
        } catch (IOException e) {
            log.error("error when multi search for query[{}]", query, e);
        }

        SearchResponse searchResponse = new SearchResponse();

        if (multiSearchResult != null && multiSearchResult.getResponses().size() == 3) {

            List<MultiSearchResult.MultiSearchResponse> responses = multiSearchResult.getResponses();

            // title search result
            List<Blog> blogListByTitle = getSearchResult(responses.get(0).searchResult);
            searchResponse.setBlogListByTitle(blogListByTitle);

            // author search result
            List<Blog> blogListByAuthor = getSearchResult(responses.get(1).searchResult);
            searchResponse.setBlogListByAuthor(blogListByAuthor);

            // tag search result
            List<Blog> blogListByTag = getSearchResult(responses.get(2).searchResult);
            searchResponse.setBlogListByTag(blogListByTag);

        }

        log.debug("search response for query[{}] : {}", query, searchResponse);

        return searchResponse;

    }

    private List<Blog> getSearchResult(SearchResult searchResult) {

        List<Blog> blogList = searchResult.getHits(Blog.class).stream().map(hit -> {
            Blog blog = hit.source;
            return blog;
        }).collect(Collectors.toList());

        log.debug("search result : {}", blogList);

        return blogList;

    }

    private static class BlogQueryBuilder {

        static String build(String field, String query) {

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(
                    QueryBuilders
                            .queryStringQuery("*" + query + "*")
                            .field(field))
                    .highlighter(new HighlightBuilder()
                            .field(field)
                            .preTags("")
                            .postTags(""));

            return searchSourceBuilder.toString().replaceAll("\\n", "");

        }

        static String buildByTitle(String query) {
            return build("title", query);
        }

        static String buildByAuthor(String query) {
            return build("author", query);
        }

        static String buildByTag(String query) {
            return build("tags", query);
        }

    }

}
