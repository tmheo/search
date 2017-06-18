package io.github.tmheo.search.service;

import io.github.tmheo.search.model.AutoCompleteResponse;
import io.github.tmheo.search.model.SearchResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by taemyung on 2017. 6. 18..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void testSearchAutoCompleteByTitle() throws Exception {
        searchAutoComplete("elastic");
    }

    @Test
    public void testSearchAutoCompleteByAuthor() throws Exception {
        searchAutoComplete("ja");
    }

    @Test
    public void testSearchAutoCompleteByTag() throws Exception {
        searchAutoComplete("big");
    }

    @Test
    public void testSearchByTitle() throws Exception {
        search("elastic");
    }

    @Test
    public void testSearchByAuthor() throws Exception {
        search("jo");
    }

    @Test
    public void testSearchByTag() throws Exception {
        search("big");
    }

    private void searchAutoComplete(String query) throws Exception {

        // Given

        // When
        AutoCompleteResponse autoCompleteResponse = searchService.searchAutoComplete(query);

        // Then
        assertThat(autoCompleteResponse, is(notNullValue()));

    }

    private void search(String query) throws Exception {

        // Given

        // When
        SearchResponse searchResponse = searchService.search(query);

        // Then
        assertThat(searchResponse, is(notNullValue()));

    }

}
