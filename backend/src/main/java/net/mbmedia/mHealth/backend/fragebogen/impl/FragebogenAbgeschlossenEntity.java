package net.mbmedia.mHealth.backend.fragebogen.impl;

import net.mbmedia.mHealth.backend.user.impl.UserEntity;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenAbgeschlossenEntity.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
public class FragebogenAbgeschlossenEntity
{
    public static final String TABLE_NAME = "frageboegen_abgeschlossen";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titel")
    private String titel;

    @Column(name = "beschreibung")
    private String beschreibung;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_uuid", referencedColumnName = "uuid")
    private UserEntity patient;

    @Column(name = "ergebnis")
    private byte[] ergebnis;

    @Column(name = "wert")
    private Integer wert;

    @Column(name = "erstellt")
    private LocalDate erstellt;

    public FragebogenAbgeschlossenEntity()
    {
    }

    private FragebogenAbgeschlossenEntity(Long id, String titel, String beschreibung, UserEntity patient, byte[] ergebnis, Integer wert, LocalDate erstellt)
    {
        this.id = id;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.patient = patient;
        this.ergebnis = ergebnis;
        this.wert = wert;
        this.erstellt = erstellt;
    }

    public static class BUILDER
    {
        private Long id;
        private String titel;
        private String beschreibung;
        private UserEntity patient;
        private byte[] ergebnis;
        private Integer wert;
        private LocalDate erstellt;

        public BUILDER withID(Long id)
        {
            this.id = id;
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

        public BUILDER withPatient(UserEntity patient)
        {
            this.patient = patient;
            return this;
        }

        public BUILDER withErgebnis(byte[] ergebnis)
        {
            this.ergebnis = ergebnis;
            return this;
        }

        public BUILDER withErgebnis(String ergebnis)
        {
            return withErgebnis(ergebnis.getBytes(StandardCharsets.UTF_8));
        }

        public BUILDER withWert(Integer wert)
        {
            this.wert = wert;
            return this;
        }

        public BUILDER withErstellt(LocalDate date)
        {
            this.erstellt = date;
            return this;
        }

        public FragebogenAbgeschlossenEntity build()
        {
            return new FragebogenAbgeschlossenEntity(this.id, this.titel, this.beschreibung, this.patient, this.ergebnis, this.wert, this.erstellt);
        }
    }

    public Long getId()
    {
        return id;
    }

    public String getTitel()
    {
        return titel;
    }

    public String getBeschreibung()
    {
        return beschreibung;
    }

    public UserEntity getPatient()
    {
        return patient;
    }

    public String getErgebnis()
    {
        return new String(ergebnis, StandardCharsets.UTF_8);
    }

    public Integer getWert()
    {
        return wert;
    }

    public LocalDate getErstellt()
    {
        return erstellt;
    }

    private void setId(Long id)
    {
        this.id = id;
    }

    private void setTitel(String titel)
    {
        this.titel = titel;
    }

    private void setBeschreibung(String beschreibung)
    {
        this.beschreibung = beschreibung;
    }

    private void setPatient(UserEntity patient)
    {
        this.patient = patient;
    }

    private void setErgebnis(byte[] ergebnis)
    {
        this.ergebnis = ergebnis;
    }

    private void setWert(Integer wert)
    {
        this.wert = wert;
    }

    private void setErstellt(LocalDate erstellt)
    {
        this.erstellt = erstellt;
    }
}
