package com.drivinglicence.myapp.service.impl;

import com.drivinglicence.myapp.domain.UserApp;
import com.drivinglicence.myapp.repository.UserAppRepository;
import com.drivinglicence.myapp.service.UserAppService;
import com.drivinglicence.myapp.service.dto.UserAppDTO;
import com.drivinglicence.myapp.service.mapper.UserAppMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserApp}.
 */
@Service
@Transactional
public class UserAppServiceImpl implements UserAppService {

    private final Logger log = LoggerFactory.getLogger(UserAppServiceImpl.class);

    private final UserAppRepository userAppRepository;

    private final UserAppMapper userAppMapper;

    public UserAppServiceImpl(UserAppRepository userAppRepository, UserAppMapper userAppMapper) {
        this.userAppRepository = userAppRepository;
        this.userAppMapper = userAppMapper;
    }

    @Override
    public UserAppDTO save(UserAppDTO userAppDTO) {
        log.debug("Request to save UserApp : {}", userAppDTO);
        UserApp userApp = userAppMapper.toEntity(userAppDTO);
        userApp = userAppRepository.save(userApp);
        return userAppMapper.toDto(userApp);
    }

    @Override
    public UserAppDTO update(UserAppDTO userAppDTO) {
        log.debug("Request to save UserApp : {}", userAppDTO);
        UserApp userApp = userAppMapper.toEntity(userAppDTO);
        userApp = userAppRepository.save(userApp);
        return userAppMapper.toDto(userApp);
    }

    @Override
    public Optional<UserAppDTO> partialUpdate(UserAppDTO userAppDTO) {
        log.debug("Request to partially update UserApp : {}", userAppDTO);

        return userAppRepository
            .findById(userAppDTO.getId())
            .map(existingUserApp -> {
                userAppMapper.partialUpdate(existingUserApp, userAppDTO);

                return existingUserApp;
            })
            .map(userAppRepository::save)
            .map(userAppMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAppDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserApps");
        return userAppRepository.findAll(pageable).map(userAppMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAppDTO> findOne(Long id) {
        log.debug("Request to get UserApp : {}", id);
        return userAppRepository.findById(id).map(userAppMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserApp : {}", id);
        userAppRepository.deleteById(id);
    }
}
