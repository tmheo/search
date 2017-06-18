package io.github.tmheo.search.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by taemyung on 2017. 6. 18..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).alwaysDo(MockMvcResultHandlers.print()).build();
    }

    @Test
    public void testSearchAutoCompleteByTitle() throws Exception {
        searchAutoComplete("elastic");
    }

    @Test
    public void testSearchAutoCompleteByAuthor() throws Exception {
        searchAutoComplete("jane");
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
        search("jane");
    }

    @Test
    public void testSearchByTag() throws Exception {
        search("big");
    }

    private void searchAutoComplete(String query) throws Exception {

        // Given

        // When
        this.mockMvc.perform(get("/api/search/auto_complete")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("query", query))

                // Then
                .andExpect(status().isOk());

    }

    private void search(String query) throws Exception {

        // Given

        // When
        this.mockMvc.perform(get("/api/search")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("query", query))

                // Then
                .andExpect(status().isOk());

    }

}
