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
    private int ID;

    @Column(name = "lastUpdate")
    private Long lastupdate;

    public ApplicationVariables(){}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Long lastupdate) {
        this.lastupdate = lastupdate;
    }
}
