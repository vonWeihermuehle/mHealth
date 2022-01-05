import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {GeneralResponse} from '../models/generalResponse.model';
import {UrlService} from './url.service';
import {LokalSpeichernService} from './lokalSpeichern.service';
import {Ort} from '../models/ort.model';
import {Subject} from 'rxjs';
import {User} from '../models/user.model';


@Injectable({
  providedIn: 'root'
})
export class OrteService {
  orteList = new Subject<Ort[]>();

  constructor(
    private httpClient: HttpClient,
    private lokalSpeichern: LokalSpeichernService,
    private urlService: UrlService) {
  }

  getAllFuer(uuid: string, anonym: boolean): void {
    let body = new HttpParams();
    body = body.set('uuid', uuid);
    body = body.set('anonym', anonym);

    this.httpClient.post<GeneralResponse<Ort[]>>(this.urlService.getUrl('api/orte/get'), body).subscribe(
      data => {
        this.orteList.next(data.data);
      }
    );
  }

  addOrt(ort: Ort): void {
    let body = new HttpParams();
    body = body.set('titel', ort.titel);
    body = body.set('beschreibung', ort.beschreibung);
    body = body.set('patient', ort.patientUUID);
    body = body.set('lat', ort.lat);
    body = body.set('lng', ort.lng);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/orte/add'), body).subscribe(data => {
      if (data.success) {
        this.updateOrte();
      }
    });
  }

  removeOrt(id: number): void {
    let body = new HttpParams();
    body = body.set('ortID', id);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/orte/del'), body).subscribe(data => {
      if (data.success) {
        this.updateOrte();
      }
    });
  }

  updateOrt(ort: Ort): void {
    let body = new HttpParams();
    body = body.set('ortID', ort.id);
    body = body.set('titel', ort.titel);
    body = body.set('beschreibung', ort.beschreibung);
    body = body.set('lat', ort.lat);
    body = body.set('lng', ort.lng);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/orte/update'), body).subscribe(data => {
      if (data.success) {
        this.updateOrte();
      }
    });
  }

  updateOrte(): void {
    const userModel = Object.assign(new User(), this.lokalSpeichern.getUserModel());
    //const userModel = this.lokalSpeichern.getUserModel();
    if (userModel.isPatient()) {
      this.getAllFuer(userModel.uuid, false);
    } else {
      this.getAllFuer(this.lokalSpeichern.getAktuellenPatient().uuid, false);
    }
  }
}
