package com.jhipster.application.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class SoftDeletedEntity<E extends SoftDeletedEntity<E, ID>, ID extends Serializable>
    extends AbstractAuditingEntity<E, ID> {

    private static final long serialVersionUID = -335144964285137631L;

    @Column(nullable = false)
    private boolean deleted = false;

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    protected E copyEntity(E entityToCopyFrom, E entityToCopyTo) {
        entityToCopyTo = super.copyEntity(entityToCopyFrom, entityToCopyTo);
        entityToCopyTo.setDeleted(entityToCopyFrom.getDeleted());
        return entityToCopyTo;
    }
}
