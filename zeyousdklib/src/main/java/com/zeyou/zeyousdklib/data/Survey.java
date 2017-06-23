 package com.zeyou.zeyousdklib.data;

 import java.util.ArrayList;
 import java.util.Comparator;

 public class Survey
   implements Comparator
 {
   public String surveyid;
   public String subject;
   public String webinarid;
   public ArrayList<Question> questions;

   public int compare(Object lhs, Object rhs)
   {
     Question question = (Question)lhs;
     Question question1 = (Question)rhs;
     int flag = question.ordernum >= question1.ordernum ? 0 : 1;
     return flag;
   }

   public static class Question
   {
     public String ques_id;
     public String subject;
     public int ordernum;
     public int must;
     public int type;
     public ArrayList<String> options;
     public ArrayList<String> answer = new ArrayList();
   }
 }



