package com.github.rayinfinite.scheduler.gap.core;

import com.github.rayinfinite.scheduler.gap.util.Constant;
import lombok.Getter;
import org.jgap.*;

import java.util.StringTokenizer;

public class ABSSGene extends CompareImpl {

    @Getter
    private String value = "";
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
        return this.value != null ? this.value.hashCode() : 0; // 使用 String 的 hashCode 作为唯一标识符
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
        if (abss_newValue instanceof String) {
            this.value = (String) abss_newValue; // 确保传入的值是 String 类型
        } else {
            throw new IllegalArgumentException("Expected a String value for the gene.");
        }
    }

    @Override
    public Object getAllele() {
        return this.value;
    }

    @Override
    public void setToRandomValue(RandomGenerator abss_numberGenerator) {
        // If type is Integer
        // Still Need Modification!!!
        if (this.type == 0) {

            this.value = String.valueOf(abss_numberGenerator.nextInt(maxNumber));
        } else {
            // 其他类型的基因，生成随机字符串或从预定义集合中选择
            this.value = generateRandomStringValue();  // 示例：生成随机字符串
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
            this.value = tokenizer.nextToken();
        } catch (NumberFormatException e) {
            throw new UnsupportedRepresentationException("Unknown representation format: Expecting correct types!");
        }
    }

    @Override
    public void applyMutation(int abss_index, double abss_percentage) {
        // Still Need Modification!!!

        if (this.type == 0) {
            // 如果是类型 0，生成随机整数并转换为 String
            this.value = String.valueOf(this.getConfiguration().getRandomGenerator().nextInt(maxNumber));
        } else {
            // 其他类型的基因，生成随机字符串或从预定义集合中选择
            this.value = generateMutatedStringValue();
        }
    }
    private String generateRandomStringValue() {
        String[] possibleValues = {"ClassroomA", "ClassroomB", "ClassroomC", "TimeSlot1", "TimeSlot2"};
        return possibleValues[this.getConfiguration().getRandomGenerator().nextInt(possibleValues.length)];
    }


    private String generateMutatedStringValue() {
        String[] possibleValues = {"MutatedClassroomA", "MutatedClassroomB", "MutatedClassroomC", "MutatedTimeSlot1", "MutatedTimeSlot2"};
        return possibleValues[this.getConfiguration().getRandomGenerator().nextInt(possibleValues.length)];
    }
}
