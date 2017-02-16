package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CommentDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Comment and its DTO CommentDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface CommentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "wxapp.id", target = "wxappId")
    @Mapping(source = "wxapp.name", target = "wxappName")
    CommentDTO commentToCommentDTO(Comment comment);

    List<CommentDTO> commentsToCommentDTOs(List<Comment> comments);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "wxappId", target = "wxapp")
    Comment commentDTOToComment(CommentDTO commentDTO);

    List<Comment> commentDTOsToComments(List<CommentDTO> commentDTOs);

    default WxApp wxAppFromId(Long id) {
        if (id == null) {
            return null;
        }
        WxApp wxApp = new WxApp();
        wxApp.setId(id);
        return wxApp;
    }
}
