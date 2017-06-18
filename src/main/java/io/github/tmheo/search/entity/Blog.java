package io.github.tmheo.search.entity;

import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by taemyung on 2017. 6. 18..
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog implements Serializable {

    @JestId
    private String id;

    private String title;

    private String author;

    private String category;

    private String contents;

    private Set<String> tags;

    private Date createdAt;

}
