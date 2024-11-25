package com.github.rayinfinite.scheduler.gap.concept;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;
import org.jgap.supergenes.AbstractSupergene;
import org.jgap.supergenes.Supergene;


public class ABSSSuperGene extends AbstractSupergene {

    public ABSSSuperGene(Configuration conf) throws InvalidConfigurationException {
        super(conf);
    }

    public ABSSSuperGene(Configuration conf, Gene[] genes) throws InvalidConfigurationException {
        super(conf, genes);
    }

    @Override
    public boolean isValid(Gene[] genes, Supergene supergene) {

        return true;
    }

    @Override
    public String getPersistentRepresentation() throws UnsupportedOperationException {
        StringBuilder sb = new StringBuilder();
        for (Gene gene : getGenes()) {
            sb.append(gene.getPersistentRepresentation()).append("+");
        }
        return sb.toString();
    }
}
