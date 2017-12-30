package com.app.tanyahukum.model;

import java.util.ArrayList;

/**
 * Created by emerio on 4/9/17.
 */

public class Consultations {
    private String consultationId;
    private String clientId;
    private String status;
    private String title;
    private String answers;
    private String historyId;
    private String consultantName;
    private String clientName;
    private String clientCity;
    private ArrayList<String> attachment;
    private String statusAppointment;
    private String questions;
    private String consultationsType;
    private String expertRecomendations;
    private String lastUpdateDate;
    private String consultationsDate;
    private String consultantId;
    private String chronology;
    private String publish;

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getClientCity() {
        return clientCity;
    }

    public void setClientCity(String clientCity) {
        this.clientCity = clientCity;
    }

    public String getChronology() {
        return chronology;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
    }

    public ArrayList<String> getAttachment() {
        return attachment;
    }

    public void setAttachment(ArrayList<String> attachment) {
        this.attachment = attachment;
    }

    public String getConsultantName() {
        return consultantName;
    }
    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getStatusAppointment() {
        return statusAppointment;
    }

    public void setStatusAppointment(String statusAppointment) {
        this.statusAppointment = statusAppointment;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Consultations() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getConsultationsType() {
        return consultationsType;
    }

    public void setConsultationsType(String consultationsType) {
        this.consultationsType = consultationsType;
    }

    public String getExpertRecomendations() {
        return expertRecomendations;
    }

    public void setExpertRecomendations(String expertRecomendations) {
        this.expertRecomendations = expertRecomendations;
    }

    public String getConsultationsDate() {
        return consultationsDate;
    }

    public void setConsultationsDate(String consultationsDate) {
        this.consultationsDate = consultationsDate;
    }

    public String getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }

}
