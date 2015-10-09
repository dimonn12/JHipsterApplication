package com.jhipster.application.web.rest.dto.audit;


public class CacheDTO {

    private String cacheName;

    public CacheDTO() {
    }

    public CacheDTO(String cacheName) {
        setCacheName(cacheName);
    }

    public String getCacheName() {
        return this.cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public String toString() {
        return "CacheDTO{" + "cacheName='" + this.cacheName + '\'' + '}';
    }
}
