package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.service.WxAppService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.WxAppDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing WxApp.
 */
@RestController
@RequestMapping("/api")
public class WxAppResource {

    private final Logger log = LoggerFactory.getLogger(WxAppResource.class);

    private static final String ENTITY_NAME = "wxApp";
        
    private final WxAppService wxAppService;

    public WxAppResource(WxAppService wxAppService) {
        this.wxAppService = wxAppService;
    }

    /**
     * POST  /wx-apps : Create a new wxApp.
     *
     * @param wxAppDTO the wxAppDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wxAppDTO, or with status 400 (Bad Request) if the wxApp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wx-apps")
    @Timed
    public ResponseEntity<WxAppDTO> createWxApp(@Valid @RequestBody WxAppDTO wxAppDTO) throws URISyntaxException {
        log.debug("REST request to save WxApp : {}", wxAppDTO);
        if (wxAppDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wxApp cannot already have an ID")).body(null);
        }
        WxAppDTO result = wxAppService.save(wxAppDTO);
        return ResponseEntity.created(new URI("/api/wx-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wx-apps : Updates an existing wxApp.
     *
     * @param wxAppDTO the wxAppDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wxAppDTO,
     * or with status 400 (Bad Request) if the wxAppDTO is not valid,
     * or with status 500 (Internal Server Error) if the wxAppDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wx-apps")
    @Timed
    public ResponseEntity<WxAppDTO> updateWxApp(@Valid @RequestBody WxAppDTO wxAppDTO) throws URISyntaxException {
        log.debug("REST request to update WxApp : {}", wxAppDTO);
        if (wxAppDTO.getId() == null) {
            return createWxApp(wxAppDTO);
        }
        WxAppDTO result = wxAppService.save(wxAppDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wxAppDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wx-apps : get all the wxApps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wxApps in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/wx-apps")
    @Timed
    public ResponseEntity<List<WxAppDTO>> getAllWxApps(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WxApps");
        Page<WxAppDTO> page = wxAppService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wx-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wx-apps/:id : get the "id" wxApp.
     *
     * @param id the id of the wxAppDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wxAppDTO, or with status 404 (Not Found)
     */
    @GetMapping("/wx-apps/{id}")
    @Timed
    public ResponseEntity<WxAppDTO> getWxApp(@PathVariable Long id) {
        log.debug("REST request to get WxApp : {}", id);
        WxAppDTO wxAppDTO = wxAppService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wxAppDTO));
    }

    /**
     * DELETE  /wx-apps/:id : delete the "id" wxApp.
     *
     * @param id the id of the wxAppDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wx-apps/{id}")
    @Timed
    public ResponseEntity<Void> deleteWxApp(@PathVariable Long id) {
        log.debug("REST request to delete WxApp : {}", id);
        wxAppService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/wx-apps?query=:query : search for the wxApp corresponding
     * to the query.
     *
     * @param query the query of the wxApp search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/wx-apps")
    @Timed
    public ResponseEntity<List<WxAppDTO>> searchWxApps(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WxApps for query {}", query);
        Page<WxAppDTO> page = wxAppService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/wx-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
