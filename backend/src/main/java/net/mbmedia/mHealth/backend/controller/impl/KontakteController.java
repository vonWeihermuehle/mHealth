package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.controller.IKontakteController;
import net.mbmedia.mHealth.backend.kontakt.IKontakteService;
import net.mbmedia.mHealth.backend.kontakt.impl.KontaktEntity;
import net.mbmedia.mHealth.backend.util.FailureAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static net.mbmedia.mHealth.backend.util.FailureAnswer.NO_PERMISSION;
import static net.mbmedia.mHealth.backend.util.RejectUtils.rejectIf;
import static net.mbmedia.mHealth.backend.util.ResponseHelper.*;

@RestController
@RequestMapping(path = "/api/kontakte")
public class KontakteController extends BaseController implements IKontakteController {
    @Autowired
    private IKontakteService kontakteService;


    @PostMapping("/add")
    @Override
    public String add(String token, String name, String art, String email, String phone, String patientID) {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        KontaktEntity kontakt = new KontaktEntity.BUILDER()
                .withName(name)
                .withArt(art)
                .withEmail(email)
                .withPhone(phone)
                .withUserID(patientID)
                .build();

        Optional<Long> id = kontakteService.addKontakt(kontakt);

        if (id.isPresent()) {
            return simpleSuccessAnswer();
        } else {
            return failureAnswer(FailureAnswer.SOME);
        }
    }

    @PostMapping("/remove")
    @Override
    public String remove(String token, Long id) {

        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());
        Optional<KontaktEntity> kontakt = kontakteService.getByID(id);
        if (kontakt.isPresent()) {
            kontakteService.delKontakt(id);
            return simpleSuccessAnswer();
        }

        return failureAnswer(NO_PERMISSION);
    }

    @PostMapping("/update")
    @Override
    public String update(String token, Long id, String name, String art, String email, String phone) {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        KontaktEntity entity = new KontaktEntity.BUILDER()
                .withID(id)
                .withName(name)
                .withArt(art)
                .withEmail(email)
                .withPhone(phone)
                .build();

        Optional<KontaktEntity> oldKontakt = kontakteService.getByID(entity.getId());
        if (oldKontakt.isPresent()) {
            kontakteService.updateKontakt(entity);
            return simpleSuccessAnswer();
        }
        return failureAnswer(NO_PERMISSION);
    }

    @PostMapping("/getAllFuer")
    @Override
    public String getAllFuer(String token, String patientUUID) {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        List<KontaktEntity> allFuer = kontakteService.getAllFuer(patientUUID);
        return successAnswerWithObject(allFuer);
    }

    @GetMapping("/getOwn")
    @Override
    public String getOwn(String token) {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isPatient(userID));

        List<KontaktEntity> allFuer = kontakteService.getAllFuer(userID.get());
        return successAnswerWithObject(allFuer);
    }
}
