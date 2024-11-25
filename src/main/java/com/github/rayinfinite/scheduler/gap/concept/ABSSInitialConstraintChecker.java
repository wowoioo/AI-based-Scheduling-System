package com.github.rayinfinite.scheduler.gap.concept;

import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.IGeneConstraintChecker;

public class ABSSInitialConstraintChecker implements IGeneConstraintChecker {
    private int i = 0;

    @Override
    public boolean verify(Gene gene, Object o, IChromosome iChromosome, int i) {
        i += 1;
        return true;
    }

}
