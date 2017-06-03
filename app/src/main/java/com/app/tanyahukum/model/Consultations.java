package com.app.tanyahukum.model;

import java.util.Date;

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

    public String getClientCity() {
        return clientCity;
    }

    public void setClientCity(String clientCity) {
        this.clientCity = clientCity;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    private String attachment;
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

    private String statusAppointment;


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

    private String questions;
    private String consultationsType;
    private String expertRecomendations;

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    private String lastUpdateDate;
    public Consultations() {
    }

    private String consultationsDate;

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

    private String consultantId;
}
