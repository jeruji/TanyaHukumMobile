package com.app.tanyahukum.model;

/**
 * Created by emerio on 5/31/17.
 */

public class Regency {
    Long id;
    Long prov_id;
            String nama;

    public Long getProv_id() {
        return prov_id;
    }

    public void setProv_id(Long prov_id) {
        this.prov_id = prov_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
