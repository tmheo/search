package io.github.tmheo.search;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by taemyung on 2017. 6. 18..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiDocumentation {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @Test
    public void searchAutoCompleteGetExample() throws Exception {

        String query = "a";

        this.mockMvc.perform(
                get("/api/search/auto_complete")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("search-auto-complete-get-example",
                                requestParameters(
                                        parameterWithName("query").description("The search query for auto complete")
                                ),
                                responseFields(
                                        fieldWithPath("titleSuggestList.[]").description("The search result list of title"),
                                        fieldWithPath("authorSuggestList.[]").description("The search result list of author"),
                                        fieldWithPath("tagSuggestList.[]").description("The search result list of tag")
                                )
                        )
                );

    }

    @Test
    public void searchGetExample() throws Exception {

        String query = "a";

        this.mockMvc.perform(
                get("/api/search")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("search-get-example",
                                requestParameters(
                                        parameterWithName("query").description("The search query")
                                ),
                                responseFields(
                                        fieldWithPath("blogListByTitle.[]").description("The search result list of title"),
                                        fieldWithPath("blogListByTitle.[].id").description("The id of the blog"),
                                        fieldWithPath("blogListByTitle.[].title").description("The title of the blog"),
                                        fieldWithPath("blogListByTitle.[].author").description("The author of the blog"),
                                        fieldWithPath("blogListByTitle.[].category").description("The category of the blog"),
                                        fieldWithPath("blogListByTitle.[].contents").description("The contents of the blog"),
                                        fieldWithPath("blogListByTitle.[].tags.[]").description("The tag list of the blog"),
                                        fieldWithPath("blogListByTitle.[].createdAt").description("The created date of the blog"),
                                        fieldWithPath("blogListByAuthor.[]").description("The search result list of author"),
                                        fieldWithPath("blogListByAuthor.[].id").description("The id of the blog"),
                                        fieldWithPath("blogListByAuthor.[].title").description("The title of the blog"),
                                        fieldWithPath("blogListByAuthor.[].author").description("The author of the blog"),
                                        fieldWithPath("blogListByAuthor.[].category").description("the category of the blog"),
                                        fieldWithPath("blogListByAuthor.[].contents").description("The contents of the blog"),
                                        fieldWithPath("blogListByAuthor.[].tags.[]").description("The tag list of the blog"),
                                        fieldWithPath("blogListByAuthor.[].createdAt").description("The created date of the blog"),
                                        fieldWithPath("blogListByTag.[]").description("The search result list of tag"),
                                        fieldWithPath("blogListByTag.[].id").description("The id of the blog"),
                                        fieldWithPath("blogListByTag.[].title").description("The title of the blog"),
                                        fieldWithPath("blogListByTag.[].author").description("The author of the blog"),
                                        fieldWithPath("blogListByTag.[].category").description("the category of the blog"),
                                        fieldWithPath("blogListByTag.[].contents").description("The contents of the blog"),
                                        fieldWithPath("blogListByTag.[].tags.[]").description("The tag list of the blog"),
                                        fieldWithPath("blogListByTag.[].createdAt").description("The created date of the blog")

                                )
                        )
                );

    }

}
