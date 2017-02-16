package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WxApp;
import com.mycompany.myapp.repository.WxAppRepository;
import com.mycompany.myapp.repository.search.WxAppSearchRepository;
import com.mycompany.myapp.service.dto.WxAppDTO;
import com.mycompany.myapp.service.mapper.WxAppMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WxApp.
 */
@Service
@Transactional
public class WxAppService {

    private final Logger log = LoggerFactory.getLogger(WxAppService.class);
    
    private final WxAppRepository wxAppRepository;

    private final WxAppMapper wxAppMapper;

    private final WxAppSearchRepository wxAppSearchRepository;

    public WxAppService(WxAppRepository wxAppRepository, WxAppMapper wxAppMapper, WxAppSearchRepository wxAppSearchRepository) {
        this.wxAppRepository = wxAppRepository;
        this.wxAppMapper = wxAppMapper;
        this.wxAppSearchRepository = wxAppSearchRepository;
    }

    /**
     * Save a wxApp.
     *
     * @param wxAppDTO the entity to save
     * @return the persisted entity
     */
    public WxAppDTO save(WxAppDTO wxAppDTO) {
        log.debug("Request to save WxApp : {}", wxAppDTO);
        WxApp wxApp = wxAppMapper.wxAppDTOToWxApp(wxAppDTO);
        wxApp = wxAppRepository.save(wxApp);
        WxAppDTO result = wxAppMapper.wxAppToWxAppDTO(wxApp);
        wxAppSearchRepository.save(wxApp);
        return result;
    }

    /**
     *  Get all the wxApps.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WxAppDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WxApps");
        Page<WxApp> result = wxAppRepository.findAll(pageable);
        return result.map(wxApp -> wxAppMapper.wxAppToWxAppDTO(wxApp));
    }

    /**
     *  Get one wxApp by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public WxAppDTO findOne(Long id) {
        log.debug("Request to get WxApp : {}", id);
        WxApp wxApp = wxAppRepository.findOne(id);
        WxAppDTO wxAppDTO = wxAppMapper.wxAppToWxAppDTO(wxApp);
        return wxAppDTO;
    }

    /**
     *  Delete the  wxApp by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WxApp : {}", id);
        wxAppRepository.delete(id);
        wxAppSearchRepository.delete(id);
    }

    /**
     * Search for the wxApp corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WxAppDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WxApps for query {}", query);
        Page<WxApp> result = wxAppSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(wxApp -> wxAppMapper.wxAppToWxAppDTO(wxApp));
    }
}
