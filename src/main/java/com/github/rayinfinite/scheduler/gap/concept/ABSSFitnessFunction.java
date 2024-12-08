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

            plan = Constant.PLAN_LIST.get(i);
            classroom = Constant.CLASSROOM_LIST.get(classroomGene.getValue());
            time = Constant.TIME_LIST.get(timeGene.getValue());
            int weekOrder = time.getWeekOrder();


            //------教室座位数 > 班级学生数
            if (classroom.getSize() < plan.getClassSize()){
                penalty += 1000;
            }
            //Check Software for Classroom
            if (!classroom.getSoftware().equals(plan.getClassroomType())){
                penalty += 1000;
            }

            //检查时间安排合理度
            penalty += this.calculateTimePenalty(
                    weekOrder
            );

            //构造map，用于后面检测冲突
            String classsKey = "classs:" + weekOrder+":"+courseOrder+":"+plan.getClasss();
            Integer classsValue = map.get(classsKey);
            if(classsValue !=null){
                map.put(classsKey,classsValue + 1);
            }else{
                map.put(classsKey,0);
            }
            String teacherKey = "teacher:" + weekOrder+":"+courseOrder+":"+plan.getTeacher();
            Integer teacherValue = map.get(teacherKey);
            if(teacherValue !=null){
                map.put(teacherKey,teacherValue + 1);
            }else{
                map.put(teacherKey,0);
            }
            String classroomKey = "classroom:" + weekOrder+":"+courseOrder+":"+classroom.getId();
            Integer classroomValue = map.get(classroomKey);
            if(classroomValue !=null){
                map.put(classroomKey,classroomValue + 1);
            }else{
                map.put(classroomKey,0);
            }
            //构造临时数据，用于后面检测冲突
            curriculumList.add(new Curriculum()
                    .setClasss(plan.getClasss())
                    .setCourse(plan.getCourse())
                    .setTeacher(plan.getTeacher())
                    .setClassroom(classroom.getId())
                    .setWeekOrder(time.getWeekOrder())
                    .setCourseOrder(time.getCourseOrder())
            );
        }
        //-----检查时间是否冲突
        for (Map.Entry<String,Integer> entry:map.entrySet()) {
            //同一时间，出现相同班级，或者相同教师，或者相同的教室
            if(entry.getValue() != 0){
                penalty += 1000;
            }
        }
        // 课程离散程度
        penalty += this.calculateDiscretePenalty(curriculumList);

        return 1 / (1 + penalty);
    }






}
