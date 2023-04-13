package com.letoy.main.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SQLUtil {
    // Version 1.0.0
    public static void main(String[] args) {
        String statement = "id, name, customer, date, amount, remark, type";
        String tableName = "sale_data";
        getInsertStatement(statement,tableName);
        getUpdateStatement(statement,tableName);

    }

    public static void getInsertStatement(String val,String tableName) {
        val = val.replace(" ","");
        val = val.replace("\n","");
        String myVal = val.replace(",","`,`");
        myVal = "`"+myVal+"`";
        String[] stringList = val.split(",");
        List<String> upperList = new ArrayList<>();
        for(String terms : stringList){
            List<String> wordList = Arrays.asList(terms.split("_"));
            StringBuilder res = new StringBuilder();
            for(int i =0 ;i<wordList.size();i++ ){
                if (i==0){
                    res.append(wordList.get(i));
                }else{
                    StringBuilder r = new StringBuilder();
                    String[] chars = wordList.get(i).split("");
                    chars[0] = chars[0].toUpperCase();
                    for(String letter : chars){
                        r.append(letter);
                    }
                    res.append(r);
                }
            }
            upperList.add(res.toString());
        }
        StringBuilder res = new StringBuilder("insert into " + tableName + " (" + myVal + ") VALUES (");
        for(String word : upperList){
            res.append("#{").append(word).append("},");
        }
        res.append(")");
        System.out.println(res);
    }

    public static void getUpdateStatement(String val,String tableName){
        val = val.replace(" ","");
        val = val.replace("\n","");
        String[] stringList = val.split(",");
        List<String> upperList = new ArrayList<>();
        for(String terms : stringList){
            List<String> wordList = Arrays.asList(terms.split("_"));
            StringBuilder res = new StringBuilder();
            for(int i =0 ;i<wordList.size();i++ ){
                if (i==0){
                    res.append(wordList.get(i));
                }else{
                    StringBuilder r = new StringBuilder();
                    String[] chars = wordList.get(i).split("");
                    chars[0] = chars[0].toUpperCase();
                    for(String letter : chars){
                        r.append(letter);
                    }
                    res.append(r);
                }
            }
            upperList.add(res.toString());
        }
        StringBuilder res = new StringBuilder("update " + tableName + "\nset ");
        for(int i =0 ;i<stringList.length;i++){
            if(i == stringList.length-1) {
                res.append("\n`").append(stringList[i]).append("`=#{").append(upperList.get(i)).append("}");
                res.append("\n where");
            }else{
                res.append("\n`").append(stringList[i]).append("`=#{").append(upperList.get(i)).append("},");
            }


        }
        System.out.println(res);
    }
}

