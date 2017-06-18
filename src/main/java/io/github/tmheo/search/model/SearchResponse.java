package io.github.tmheo.search.model;

import io.github.tmheo.search.entity.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taemyung on 2017. 6. 18..
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse implements Serializable {

    private List<Blog> blogListByTitle;
    private List<Blog> blogListByAuthor;
    private List<Blog> blogListByTag;

}
