package io.github.tmheo.search.controller;

import io.github.tmheo.search.model.AutoCompleteResponse;
import io.github.tmheo.search.model.SearchResponse;
import io.github.tmheo.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by taemyung on 2017. 6. 18..
 */
@RestController
@RequestMapping("/api/search")
@Slf4j
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/auto_complete")
    public AutoCompleteResponse searchAutoComplete(@RequestParam String query) {

        log.debug("search auto complete request for query[{}]", query);

        AutoCompleteResponse autoCompleteResponse = searchService.searchAutoComplete(query);

        log.debug("search auto complete response for query[{}] : {}", query, autoCompleteResponse);

        return autoCompleteResponse;

    }

    @GetMapping
    public SearchResponse search(@RequestParam String query) {

        log.debug("search request for query[{}]", query);

        SearchResponse searchResponse = searchService.search(query);

        log.debug("search response for query[{}] : {}", query, searchResponse);

        return searchResponse;

    }

}
