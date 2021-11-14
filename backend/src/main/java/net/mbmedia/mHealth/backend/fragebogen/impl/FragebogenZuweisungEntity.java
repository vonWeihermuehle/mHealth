package net.mbmedia.mHealth.backend.fragebogen.impl;

import net.mbmedia.mHealth.backend.user.impl.UserEntity;

import javax.persistence.*;
import java.time.LocalDate;

import static net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenZuweisungEntity.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
public class FragebogenZuweisungEntity
{
    public static final String TABLE_NAME = "fragebogen_zuweisung";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "frageboegen_id", referencedColumnName = "id")
    private FragebogenEntity fragebogen;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empfaenger", referencedColumnName = "uuid")
    private UserEntity empfaenger;

    @Column(name = "cron")
    private String cron;

    @Column(name = "erstellt")
    private LocalDate erstellt;

    private FragebogenZuweisungEntity()
    {
    }

    private FragebogenZuweisungEntity(Long id, FragebogenEntity fragebogen, UserEntity empfaenger, String cron, LocalDate erstellt)
    {
        this.id = id;
        this.fragebogen = fragebogen;
        this.empfaenger = empfaenger;
        this.cron = cron;
        this.erstellt = erstellt;
    }

    public static class BUILDER
    {
        private Long id;
        private FragebogenEntity fragebogen;
        private UserEntity empfaenger;
        private String cron;
        private LocalDate erstellt;

        public BUILDER withID(Long id)
        {
            this.id = id;
            return this;
        }

        public BUILDER withFragebogen(FragebogenEntity fragebogen)
        {
            this.fragebogen = fragebogen;
            return this;
        }

        public BUILDER withEmpfaenger(UserEntity user)
        {
            this.empfaenger = user;
            return this;
        }

        public BUILDER withCron(String cron)
        {
            this.cron = cron;
            return this;
        }

        public BUILDER withErstellt(LocalDate erstellt)
        {
            this.erstellt = erstellt;
            return this;
        }

        public FragebogenZuweisungEntity build()
        {
            return new FragebogenZuweisungEntity(this.id, this.fragebogen, this.empfaenger, this.cron, this.erstellt);
        }
    }

    public Long getId()
    {
        return id;
    }

    public FragebogenEntity getFragebogen()
    {
        return fragebogen;
    }

    public UserEntity getEmpfaenger()
    {
        return empfaenger;
    }

    public String getCron()
    {
        return cron;
    }

    public LocalDate getErstellt()
    {
        return erstellt;
    }

    void setId(Long id)
    {
        this.id = id;
    }

    void setFragebogen(FragebogenEntity fragebogen)
    {
        this.fragebogen = fragebogen;
    }

    void setEmpfaenger(UserEntity empfaenger)
    {
        this.empfaenger = empfaenger;
    }

    void setCron(String cron)
    {
        this.cron = cron;
    }

    void setErstellt(LocalDate erstellt)
    {
        this.erstellt = erstellt;
    }

    public FragebogenZuweisungEntity.BUILDER toBuilder()
    {
        return new FragebogenZuweisungEntity.BUILDER()
                .withEmpfaenger(empfaenger)
                .withFragebogen(fragebogen)
                .withCron(cron)
                .withErstellt(erstellt);
    }

    public FragebogenZuweisungEntity removeData()
    {
        this.empfaenger = this.empfaenger.toBuilder()
                .withPasswort(null)
                .withLng(null)
                .withLat(null)
                .build();
        this.fragebogen = null;
        return this;
    }
}
