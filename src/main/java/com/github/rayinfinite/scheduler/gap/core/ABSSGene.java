package com.github.rayinfinite.scheduler.gap.core;

import com.github.rayinfinite.scheduler.gap.util.Constant;
import lombok.Getter;
import org.jgap.*;

import java.util.StringTokenizer;

public class ABSSGene extends CompareImpl {

    @Getter
    private Object value;  //Multiple Type Support
    private int maxNumber;
    private int type;

//    private int index;

    public ABSSGene(Configuration conf, int maxNumber, int type) throws InvalidConfigurationException {
        super(conf);
        if( maxNumber < 0 )
        {
            throw new IllegalArgumentException("The maximum number of quarters must be non-negative." );
        }
        this.maxNumber = maxNumber;
        this.type = type;
    }

//    public ABSSGene(Configuration conf, int maxNumber, int type, int index) throws InvalidConfigurationException {
//        super(conf);
//        if( maxNumber < 0 )
//        {
//            throw new IllegalArgumentException("The maximum number of quarters must be non-negative." );
//        }
//        this.maxNumber = maxNumber;
//        this.type = type;
//        this.index = index;
//    }

    @Override
    protected Integer getIdentifyId() {
        return this.value.hashCode(); // 使用 hashCode() 作为唯一标识符
    }

    @Override
    public Gene newGeneInternal() {
        try {
            return new ABSSGene(this.getConfiguration(), maxNumber, type);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public void setAllele(Object abss_newValue) {
        this.value = abss_newValue;
    }

    @Override
    public Object getAllele() {
        return this.value;
    }

    @Override
    public void setToRandomValue(RandomGenerator abss_numberGenerator) {
        // If type is Integer
        // Still Need Modification!!!
        if (type == 0) {
            this.value = abss_numberGenerator.nextInt(this.maxNumber);
        } else {
            // If type is String
            this.value = "RandomStringValue";
        }
    }

    @Override
    public String getPersistentRepresentation() {
        return this.type + Constant.TOKEN_SEPARATOR + this.maxNumber + Constant.TOKEN_SEPARATOR + this.value.toString();
    }

    @Override
    public void setValueFromPersistentRepresentation(String abss_representation) throws UnsupportedRepresentationException {
        StringTokenizer tokenizer = new StringTokenizer(abss_representation, Constant.TOKEN_SEPARATOR);
        if (tokenizer.countTokens() != 3) {
            throw new UnsupportedRepresentationException("Unknown representation format: Three tokens expected!");
        }
        try {
            this.type = Integer.parseInt(tokenizer.nextToken());
            this.maxNumber = Integer.parseInt(tokenizer.nextToken());
            String valueStr = tokenizer.nextToken();

            // set value according to type
            if (this.type == 0) {
                this.value = Integer.parseInt(valueStr); // Integer
            } else {
                this.value = valueStr; // String
            }
        } catch (ClassCastException e) {
            throw new UnsupportedRepresentationException("Unknown representation format: Expecting correct types!");
        }
    }

    @Override
    public void applyMutation(int abss_index, double abss_percentage) {
        if (this.type == 0) {
            this.value = this.getConfiguration().getRandomGenerator().nextInt(this.maxNumber);
        } else {
            this.value = "MutatedString";
        }
    }
}
