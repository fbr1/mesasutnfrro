package com.fbr1.mesasutnfrro.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="application_variables")
public class ApplicationVariables {

    @Id
    @Column(name = "id_application_vars")
    private int Id;

    @Column(name = "lastupdate")
    private Long lastupdate;

    // Update interval in ms
    // Maximum allowed is ~24 days, restricted by int max number and INT in the db
    @Column(name = "updateinterval")
    private int updateInterval;

    public ApplicationVariables(){}

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public Long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Long lastupdate) {
        this.lastupdate = lastupdate;
    }

    public int getUpdateInteval() {
        return updateInterval;
    }

    public void setUpdateInteval(int updateInteval) {
        this.updateInterval = updateInteval;
    }
}
