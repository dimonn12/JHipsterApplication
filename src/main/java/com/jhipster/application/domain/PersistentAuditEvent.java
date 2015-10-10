package com.jhipster.application.domain;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Persist AuditEvent managed by the Spring Boot actuator
 *
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */
@Entity
@Table(name = "jhi_persistent_audit_event")
public class PersistentAuditEvent extends BaseEntity<PersistentAuditEvent, Long> {

    @NotNull
    @Column(nullable = false)
    private String principal;

    @Column(name = "event_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime auditEventDate;

    @Column(name = "event_type")
    private String auditEventType;

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @CollectionTable(name = "jhi_persistent_audit_evt_data", joinColumns = @JoinColumn(name = "id"))
    private Map<String, String> data = new HashMap<>();

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public LocalDateTime getAuditEventDate() {
        return auditEventDate;
    }

    public void setAuditEventDate(LocalDateTime auditEventDate) {
        this.auditEventDate = auditEventDate;
    }

    public String getAuditEventType() {
        return auditEventType;
    }

    public void setAuditEventType(String auditEventType) {
        this.auditEventType = auditEventType;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    protected PersistentAuditEvent copyEntity(PersistentAuditEvent entityToCopyFrom,
                                              PersistentAuditEvent entityToCopyTo) {
        if(null == entityToCopyTo) {
            entityToCopyTo = new PersistentAuditEvent();
        }
        entityToCopyTo = super.copyEntity(entityToCopyFrom, entityToCopyTo);
        entityToCopyTo.setAuditEventDate(entityToCopyFrom.getAuditEventDate());
        entityToCopyTo.setAuditEventType(entityToCopyFrom.getAuditEventType());
        entityToCopyTo.setData(entityToCopyFrom.getData());
        entityToCopyTo.setPrincipal(entityToCopyFrom.getPrincipal());
        return entityToCopyTo;
    }

    @Override
    public int hashCode() {
        return baseHashCode(23);
    }
}
