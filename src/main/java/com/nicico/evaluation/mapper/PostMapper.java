package com.nicico.evaluation.mapper;

import com.nicico.evaluation.dto.PostDTO;
import com.nicico.evaluation.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    public abstract PostDTO.Info entityToDtoInfo(Post entity);

    public abstract List<PostDTO.Info> entityToDtoInfoList(List<Post> entities);


}
