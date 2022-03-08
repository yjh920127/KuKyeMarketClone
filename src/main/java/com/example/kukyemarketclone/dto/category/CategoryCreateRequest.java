package com.example.kukyemarketclone.dto.category;

import com.example.kukyemarketclone.entity.category.Category;
import com.example.kukyemarketclone.exception.CategoryNotFoundException;
import com.example.kukyemarketclone.repository.category.CategoryRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;

@ApiModel(value = "카테고리 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {

    @ApiModelProperty(value = "카테고리 명",notes = "카테고리명을 입력해주세요", required = true, example = "my category")
    @NotBlank(message = "{categoryCreateRequest.name.notBlank}")
    @Size(min = 2, max= 30, message = "{categoryCreateRequest.name.size}")
    private String name;

    @ApiModelProperty(value = "부모 카테고리 아이디", notes = "부모 카테고리 아이디를 입력해주세요", required = true, example = "7")
    private Long parentId;

}
