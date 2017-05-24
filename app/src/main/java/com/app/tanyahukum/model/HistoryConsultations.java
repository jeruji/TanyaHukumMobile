package com.app.tanyahukum.model;

/**
 * Created by emerio on 4/18/17.
 */

public class HistoryConsultations {
    String questionsId;
    String questionsOld;
    String historyQuestionsId;
    String questionsDate;

    public String getQuestionsDate() {
        return questionsDate;
    }

    public void setQuestionsDate(String questionsDate) {
        this.questionsDate = questionsDate;
    }

    public String getAnswersDate() {
        return answersDate;
    }

    public void setAnswersDate(String answersDate) {
        this.answersDate = answersDate;
    }

    String answersDate;

    public String getHistoryQuestionsId() {
        return historyQuestionsId;
    }

    public void setHistoryQuestionsId(String historyQuestionsId) {
        this.historyQuestionsId = historyQuestionsId;
    }

    public String getQuestionsId() {

        return questionsId;
    }

    public void setQuestionsId(String questionsId) {
        this.questionsId = questionsId;
    }

    public String getQuestionsOld() {
        return questionsOld;
    }

    public void setQuestionsOld(String questionsOld) {
        this.questionsOld = questionsOld;
    }

    public String getAnswersOld() {
        return answersOld;
    }

    public void setAnswersOld(String answersOld) {
        this.answersOld = answersOld;
    }

    String answersOld;
}
