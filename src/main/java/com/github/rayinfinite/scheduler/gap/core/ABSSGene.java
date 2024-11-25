package com.github.rayinfinite.scheduler.gap.core;

import com.github.rayinfinite.scheduler.gap.util.Constant;
import lombok.Getter;
import org.jgap.*;

import java.util.StringTokenizer;

public class ABSSGene extends CompareImpl {

    @Getter
    private Integer number = 0;
    private int maxNumber;
    private int type;

    public ABSSGene(Configuration conf, int maxNumber, int type) throws InvalidConfigurationException {
        super(conf);
        if( maxNumber < 0 )
        {
            throw new IllegalArgumentException("The maximum number of quarters must be non-negative." );
        }
        this.maxNumber = maxNumber;
        this.type = type;
    }

    @Override
    protected Integer getIdentifyId() {
        return this.number;
    }

    @Override
    public Gene newGeneInternal() {
        try {
            return new ABSSGene(this.getConfiguration(), maxNumber,type);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public void setAllele(Object a_newValue){
        this.number =  Integer.parseInt(a_newValue.toString());
    }

    @Override
    public Object getAllele() {
        return this.number;
    }

    @Override
    public void setToRandomValue(RandomGenerator a_numberGenerator) {
        this.number = a_numberGenerator.nextInt(this.maxNumber);
    }

    @Override
    public String getPersistentRepresentation() {
        return "" + this.type + Constant.TOKEN_SEPARATOR + this.maxNumber + Constant.TOKEN_SEPARATOR + this.number;
    }

    @Override
    public void setValueFromPersistentRepresentation(String a_representation) throws UnsupportedRepresentationException {
        StringTokenizer tokenizer = new StringTokenizer(a_representation, Constant.TOKEN_SEPARATOR);
        if(tokenizer.countTokens() != 3) {
            throw new UnsupportedRepresentationException("Unknown representation format: Two tokens expected!");
        }
        try {
            this.type = Integer.parseInt(tokenizer.nextToken());
            this.maxNumber = Integer.parseInt(tokenizer.nextToken());
            this.number = Integer.parseInt(tokenizer.nextToken());
        } catch(ClassCastException e) {
            throw new UnsupportedRepresentationException("Unknown representation format: Expecting integer values!");
        }
    }

    @Override
    public void applyMutation(int a_index, double a_percentage) {
        this.setAllele(this.getConfiguration().getRandomGenerator().nextInt(this.maxNumber));
    }

}
