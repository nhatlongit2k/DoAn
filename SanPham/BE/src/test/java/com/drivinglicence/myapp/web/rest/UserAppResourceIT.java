package com.drivinglicence.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.drivinglicence.myapp.IntegrationTest;
import com.drivinglicence.myapp.domain.UserApp;
import com.drivinglicence.myapp.repository.UserAppRepository;
import com.drivinglicence.myapp.service.dto.UserAppDTO;
import com.drivinglicence.myapp.service.mapper.UserAppMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserAppResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAppResourceIT {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR = "BBBBBBBBBB";

    private static final Long DEFAULT_PERMISSION = 1L;
    private static final Long UPDATED_PERMISSION = 2L;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/user-apps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAppRepository userAppRepository;

    @Autowired
    private UserAppMapper userAppMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAppMockMvc;

    private UserApp userApp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserApp createEntity(EntityManager em) {
        UserApp userApp = new UserApp()
            .userName(DEFAULT_USER_NAME)
            .password(DEFAULT_PASSWORD)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .name(DEFAULT_NAME)
            .avatar(DEFAULT_AVATAR)
            .permission(DEFAULT_PERMISSION)
            .isActive(DEFAULT_IS_ACTIVE);
        return userApp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserApp createUpdatedEntity(EntityManager em) {
        UserApp userApp = new UserApp()
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .name(UPDATED_NAME)
            .avatar(UPDATED_AVATAR)
            .permission(UPDATED_PERMISSION)
            .isActive(UPDATED_IS_ACTIVE);
        return userApp;
    }

    @BeforeEach
    public void initTest() {
        userApp = createEntity(em);
    }

    @Test
    @Transactional
    void createUserApp() throws Exception {
        int databaseSizeBeforeCreate = userAppRepository.findAll().size();
        // Create the UserApp
        UserAppDTO userAppDTO = userAppMapper.toDto(userApp);
        restUserAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAppDTO)))
            .andExpect(status().isCreated());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeCreate + 1);
        UserApp testUserApp = userAppList.get(userAppList.size() - 1);
        assertThat(testUserApp.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testUserApp.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUserApp.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserApp.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testUserApp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserApp.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testUserApp.getPermission()).isEqualTo(DEFAULT_PERMISSION);
        assertThat(testUserApp.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createUserAppWithExistingId() throws Exception {
        // Create the UserApp with an existing ID
        userApp.setId(1L);
        UserAppDTO userAppDTO = userAppMapper.toDto(userApp);

        int databaseSizeBeforeCreate = userAppRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAppDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserApps() throws Exception {
        // Initialize the database
        userAppRepository.saveAndFlush(userApp);

        // Get all the userAppList
        restUserAppMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getUserApp() throws Exception {
        // Initialize the database
        userAppRepository.saveAndFlush(userApp);

        // Get the userApp
        restUserAppMockMvc
            .perform(get(ENTITY_API_URL_ID, userApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userApp.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.avatar").value(DEFAULT_AVATAR))
            .andExpect(jsonPath("$.permission").value(DEFAULT_PERMISSION.intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserApp() throws Exception {
        // Get the userApp
        restUserAppMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserApp() throws Exception {
        // Initialize the database
        userAppRepository.saveAndFlush(userApp);

        int databaseSizeBeforeUpdate = userAppRepository.findAll().size();

        // Update the userApp
        UserApp updatedUserApp = userAppRepository.findById(userApp.getId()).get();
        // Disconnect from session so that the updates on updatedUserApp are not directly saved in db
        em.detach(updatedUserApp);
        updatedUserApp
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .name(UPDATED_NAME)
            .avatar(UPDATED_AVATAR)
            .permission(UPDATED_PERMISSION)
            .isActive(UPDATED_IS_ACTIVE);
        UserAppDTO userAppDTO = userAppMapper.toDto(updatedUserApp);

        restUserAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAppDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAppDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeUpdate);
        UserApp testUserApp = userAppList.get(userAppList.size() - 1);
        assertThat(testUserApp.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testUserApp.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserApp.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserApp.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUserApp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserApp.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testUserApp.getPermission()).isEqualTo(UPDATED_PERMISSION);
        assertThat(testUserApp.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingUserApp() throws Exception {
        int databaseSizeBeforeUpdate = userAppRepository.findAll().size();
        userApp.setId(count.incrementAndGet());

        // Create the UserApp
        UserAppDTO userAppDTO = userAppMapper.toDto(userApp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAppDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAppDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserApp() throws Exception {
        int databaseSizeBeforeUpdate = userAppRepository.findAll().size();
        userApp.setId(count.incrementAndGet());

        // Create the UserApp
        UserAppDTO userAppDTO = userAppMapper.toDto(userApp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAppDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserApp() throws Exception {
        int databaseSizeBeforeUpdate = userAppRepository.findAll().size();
        userApp.setId(count.incrementAndGet());

        // Create the UserApp
        UserAppDTO userAppDTO = userAppMapper.toDto(userApp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAppMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAppDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAppWithPatch() throws Exception {
        // Initialize the database
        userAppRepository.saveAndFlush(userApp);

        int databaseSizeBeforeUpdate = userAppRepository.findAll().size();

        // Update the userApp using partial update
        UserApp partialUpdatedUserApp = new UserApp();
        partialUpdatedUserApp.setId(userApp.getId());

        partialUpdatedUserApp
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .name(UPDATED_NAME)
            .avatar(UPDATED_AVATAR)
            .permission(UPDATED_PERMISSION)
            .isActive(UPDATED_IS_ACTIVE);

        restUserAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserApp))
            )
            .andExpect(status().isOk());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeUpdate);
        UserApp testUserApp = userAppList.get(userAppList.size() - 1);
        assertThat(testUserApp.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testUserApp.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUserApp.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserApp.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUserApp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserApp.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testUserApp.getPermission()).isEqualTo(UPDATED_PERMISSION);
        assertThat(testUserApp.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateUserAppWithPatch() throws Exception {
        // Initialize the database
        userAppRepository.saveAndFlush(userApp);

        int databaseSizeBeforeUpdate = userAppRepository.findAll().size();

        // Update the userApp using partial update
        UserApp partialUpdatedUserApp = new UserApp();
        partialUpdatedUserApp.setId(userApp.getId());

        partialUpdatedUserApp
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .name(UPDATED_NAME)
            .avatar(UPDATED_AVATAR)
            .permission(UPDATED_PERMISSION)
            .isActive(UPDATED_IS_ACTIVE);

        restUserAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserApp))
            )
            .andExpect(status().isOk());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeUpdate);
        UserApp testUserApp = userAppList.get(userAppList.size() - 1);
        assertThat(testUserApp.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testUserApp.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserApp.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserApp.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUserApp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserApp.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testUserApp.getPermission()).isEqualTo(UPDATED_PERMISSION);
        assertThat(testUserApp.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingUserApp() throws Exception {
        int databaseSizeBeforeUpdate = userAppRepository.findAll().size();
        userApp.setId(count.incrementAndGet());

        // Create the UserApp
        UserAppDTO userAppDTO = userAppMapper.toDto(userApp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAppDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAppDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserApp() throws Exception {
        int databaseSizeBeforeUpdate = userAppRepository.findAll().size();
        userApp.setId(count.incrementAndGet());

        // Create the UserApp
        UserAppDTO userAppDTO = userAppMapper.toDto(userApp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAppDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserApp() throws Exception {
        int databaseSizeBeforeUpdate = userAppRepository.findAll().size();
        userApp.setId(count.incrementAndGet());

        // Create the UserApp
        UserAppDTO userAppDTO = userAppMapper.toDto(userApp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAppMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userAppDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserApp in the database
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserApp() throws Exception {
        // Initialize the database
        userAppRepository.saveAndFlush(userApp);

        int databaseSizeBeforeDelete = userAppRepository.findAll().size();

        // Delete the userApp
        restUserAppMockMvc
            .perform(delete(ENTITY_API_URL_ID, userApp.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserApp> userAppList = userAppRepository.findAll();
        assertThat(userAppList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
