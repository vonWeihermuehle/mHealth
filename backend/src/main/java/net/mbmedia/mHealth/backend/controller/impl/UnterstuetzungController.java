package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.controller.IUnterstuetzungController;
import net.mbmedia.mHealth.backend.unterstuetzung.IUnterstuetzungService;
import net.mbmedia.mHealth.backend.unterstuetzung.impl.UnterstuetzungEntity;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static net.mbmedia.mHealth.backend.util.FailureAnswer.*;
import static net.mbmedia.mHealth.backend.util.RejectUtils.rejectIf;
import static net.mbmedia.mHealth.backend.util.ResponseHelper.simpleSuccesAnswer;
import static net.mbmedia.mHealth.backend.util.ResponseHelper.successAnswerWithObject;

@RestController
@RequestMapping(path = "/api/unterstuetzung")
public class UnterstuetzungController extends BaseController implements IUnterstuetzungController
{

    @Autowired
    private IUnterstuetzungService unterstuetzungService;

    @PostMapping("/addUebung")
    @Override
    public String addUebung(String token, String titel, String text, String empfaenger)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTherapeut(userID) || !isTokenValid(token));

        UnterstuetzungEntity entity = new UnterstuetzungEntity.BUILDER()
                .withAuthor(userID.get())
                .withTitel(titel)
                .withText(text)
                .withEmpfaenger(empfaenger)
                .build();

        Optional<Long> id = unterstuetzungService.add(entity);

        if (id.isPresent())
        {
            return simpleSuccesAnswer();
        }

        return failureAnswer(SOME);
    }

    @PostMapping("/updateUebung")
    @Override
    public String updateUebung(String token, Long uebungID, String titel, String text)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        Optional<UnterstuetzungEntity> uebung = unterstuetzungService.getByIDAndErstelltVon(uebungID, userID.get());
        if (!uebung.isPresent() || !uebung.get().getAuthor().equals(userID.get()))
        {
            return failureAnswer(NO_PERMISSION);
        }

        unterstuetzungService.update(uebungID, titel, text);

        return simpleSuccesAnswer();
    }

    @GetMapping("/getUebungen")
    @Override
    public String getUebungen(String token)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isPatient(userID));

        List<UnterstuetzungEntity> uebungen = unterstuetzungService.getAllFuer(userID.get());
        return successAnswerWithObject(uebungen);
    }

    @PostMapping("/getUebungenFuerPatient")
    @Override
    public String getUebungenFuerPatient(String token, String uuid)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        Optional<UserEntity> patient = userService.getById(uuid);
        if (!patient.isPresent())
        {
            return failureAnswer(NO_USER_FOUND);
        }

        List<UnterstuetzungEntity> uebungen = unterstuetzungService.getAllFuer(patient.get().getUuid());

        return successAnswerWithObject(uebungen);
    }

    @PostMapping("/delUebung")
    @Override
    public String delUebung(String token, Long uebungID)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        Optional<UnterstuetzungEntity> uebung = unterstuetzungService.getByIDAndErstelltVon(uebungID, userID.get());
        if (!uebung.isPresent() || !uebung.get().getAuthor().equals(userID.get()))
        {
            return failureAnswer(NO_PERMISSION);
        }

        unterstuetzungService.removeByID(uebungID);

        return simpleSuccesAnswer();
    }

}
