package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AppnavApp;

import com.mycompany.myapp.domain.WxApp;
import com.mycompany.myapp.repository.WxAppRepository;
import com.mycompany.myapp.service.WxAppService;
import com.mycompany.myapp.repository.search.WxAppSearchRepository;
import com.mycompany.myapp.service.dto.WxAppDTO;
import com.mycompany.myapp.service.mapper.WxAppMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WxAppResource REST controller.
 *
 * @see WxAppResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppnavApp.class)
public class WxAppResourceIntTest {

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PUBLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLICATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SCREENSHOT = "AAAAAAAAAA";
    private static final String UPDATED_SCREENSHOT = "BBBBBBBBBB";

    private static final String DEFAULT_QCODE = "AAAAAAAAAA";
    private static final String UPDATED_QCODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SCORE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SCORE = new BigDecimal(2);

    private static final Long DEFAULT_VIEWS = 1L;
    private static final Long UPDATED_VIEWS = 2L;

    @Autowired
    private WxAppRepository wxAppRepository;

    @Autowired
    private WxAppMapper wxAppMapper;

    @Autowired
    private WxAppService wxAppService;

    @Autowired
    private WxAppSearchRepository wxAppSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWxAppMockMvc;

    private WxApp wxApp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WxAppResource wxAppResource = new WxAppResource(wxAppService);
        this.restWxAppMockMvc = MockMvcBuilders.standaloneSetup(wxAppResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WxApp createEntity(EntityManager em) {
        WxApp wxApp = new WxApp()
                .icon(DEFAULT_ICON)
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .publicationDate(DEFAULT_PUBLICATION_DATE)
                .screenshot(DEFAULT_SCREENSHOT)
                .qcode(DEFAULT_QCODE)
                .score(DEFAULT_SCORE)
                .views(DEFAULT_VIEWS);
        return wxApp;
    }

    @Before
    public void initTest() {
        wxAppSearchRepository.deleteAll();
        wxApp = createEntity(em);
    }

    @Test
    @Transactional
    public void createWxApp() throws Exception {
        int databaseSizeBeforeCreate = wxAppRepository.findAll().size();

        // Create the WxApp
        WxAppDTO wxAppDTO = wxAppMapper.wxAppToWxAppDTO(wxApp);

        restWxAppMockMvc.perform(post("/api/wx-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxAppDTO)))
            .andExpect(status().isCreated());

        // Validate the WxApp in the database
        List<WxApp> wxAppList = wxAppRepository.findAll();
        assertThat(wxAppList).hasSize(databaseSizeBeforeCreate + 1);
        WxApp testWxApp = wxAppList.get(wxAppList.size() - 1);
        assertThat(testWxApp.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testWxApp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWxApp.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWxApp.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
        assertThat(testWxApp.getScreenshot()).isEqualTo(DEFAULT_SCREENSHOT);
        assertThat(testWxApp.getQcode()).isEqualTo(DEFAULT_QCODE);
        assertThat(testWxApp.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testWxApp.getViews()).isEqualTo(DEFAULT_VIEWS);

        // Validate the WxApp in Elasticsearch
        WxApp wxAppEs = wxAppSearchRepository.findOne(testWxApp.getId());
        assertThat(wxAppEs).isEqualToComparingFieldByField(testWxApp);
    }

    @Test
    @Transactional
    public void createWxAppWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wxAppRepository.findAll().size();

        // Create the WxApp with an existing ID
        WxApp existingWxApp = new WxApp();
        existingWxApp.setId(1L);
        WxAppDTO existingWxAppDTO = wxAppMapper.wxAppToWxAppDTO(existingWxApp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWxAppMockMvc.perform(post("/api/wx-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingWxAppDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WxApp> wxAppList = wxAppRepository.findAll();
        assertThat(wxAppList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wxAppRepository.findAll().size();
        // set the field null
        wxApp.setName(null);

        // Create the WxApp, which fails.
        WxAppDTO wxAppDTO = wxAppMapper.wxAppToWxAppDTO(wxApp);

        restWxAppMockMvc.perform(post("/api/wx-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxAppDTO)))
            .andExpect(status().isBadRequest());

        List<WxApp> wxAppList = wxAppRepository.findAll();
        assertThat(wxAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = wxAppRepository.findAll().size();
        // set the field null
        wxApp.setQcode(null);

        // Create the WxApp, which fails.
        WxAppDTO wxAppDTO = wxAppMapper.wxAppToWxAppDTO(wxApp);

        restWxAppMockMvc.perform(post("/api/wx-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxAppDTO)))
            .andExpect(status().isBadRequest());

        List<WxApp> wxAppList = wxAppRepository.findAll();
        assertThat(wxAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWxApps() throws Exception {
        // Initialize the database
        wxAppRepository.saveAndFlush(wxApp);

        // Get all the wxAppList
        restWxAppMockMvc.perform(get("/api/wx-apps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wxApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].screenshot").value(hasItem(DEFAULT_SCREENSHOT.toString())))
            .andExpect(jsonPath("$.[*].qcode").value(hasItem(DEFAULT_QCODE.toString())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.intValue())))
            .andExpect(jsonPath("$.[*].views").value(hasItem(DEFAULT_VIEWS.intValue())));
    }

    @Test
    @Transactional
    public void getWxApp() throws Exception {
        // Initialize the database
        wxAppRepository.saveAndFlush(wxApp);

        // Get the wxApp
        restWxAppMockMvc.perform(get("/api/wx-apps/{id}", wxApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wxApp.getId().intValue()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()))
            .andExpect(jsonPath("$.screenshot").value(DEFAULT_SCREENSHOT.toString()))
            .andExpect(jsonPath("$.qcode").value(DEFAULT_QCODE.toString()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.intValue()))
            .andExpect(jsonPath("$.views").value(DEFAULT_VIEWS.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWxApp() throws Exception {
        // Get the wxApp
        restWxAppMockMvc.perform(get("/api/wx-apps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWxApp() throws Exception {
        // Initialize the database
        wxAppRepository.saveAndFlush(wxApp);
        wxAppSearchRepository.save(wxApp);
        int databaseSizeBeforeUpdate = wxAppRepository.findAll().size();

        // Update the wxApp
        WxApp updatedWxApp = wxAppRepository.findOne(wxApp.getId());
        updatedWxApp
                .icon(UPDATED_ICON)
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .publicationDate(UPDATED_PUBLICATION_DATE)
                .screenshot(UPDATED_SCREENSHOT)
                .qcode(UPDATED_QCODE)
                .score(UPDATED_SCORE)
                .views(UPDATED_VIEWS);
        WxAppDTO wxAppDTO = wxAppMapper.wxAppToWxAppDTO(updatedWxApp);

        restWxAppMockMvc.perform(put("/api/wx-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxAppDTO)))
            .andExpect(status().isOk());

        // Validate the WxApp in the database
        List<WxApp> wxAppList = wxAppRepository.findAll();
        assertThat(wxAppList).hasSize(databaseSizeBeforeUpdate);
        WxApp testWxApp = wxAppList.get(wxAppList.size() - 1);
        assertThat(testWxApp.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testWxApp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWxApp.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWxApp.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
        assertThat(testWxApp.getScreenshot()).isEqualTo(UPDATED_SCREENSHOT);
        assertThat(testWxApp.getQcode()).isEqualTo(UPDATED_QCODE);
        assertThat(testWxApp.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testWxApp.getViews()).isEqualTo(UPDATED_VIEWS);

        // Validate the WxApp in Elasticsearch
        WxApp wxAppEs = wxAppSearchRepository.findOne(testWxApp.getId());
        assertThat(wxAppEs).isEqualToComparingFieldByField(testWxApp);
    }

    @Test
    @Transactional
    public void updateNonExistingWxApp() throws Exception {
        int databaseSizeBeforeUpdate = wxAppRepository.findAll().size();

        // Create the WxApp
        WxAppDTO wxAppDTO = wxAppMapper.wxAppToWxAppDTO(wxApp);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWxAppMockMvc.perform(put("/api/wx-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxAppDTO)))
            .andExpect(status().isCreated());

        // Validate the WxApp in the database
        List<WxApp> wxAppList = wxAppRepository.findAll();
        assertThat(wxAppList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWxApp() throws Exception {
        // Initialize the database
        wxAppRepository.saveAndFlush(wxApp);
        wxAppSearchRepository.save(wxApp);
        int databaseSizeBeforeDelete = wxAppRepository.findAll().size();

        // Get the wxApp
        restWxAppMockMvc.perform(delete("/api/wx-apps/{id}", wxApp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean wxAppExistsInEs = wxAppSearchRepository.exists(wxApp.getId());
        assertThat(wxAppExistsInEs).isFalse();

        // Validate the database is empty
        List<WxApp> wxAppList = wxAppRepository.findAll();
        assertThat(wxAppList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWxApp() throws Exception {
        // Initialize the database
        wxAppRepository.saveAndFlush(wxApp);
        wxAppSearchRepository.save(wxApp);

        // Search the wxApp
        restWxAppMockMvc.perform(get("/api/_search/wx-apps?query=id:" + wxApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wxApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].screenshot").value(hasItem(DEFAULT_SCREENSHOT.toString())))
            .andExpect(jsonPath("$.[*].qcode").value(hasItem(DEFAULT_QCODE.toString())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.intValue())))
            .andExpect(jsonPath("$.[*].views").value(hasItem(DEFAULT_VIEWS.intValue())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WxApp.class);
    }
}
