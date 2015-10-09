package com.jhipster.application.web.rest.audit;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.application.web.rest.dto.audit.CacheDTO;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/api")
public class CacheResource {

    private static final Logger LOG = LoggerFactory.getLogger(CacheResource.class);

    @RequestMapping(value = "/cache", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Timed
    public void clearCache(@RequestBody CacheDTO jsonCache) {
        String cacheName = jsonCache.getCacheName();
        Ehcache cache = CacheManager.getInstance().getEhcache(cacheName);
        if(null != cache) {
            LOG.debug("Removing all from cache: ".concat(cacheName));
            cache.removeAll();
            LOG.debug(cacheName.concat(" has been successfully cleared"));
        }
    }

}
