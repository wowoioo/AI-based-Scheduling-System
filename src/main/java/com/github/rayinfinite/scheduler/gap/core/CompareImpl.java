package com.github.rayinfinite.scheduler.gap.core;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.IntegerGene;

public abstract class CompareImpl extends IntegerGene implements Gene {

    public CompareImpl(Configuration a_config) throws InvalidConfigurationException {
        super(a_config);
    }

    protected abstract Integer getIdentifyId();

    @Override
    public int compareTo(Object abss_otherGroupGene) {
        if (abss_otherGroupGene == null) {
            return 1;
        }
        if (getIdentifyId() == null) {
            if (((CompareImpl) abss_otherGroupGene).getIdentifyId() == null) {
                return 0;
            }else {
                return -1;
            }
        }
        return getIdentifyId().compareTo(((CompareImpl) abss_otherGroupGene).getIdentifyId());
    }

    @Override
    public boolean equals(Object abss_otherGroupGene) {
        return abss_otherGroupGene instanceof CompareImpl && compareTo(abss_otherGroupGene) == 0;
    }

    @Override
    public int hashCode() {
        return getIdentifyId();
    }

    @Override
    public Object getInternalValue() {
        return getIdentifyId();
    }
}
