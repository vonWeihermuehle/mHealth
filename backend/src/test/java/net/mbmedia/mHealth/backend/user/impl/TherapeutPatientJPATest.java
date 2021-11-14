package net.mbmedia.mHealth.backend.user.impl;

import net.mbmedia.mHealth.backend.user.ITherapeutPatientService;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.util.UUIDHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntityBuilder;
import static net.mbmedia.mHealth.backend.util.UUIDHelper.generateUUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TherapeutPatientJPATest
{
    @Autowired
    private IUserService userService;

    @Autowired
    private ITherapeutPatientService therapeutPatientService;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(userService, therapeutPatientService);
    }

    @Test
    public void soll_Patient_hinzufuegen()
    {
        UserEntity therapeut = getStandardUserEntityBuilder().withTherapeut(true).build();
        UserEntity patient = getStandardUserEntityBuilder().withTherapeut(false).build();

        TherapeutPatientEntity entity = new TherapeutPatientEntity.BUILDER()
                .withTherapeut(therapeut.getUuid())
                .withPatient(patient.getUuid())
                .build();

        addTherapeutPatient(entity);

        assert (therapeutPatientService.getAll().size()) == 1;
    }

    @Test
    public void soll_Patient_loeschen()
    {
        UserEntity therapeut = getStandardUserEntityBuilder().withTherapeut(true).build();
        UserEntity patient = getStandardUserEntityBuilder().withTherapeut(false).build();

        TherapeutPatientEntity entity = new TherapeutPatientEntity.BUILDER()
                .withTherapeut(therapeut.getUuid())
                .withPatient(patient.getUuid())
                .build();

        addTherapeutPatient(entity);

        therapeutPatientService.delete(entity);
        assert (therapeutPatientService.getAll().size()) == 0;
    }

    @Test
    public void soll_alle_Kombinationen_anzeigen_koennen()
    {
        TherapeutPatientEntity entity = new TherapeutPatientEntity.BUILDER()
                .withTherapeut(generateUUID())
                .withPatient(generateUUID())
                .build();

        addTherapeutPatient(entity);

        assert (therapeutPatientService.getAll().size()) == 1;
    }

    @Test
    public void soll_Patienten_fuer_Therapeut_anzeigen()
    {
        UserEntity therapeut = getStandardUserEntityBuilder().withTherapeut(true).build();
        UserEntity andererTherapeut = getStandardUserEntityBuilder().withTherapeut(true).build();
        UserEntity patient = getStandardUserEntityBuilder().withTherapeut(false).build();
        UserEntity andererPatient = getStandardUserEntityBuilder().withTherapeut(false).build();

        userService.register(patient);
        TherapeutPatientEntity kombi1 = new TherapeutPatientEntity.BUILDER().withTherapeut(therapeut.getUuid()).withPatient(patient.getUuid()).build();
        TherapeutPatientEntity kombi2 = new TherapeutPatientEntity.BUILDER().withTherapeut(andererTherapeut.getUuid()).withPatient(andererPatient.getUuid()).build();
        addTherapeutPatient(kombi1, kombi2);

        List<UserEntity> patienten = therapeutPatientService.getPatientenFuer(therapeut.getUuid());
        assert(patienten.size()) == 1;
        assert patienten.get(0).getUuid().equals(patient.getUuid());
    }

    @Test
    public void deleteForPatient()
    {
        String patientUUID = generateUUID();
        String therapeutUUID = generateUUID();
        TherapeutPatientEntity e = new TherapeutPatientEntity.BUILDER()
                .withTherapeut(therapeutUUID)
                .withPatient(patientUUID)
                .build();
        addTherapeutPatient(e);

        therapeutPatientService.deleteForPatient(patientUUID);

        List<UserEntity> patientenFuer = therapeutPatientService.getPatientenFuer(therapeutUUID);
        assert(patientenFuer.isEmpty());
    }

    private void addTherapeutPatient(TherapeutPatientEntity... entities)
    {
        Arrays.stream(entities).forEach(e -> therapeutPatientService.add(e));
    }

}