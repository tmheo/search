package io.github.tmheo.search.service;

import io.github.tmheo.search.model.AutoCompleteResponse;
import io.github.tmheo.search.model.SearchResponse;

/**
 * Created by taemyung on 2017. 6. 18..
 */
public interface SearchService {

    AutoCompleteResponse searchAutoComplete(String query);

    SearchResponse search(String query);

}
