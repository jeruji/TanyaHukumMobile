package com.app.tanyahukum.model;

import java.util.ArrayList;

/**
 * Created by emerio on 4/18/17.
 */

public class HistoryConsultations {
    String questionsId;
    String questionsOld;
    String historyQuestionsId;
    String questionsDate;
    ArrayList<String> attachmentOld;

    public String getQuestionsDate() {
        return questionsDate;
    }

    public void setQuestionsDate(String questionsDate) {
        this.questionsDate = questionsDate;
    }

    public ArrayList<String> getAttachmentOld() {
        return attachmentOld;
    }

    public void setAttachmentOld(ArrayList<String> attachmentOld) {
        this.attachmentOld = attachmentOld;
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
