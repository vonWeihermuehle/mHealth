package net.mbmedia.mHealth.backend.user.impl;

import javax.persistence.*;
import java.io.Serializable;

import static net.mbmedia.mHealth.backend.user.impl.TherapeutPatientEntity.TABLE_NAME;

@NamedQuery(name = "findFor", query = "SELECT u from UserEntity u " +
        "join TherapeutPatientEntity t on t.id.patientUUID = u.uuid " +
        "where t.id.therapeutUUID = :uuid")

@Entity
@Table(name = TABLE_NAME)
public class TherapeutPatientEntity
{
    public static final String TABLE_NAME = "TherapeutPatient";

    @EmbeddedId
    private TherapeutPatientID id;

    private TherapeutPatientEntity()
    {
    }

    public TherapeutPatientEntity(TherapeutPatientID id)
    {
        this.id = id;
    }

    @Embeddable
    public static class TherapeutPatientID implements Serializable
    {

        @Column(name = "therapeutUUID")
        private String therapeutUUID;

        @Column(name = "patientUUID")
        private String patientUUID;

        public TherapeutPatientID()
        {
        }

        public TherapeutPatientID(String therapeutUUID, String patientUUID)
        {
            this.therapeutUUID = therapeutUUID;
            this.patientUUID = patientUUID;
        }

        public String getTherapeutUUID()
        {
            return therapeutUUID;
        }

        public String getPatientUUID()
        {
            return patientUUID;
        }

        private void setTherapeutUUID(String therapeutUUID)
        {
            this.therapeutUUID = therapeutUUID;
        }

        private void setPatientUUID(String patientUUID)
        {
            this.patientUUID = patientUUID;
        }
    }

    public static class BUILDER
    {
        private String therapeutUUID;
        private String patientUUID;

        public BUILDER withTherapeut(String uuid)
        {
            this.therapeutUUID = uuid;
            return this;
        }

        public BUILDER withPatient(String patient)
        {
            this.patientUUID = patient;
            return this;
        }

        public TherapeutPatientEntity build()
        {
            return new TherapeutPatientEntity(new TherapeutPatientID(therapeutUUID, patientUUID));
        }
    }

    public TherapeutPatientID getId()
    {
        return id;
    }

    private void setId(TherapeutPatientID id)
    {
        this.id = id;
    }

    public TherapeutPatientID toPrimaryKey()
    {
        return new TherapeutPatientID(this.getId().getTherapeutUUID(), this.getId().getPatientUUID());
    }
}
