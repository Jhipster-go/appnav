package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.WxAppDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity WxApp and its DTO WxAppDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface WxAppMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userFirstName")
    WxAppDTO wxAppToWxAppDTO(WxApp wxApp);

    List<WxAppDTO> wxAppsToWxAppDTOs(List<WxApp> wxApps);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(source = "userId", target = "user")
    WxApp wxAppDTOToWxApp(WxAppDTO wxAppDTO);

    List<WxApp> wxAppDTOsToWxApps(List<WxAppDTO> wxAppDTOs);
}
