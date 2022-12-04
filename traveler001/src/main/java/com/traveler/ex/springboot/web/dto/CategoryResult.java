package com.traveler.ex.springboot.web.dto;

import com.traveler.ex.springboot.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResult {
    private Long id;
    private String name;
    private Long depth;
    private List<CategoryResult> children;

    public static CategoryResult of(Category category) {
        return new CategoryResult(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getChildren().stream().map(CategoryResult::of).collect(Collectors.toList())
        ); //Entity에서 list로 담은 children을 DTO에서는 Map으로 구현 (stream, Collectors 사용)
    }
}