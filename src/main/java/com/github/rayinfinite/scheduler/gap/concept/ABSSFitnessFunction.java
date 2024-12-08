package com.github.rayinfinite.scheduler.gap.concept;

import cn.hutool.core.util.ArrayUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.gap.concept.*;
import com.github.rayinfinite.scheduler.gap.core.*;
import com.github.rayinfinite.scheduler.gap.entity.*;
import com.github.rayinfinite.scheduler.gap.util.Constant;

import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

import java.util.*;
import java.util.stream.Collectors;

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
        ABSSGene teacherGene;

        TeachingPlan plan;
        Classroom classroom;
        Teacher teacher;
        Time time;

        // Extract supergenes from chromosome
        ABSSSuperGene[] s = this.extractChromosome(a_subject, this.geneSize);

        Map<String,Integer> map = new HashMap<>();
        List<Curriculum> curriculumList = new ArrayList<>();

        // -----------------------Checking hard constraints---------------------------
        for (int i = 0;i < this.geneSize;i++) {
            classroomGene = (ABSSGene) s[i].geneAt(Constant.CLASSROOM);
            timeGene = (ABSSGene)s[i].geneAt(Constant.TIME);
            teacherGene = (ABSSGene)s[i].geneAt(Constant.TEACHER);

            plan = Constant.planMap.get(i);
            classroom = Constant.classroomMap.get(classroomGene.getValue());
            time = Constant.timeMap.get(timeGene.getValue());
            teacher = Constant.teacherMap.get(teacherGene.getValue());

            int weekOrder = time.getWeekOrder();
//            int duration = time.getDuration();


            //Classroom size & class size
            if (classroom.getSize() < plan.getClassSize()){
                penalty += 1000;
            }
            //Check Software for Classroom
            if (!classroom.getSoftware().equals(plan.getSoftware())){
                penalty += 1000;
            }

            // 构造 map，用于后面检测冲突
            String cohortKey = "cohort:" + weekOrder + ":" + plan.getCohort();
            Integer cohortValue = map.get(cohortKey);
            if (cohortValue != null) {
                map.put(cohortKey,cohortValue + 1);
            } else {
                map.put(cohortKey, 1);
            }

            List<String> ABSSteachers = Arrays.asList(plan.getTeacher1(), plan.getTeacher2(), plan.getTeacher3());
            for (String ABSSteacher : ABSSteachers) {
                if (StringUtils.isNotBlank(ABSSteacher)) {
                    String teacherKey = "teacher:" + weekOrder + ":" + teacher;
                    Integer teacherValue = map.get(teacherKey);
                    if (teacherValue != null) {
                        map.put(teacherKey,teacherValue + 1);
                    } else {
                        map.put(teacherKey, 1);
                    }
                }
            }

            String classroomKey = "classroom:" + weekOrder + ":" + classroom.getId();
            Integer classroomValue = map.get(classroomKey);
            if (classroomValue != null) {
                map.put(classroomKey,classroomValue + 1);
            } else {
                map.put(classroomKey, 1);
            }

            //构造临时数据，用于后面检测冲突
            String teacher1 = plan.getTeacher1();
            String teacher2 = plan.getTeacher2();
            String teacher3 = plan.getTeacher3();

            TeachingPlan teachingPlan = new TeachingPlan()
                    .setCourseName(plan.getCourseName())
                    .setCohort(plan.getCohort())
                    .setClassroom(classroom.getId())
                    .setWeek(String.valueOf(time.getWeekOrder()))
                    .setTeacher1(teacher1)
                    .setTeacher2(teacher2)
                    .setTeacher3(teacher3)
                    .setCourseCode(plan.getCourseCode())
                    .setDuration(1)
                    .setSoftware(null)
                    .setRun(null)
                    .setManager(null)
                    .setCert(null)
                    .setClassSize(plan.getClassSize())
                    .setCourseDate(new Date());
        }

        //-----检查时间是否冲突
        for (Map.Entry<String,Integer> entry:map.entrySet()) {
            //同一时间，出现相同班级，或者相同教师，或者相同的教室
            if(entry.getValue() != 0){
                penalty += 1000;
            }
        }

        return 1 / (1 + penalty);
    }

    //惩罚值计算



}
