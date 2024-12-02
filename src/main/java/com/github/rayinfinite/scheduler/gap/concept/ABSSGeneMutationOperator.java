package com.github.rayinfinite.scheduler.gap.concept;

import org.jgap.Configuration;
import org.jgap.IUniversalRateCalculator;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.MutationOperator;

public class ABSSGeneMutationOperator extends MutationOperator {

    public ABSSGeneMutationOperator() throws InvalidConfigurationException {
        super();
    }
    public ABSSGeneMutationOperator(Configuration abss_conf) throws InvalidConfigurationException {
        super(abss_conf);
    }

    public ABSSGeneMutationOperator(Configuration abss_conf, IUniversalRateCalculator abss_mutationRateCalculator) throws InvalidConfigurationException {
        super(abss_conf, abss_mutationRateCalculator);
    }

    public ABSSGeneMutationOperator(Configuration abss_conf, int abss_desiredMutationRate) throws InvalidConfigurationException {
        super(abss_conf, abss_desiredMutationRate);
    }

}