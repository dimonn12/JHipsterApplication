package com.jhipster.application.service.security;

import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.security.PersistentToken;
import com.jhipster.application.domain.security.User;
import com.jhipster.application.repository.security.PersistentTokenRepository;
import com.jhipster.application.service.AbstractService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by dimonn12 on 13.10.2015.
 */
@Service
public class PersistentTokenService extends AbstractService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PersistentTokenService.class);

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    public List<PersistentToken> findByUser(User user) {
        List<PersistentToken> result = persistentTokenRepository.findByUser(user);
        if(null == result) {
            addError(ErrorStatusCode.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    public void delete(String series) {
        persistentTokenRepository.delete(series);
    }

}
