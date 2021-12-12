package net.mbmedia.mHealth.backend;

import net.mbmedia.mHealth.backend.chat.IChatService;
import net.mbmedia.mHealth.backend.chat.impl.MessageEntity;
import net.mbmedia.mHealth.backend.fragebogen.IFragebogenService;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenAbgeschlossenEntity;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenEntity;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenZuweisungEntity;
import net.mbmedia.mHealth.backend.kontakt.IKontakteService;
import net.mbmedia.mHealth.backend.kontakt.impl.KontaktEntity;
import net.mbmedia.mHealth.backend.ort.IOrteService;
import net.mbmedia.mHealth.backend.ort.impl.OrtEntity;
import net.mbmedia.mHealth.backend.ort.impl.OrtEntityTestDatenErzeuger;
import net.mbmedia.mHealth.backend.param.IParameterService;
import net.mbmedia.mHealth.backend.unterstuetzung.IUnterstuetzungService;
import net.mbmedia.mHealth.backend.unterstuetzung.impl.UnterstuetzungEntity;
import net.mbmedia.mHealth.backend.user.ITherapeutPatientService;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.impl.TherapeutPatientEntity;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import net.mbmedia.mHealth.backend.util.UUIDHelper;
import net.mbmedia.mHealth.backend.util.ValueProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static java.time.LocalDate.now;
import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.chat.impl.MessageEntityTestdatenErzeuger.getStandardMessageBuilder;
import static net.mbmedia.mHealth.backend.fragebogen.FragebogenAbgeschlossenEntityTestdatenErzeuger.getStandardFragebogenAbgeschlossenBuilder;
import static net.mbmedia.mHealth.backend.fragebogen.FragebogenEntityTestdatenErzeuger.getStandardFragebogenEntityBuilder;
import static net.mbmedia.mHealth.backend.fragebogen.FragebogenZuweisungEntityTestdatenErzeuger.getStandardFragebogenZuweisungBuilder;
import static net.mbmedia.mHealth.backend.kontakt.impl.KontakteEntityTestDatenErzeuger.getStandardKontakteBuilder;
import static net.mbmedia.mHealth.backend.mail.impl.EmailParameterTestDatenErzeuger.generateMailParameter;
import static net.mbmedia.mHealth.backend.unterstuetzung.UnterstuetzungEntityTestDatenErzeuger.standardUnterstuetzungBuilder;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.STANDARD_PASSWORT;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntityBuilder;
import static net.mbmedia.mHealth.backend.util.CryptoHelper.hash;
import static net.mbmedia.mHealth.backend.util.ValueProvider.mitZufallswerten;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestDatenEinspieler {
    private static final String THERAPEUT_UUID = UUIDHelper.generateUUID();
    private static final String Patient_UUID = UUIDHelper.generateUUID();
    private static final String THERAPEUT_USERNAME = "T";
    private static final String PATIENT_USERNAME = "P";
    private static final ValueProvider zufall = mitZufallswerten();


    @Autowired
    private IUserService userService;

    @Autowired
    private ITherapeutPatientService therapeutPatientService;

    @Autowired
    private IUnterstuetzungService unterstuetzungService;

    @Autowired
    private IKontakteService kontakteService;

    @Autowired
    private IOrteService orteService;

    @Autowired
    private IChatService chatService;

    @Autowired
    private IParameterService parameterService;

    @Autowired
    private IFragebogenService fragebogenService;

    @BeforeEach
    public void leereTabellen() {
        truncateAllTables(userService, therapeutPatientService, unterstuetzungService, kontakteService, orteService, chatService, parameterService, fragebogenService);
    }

    @Disabled
    @Test
    public void spiele_Testdaten_ein() {
        spieleUserEin();
        spieleUnterstuetzungEin();
        spieleKontakteEin();
        spieleOrteEin();
        spieleChatEin();
        spieleParameterEin();
        spieleFrageboegenEin();
    }

    private void spieleUserEin() {
        UserEntity Therapeut = getStandardUserEntityBuilder()
                .withEmail(zufall.emailAddress())
                .withTherapeut(true)
                .withNachname(zufall.familienname())
                .withVorname(zufall.vorname())
                .withPasswort(hash(STANDARD_PASSWORT))
                .withUUID(THERAPEUT_UUID)
                .withUsername(THERAPEUT_USERNAME)
                .build();
        UserEntity Patient = getStandardUserEntityBuilder()
                .withEmail(zufall.emailAddress())
                .withTherapeut(false)
                .withNachname(zufall.familienname())
                .withVorname(zufall.vorname())
                .withPasswort(hash(STANDARD_PASSWORT))
                .withUUID(Patient_UUID)
                .withUsername(PATIENT_USERNAME)
                .build();

        UserEntity Patient2 = getStandardUserEntityBuilder()
                .withEmail(zufall.emailAddress())
                .withTherapeut(false)
                .withNachname(zufall.familienname())
                .withVorname(zufall.vorname())
                .withPasswort(hash(STANDARD_PASSWORT))
                .withUUID(UUIDHelper.generateUUID())
                .withUsername("P2")
                .build();

        UserEntity admin = getStandardUserEntityBuilder()
                .withTechUser(true)
                .withUsername("A")
                .withPasswort(hash(STANDARD_PASSWORT))
                .build();

        persistiere(Therapeut, Patient, Patient2, admin);

        TherapeutPatientEntity e = new TherapeutPatientEntity.BUILDER()
                .withTherapeut(THERAPEUT_UUID)
                .withPatient(Patient_UUID)
                .build();

        TherapeutPatientEntity e2 = new TherapeutPatientEntity.BUILDER()
                .withTherapeut(THERAPEUT_UUID)
                .withPatient(Patient2.getUuid())
                .build();


        persistiere(e, e2);
    }

    private void spieleUnterstuetzungEin() {
        UnterstuetzungEntity e = standardUnterstuetzungBuilder()
                .withAuthor(THERAPEUT_UUID)
                .withEmpfaenger(Patient_UUID)
                .build();

        unterstuetzungService.add(e);
    }

    private void spieleKontakteEin() {
        KontaktEntity entity = getStandardKontakteBuilder()
                .withUserID(Patient_UUID)
                .build();
        kontakteService.addKontakt(entity);
    }

    private void spieleOrteEin() {
        OrtEntity ortEntity = OrtEntityTestDatenErzeuger.standardOrtBuilder()
                .withPatient(Patient_UUID)
                .build();
        orteService.add(ortEntity);
    }

    private void spieleChatEin() {
        MessageEntity anPatient = getStandardMessageBuilder()
                .withAuthorID(THERAPEUT_UUID)
                .withEmpfaengerID(Patient_UUID)
                .withNachricht("Hallo Patient, wie geht es Ihnen?")
                .build();

        MessageEntity anTherapeut = getStandardMessageBuilder()
                .withAuthorID(Patient_UUID)
                .withEmpfaengerID(THERAPEUT_UUID)
                .withNachricht("Hallo Therapeut, mir geht es gut!")
                .build();

        persistiere(anPatient, anTherapeut);
    }

    private void spieleFrageboegenEin() {
        Optional<UserEntity> patient = userService.getById(Patient_UUID);
        FragebogenEntity fragebogen = getStandardFragebogenEntityBuilder().withAuthor(THERAPEUT_UUID).build();

        fragebogenService.addFragebogen(fragebogen);

        FragebogenZuweisungEntity zuweisung = getStandardFragebogenZuweisungBuilder()
                .withFragebogen(fragebogen)
                .withEmpfaenger(patient.get())
                .withErstellt(now())
                .build();

        fragebogenService.addZuweisung(zuweisung);

        FragebogenAbgeschlossenEntity abgeschlossen = getStandardFragebogenAbgeschlossenBuilder()
                .withPatient(patient.get())
                .withErstellt(now())
                .build();
        fragebogenService.addAbgeschlossen(abgeschlossen);
    }

    private void spieleParameterEin() {
        generateMailParameter().forEach(p -> parameterService.persist(p));
    }

    private void persistiere(TherapeutPatientEntity... entities) {
        Arrays.stream(entities).forEach(t -> therapeutPatientService.add(t));
    }

    private void persistiere(UserEntity... user) {
        Arrays.stream(user).forEach(u -> userService.register(u));
    }

    private void persistiere(MessageEntity... messages) {
        Arrays.stream(messages).forEach(m -> chatService.add(m));
    }
}
