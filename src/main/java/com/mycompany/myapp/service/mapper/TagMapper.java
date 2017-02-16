package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.TagDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Tag and its DTO TagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TagMapper {

    @Mapping(source = "wxApp.id", target = "wxAppId")
    TagDTO tagToTagDTO(Tag tag);

    List<TagDTO> tagsToTagDTOs(List<Tag> tags);

    @Mapping(source = "wxAppId", target = "wxApp")
    Tag tagDTOToTag(TagDTO tagDTO);

    List<Tag> tagDTOsToTags(List<TagDTO> tagDTOs);

    default WxApp wxAppFromId(Long id) {
        if (id == null) {
            return null;
        }
        WxApp wxApp = new WxApp();
        wxApp.setId(id);
        return wxApp;
    }
}
