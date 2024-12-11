package com.github.rayinfinite.scheduler.gap.concept;

import com.github.rayinfinite.scheduler.entity.Classroom;
import org.apache.commons.lang3.StringUtils;

import com.github.rayinfinite.scheduler.gap.core.*;
import com.github.rayinfinite.scheduler.gap.entity.*;
import com.github.rayinfinite.scheduler.gap.util.Constant;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import java.util.*;

/**
 * Avoiding premature convergence: Fitness training
 * `f`: current fitness value
 * `fmax`: population maximum fitness value
 * `favg`: population average fitness value
 *
 * FitnessFunction: F = (fmax - f) / (fmax - favg) * f
 */

public class ABSSFitnessFunction extends FitnessFunction {
    private int geneSize;
    public ABSSFitnessFunction(int geneSize) {
        this.geneSize = geneSize;
    }

    /** 解组染色体 */
    private ABSSSuperGene[] extractChromosome(IChromosome a_subject , int chromosomeSize) {
        ABSSSuperGene[] arr = new ABSSSuperGene[chromosomeSize];
        for(int i = 0; i < chromosomeSize; i++){
            arr[i] = (ABSSSuperGene)a_subject.getGene(i);
        }
        return arr;
    }

    @Override
    protected double evaluate(IChromosome a_subject) {

        /** 惩罚点 */
        double penalty = 0;
        ABSSGene classroomGene;
        ABSSGene timeGene;
        ABSSGene teacher1Gene;
        ABSSGene teacher2Gene;
        ABSSGene teacher3Gene;

        TeachingPlan plan;
        Classroom classroom;
        Teacher1 teacher1;
        Teacher2 teacher2;
        Teacher3 teacher3;
        Time time;

        // Extract supergenes from chromosome
        ABSSSuperGene[] s = this.extractChromosome(a_subject, this.geneSize);

        Map<String, Integer> map = new HashMap<>();
        List<Curriculum> curriculumList = new ArrayList<>();
//        List<TeachingPlan> teachingPlanList = new ArrayList<>();

        // -----------------------Checking hard constraints---------------------------
        for (int i = 0; i < this.geneSize; i++) {
            classroomGene = (ABSSGene) s[i].geneAt(Constant.CLASSROOM);
            timeGene = (ABSSGene) s[i].geneAt(Constant.TIME);
            teacher1Gene = (ABSSGene) s[i].geneAt(Constant.TEACHER1);
            teacher2Gene = (ABSSGene) s[i].geneAt(Constant.TEACHER2);
            teacher3Gene = (ABSSGene) s[i].geneAt(Constant.TEACHER3);

            plan = Constant.PLAN_LIST.get(i);
            classroom = Constant.classroomMap.get(classroomGene.getValue());
            time = Constant.timeMap.get(timeGene.getValue());
            teacher1 = Constant.teacher1Map.get(teacher1Gene.getValue());
            teacher2 = Constant.teacher2Map.get(teacher2Gene.getValue());
            teacher3 = Constant.teacher3Map.get(teacher3Gene.getValue());

            Date courseDate = time.getCourseDate();
            int weekOrder = time.getWeekOrder();

            //Classroom size & class size
            if (classroom.getClassSize() < plan.getClassSize()) {
                penalty += 1000;
            }
            //Check Software for Classroom
            if (!classroom.getSoftware().equals(plan.getSoftware())) {
                penalty += 1000;
            }

            // 构造 map，用于后面检测冲突
            String cohortKey = "cohort:" + weekOrder + ":" + plan.getCohort();
            Integer cohortValue = map.get(cohortKey);
            if (cohortValue != null) {
                map.put(cohortKey, cohortValue + 1);
            } else {
                map.put(cohortKey, 0);
            }

            String classroomKey = "classroom:" + weekOrder + ":" + classroom.getId();
            Integer classroomValue = map.get(classroomKey);
            if (classroomValue != null) {
                map.put(classroomKey, classroomValue + 1);
            } else {
                map.put(classroomKey, 0);
            }

            String teacher1Key = "teacher1:" + weekOrder + ":" + plan.getTeacher1();
            Integer teacher1Value = map.get(teacher1Key);
            if (teacher1Value != null) {
                map.put(teacher1Key, teacher1Value + 1);
            } else {
                map.put(teacher1Key, 0);
            }

            String teacher2Key = "teacher2:" + weekOrder + ":" + plan.getTeacher2();
            Integer teacher2Value = map.get(teacher2Key);
            if (teacher2Value != null) {
                map.put(teacher2Key, teacher2Value + 1);
            } else {
                map.put(teacher2Key, 0);
            }

            String teacher3Key = "teacher3:" + weekOrder + ":" + plan.getTeacher3();
            Integer teacher3Value = map.get(teacher3Key);
            if (teacher3Value != null) {
                map.put(teacher3Key, teacher3Value + 1);
            } else {
                map.put(teacher3Key, 0);
            }

            //Mock for conflict detection
            curriculumList.add(new Curriculum()
                    .setCohort(plan.getCohort())
                    .setCourseCode(plan.getCourseCode())
                    .setCourseName(plan.getCourseName())
                    .setTeacher1(plan.getTeacher1())
                    .setTeacher2(plan.getTeacher2())
                    .setTeacher3(plan.getTeacher3())
                    .setClassId(classroom.getId())
                    .setWeekOrder(time.getWeekOrder())
                    .setCourseDate(time.getCourseDate())
                    .setDuration(time.getDuration())
            );
        }


        //-----detect time conflict
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            //same class/teacher/classroom in same time?
            if (entry.getValue() != 0) {
                penalty += 1000;
            }
        }

            return 1 / (1 + penalty);
        }

}

