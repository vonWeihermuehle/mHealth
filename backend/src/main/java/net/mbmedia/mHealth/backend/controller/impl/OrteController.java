package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.controller.IOrteController;
import net.mbmedia.mHealth.backend.ort.IOrteService;
import net.mbmedia.mHealth.backend.ort.impl.OrtEntity;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static net.mbmedia.mHealth.backend.util.FailureAnswer.SOME;
import static net.mbmedia.mHealth.backend.util.RejectUtils.rejectIf;
import static net.mbmedia.mHealth.backend.util.ResponseHelper.*;

@RestController
@RequestMapping(path = "/api/orte")
public class OrteController extends BaseController implements IOrteController
{

    @Autowired
    private IOrteService orteService;

    @PostMapping("/add")
    @Override
    public String addOrt(String token, String titel, String beschreibung, String patient, String lat, String lng)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        OrtEntity ort = new OrtEntity.BUILDER()
                .withTitel(titel)
                .withBeschreibung(beschreibung)
                .withPatient(patient)
                .withLat(lat)
                .withLng(lng)
                .build();

        Optional<Long> ortId = orteService.add(ort);

        if (ortId.isPresent())
        {
            return simpleSuccessAnswer();
        }
        return failureAnswer(SOME);
    }

    @PostMapping("/update")
    @Override
    public String updateOrt(String token, Long ortID, String titel, String beschreibung, String lat, String lng)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        OrtEntity ort = new OrtEntity.BUILDER()
                .withID(ortID)
                .withTitel(titel)
                .withBeschreibung(beschreibung)
                .withLat(lat)
                .withLng(lng)
                .build();

        orteService.update(ort);

        return simpleSuccessAnswer();
    }

    @PostMapping("/get")
    @Override
    public String getOrteFuerPatient(String token, String uuid, Boolean anonym)
    {
        Optional<String> userID = getUserIDFromToken(token);
        Optional<UserEntity> patient = userService.getById(uuid);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !patient.isPresent());

        List<OrtEntity> allFuer = orteService.getAllFuer(uuid);

        if(anonym){
            allFuer = allFuer.stream()
                    .map(OrtEntity::removeUnnecessaryData)
                    .collect(toList());
        }

        return successAnswerWithObject(allFuer);
    }

    @PostMapping("/del")
    @Override
    public String delOrt(String token, Long ortID)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        orteService.removeByID(ortID);
        return simpleSuccessAnswer();
    }
}
