package io.github.tmheo.search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by taemyung on 2017. 6. 18..
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoCompleteResponse implements Serializable {

    private Set<String> titleSuggestList;
    private Set<String> authorSuggestList;
    private Set<String> tagSuggestList;

}
