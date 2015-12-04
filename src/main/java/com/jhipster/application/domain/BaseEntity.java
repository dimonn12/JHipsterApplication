package com.jhipster.application.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity<E extends BaseEntity<E, ID>, ID extends Serializable> implements Model<ID> {

    private static final long serialVersionUID = -884367423115557713L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    @Override
    public ID getId() {
        return this.id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return baseHashCode(5);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        @SuppressWarnings("unchecked")
        E entity = (E)o;

        if(!this.baseEquals(entity)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return baseToString();
    }

    public final E copyEntity(E entityToCopyFrom) {
        return copyEntity(entityToCopyFrom, null);
    }

    protected E copyEntity(E entityToCopyFrom, E entityToCopyTo) {
        entityToCopyTo.setId(entityToCopyFrom.getId());
        return entityToCopyTo;
    }

    protected final boolean baseEquals(E o) {
        if(this.id != null ? !this.id.equals(o.getId()) : o.getId() != null) {
            return false;
        }
        return true;
    }

    protected final int baseHashCode(final int hash) {
        int result = 7;
        result += hash + (hash * (null != this.id ? (int)(this.id.hashCode() ^ (this.id.hashCode() >>> 32)) : 0));
        return result;
    }

    protected final String baseToString(String... fieldsToExclude) {
        return ReflectionToStringBuilder.toStringExclude(this, fieldsToExclude);
    }

}
