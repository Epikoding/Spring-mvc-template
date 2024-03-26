package com.template.domain.user.service;


import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.repository.AuthorityDao;
import com.template.global.common.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.template.global.common.enums.CacheType.CacheTimeConfig.CACHE_300_SECOND;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityDao authorityDao;

    @Cacheable(value = CACHE_300_SECOND, keyGenerator = "customKeyGenerator")
    public AuthorityEntity getAuthorityEntityByRole(Role role) {
        return authorityDao.findAuthorityEntityByRole(role);
    }

}
