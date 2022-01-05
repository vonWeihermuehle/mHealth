package net.mbmedia.mHealth.backend.ort.impl;

import javax.persistence.*;

import static net.mbmedia.mHealth.backend.ort.impl.OrtEntity.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
public class OrtEntity
{
    public final static String TABLE_NAME = "orte";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "patient_uuid")
    private String patientUUID;

    @Column(name = "titel")
    private String titel;

    @Column(name = "beschreibung")
    private String beschreibung;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

    private OrtEntity(Long id, String patientUUID, String titel, String beschreibung, String lat, String lng)
    {
        this.id = id;
        this.patientUUID = patientUUID;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.lat = lat;
        this.lng = lng;
    }

    public OrtEntity()
    {
    }

    public static class BUILDER
    {
        private Long id;
        private String patientUUID;
        private String titel;
        private String beschreibung;
        private String lat;
        private String lng;

        public BUILDER withID(Long id)
        {
            this.id = id;
            return this;
        }

        public BUILDER withPatient(String uuid)
        {
            this.patientUUID = uuid;
            return this;
        }

        public BUILDER withTitel(String titel)
        {
            this.titel = titel;
            return this;
        }

        public BUILDER withBeschreibung(String beschreibung)
        {
            this.beschreibung = beschreibung;
            return this;
        }

        public BUILDER withLat(String lat)
        {
            this.lat = lat;
            return this;
        }

        public BUILDER withLng(String lng)
        {
            this.lng = lng;
            return this;
        }

        public OrtEntity build()
        {
            return new OrtEntity(id, patientUUID, titel, beschreibung, lat, lng);
        }

        public static OrtEntity.BUILDER toBuilder(OrtEntity entity)
        {
            return new OrtEntity.BUILDER()
                    .withID(entity.getId())
                    .withTitel(entity.getTitel())
                    .withBeschreibung(entity.getBeschreibung())
                    .withLng(entity.getLng())
                    .withLat(entity.getLat())
                    .withPatient(entity.getPatientUUID());
        }
    }

    public long getId()
    {
        return id;
    }

    public String getPatientUUID()
    {
        return patientUUID;
    }

    public String getTitel()
    {
        return titel;
    }

    public String getBeschreibung()
    {
        return beschreibung;
    }

    public String getLat()
    {
        return lat;
    }

    public String getLng()
    {
        return lng;
    }

    void setId(long id)
    {
        this.id = id;
    }

    void setPatientUUID(String patientUUID)
    {
        this.patientUUID = patientUUID;
    }

    void setTitel(String titel)
    {
        this.titel = titel;
    }

    void setBeschreibung(String beschreibung)
    {
        this.beschreibung = beschreibung;
    }

    void setLat(String lat)
    {
        this.lat = lat;
    }

    void setLng(String lng)
    {
        this.lng = lng;
    }

    public OrtEntity removeUnnecessaryData(){
        this.id = null;
        this.beschreibung = null;
        this.patientUUID = null;
        this.titel = null;
        return this;
    }
}
