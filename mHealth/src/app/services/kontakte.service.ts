import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {GeneralResponse} from '../models/generalResponse.model';
import {UrlService} from './url.service';
import {LokalSpeichernService} from './lokalSpeichern.service';
import {Kontakt} from '../models/kontakt.model';
import {Subject} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class KontakteService {
  kontakteList = new Subject<Kontakt[]>();

  constructor(
    private httpClient: HttpClient,
    private lokalSpeichern: LokalSpeichernService,
    private urlService: UrlService) {
  }

  getAllFuer(uuid: string): void {
    let body = new HttpParams();
    body = body.set('userID', uuid);

    this.httpClient.post<GeneralResponse<Kontakt[]>>(this.urlService.getUrl('api/kontakte/getAllFuer'), body).subscribe(
      data => {
        this.kontakteList.next(data.data);
      }
    );
  }

  getOwn(): void {
    this.httpClient.get<GeneralResponse<Kontakt[]>>(this.urlService.getUrl('api/kontakte/getOwn')).subscribe(
      data => {
        this.kontakteList.next(data.data);
      }
    );
  }

  addKontakt(kontakt: Kontakt): void {
    let body = new HttpParams();
    body = body.set('name', kontakt.name);
    body = body.set('art', kontakt.art);
    body = body.set('email', kontakt.email);
    body = body.set('phone', kontakt.phone);
    body = body.set('userID', this.lokalSpeichern.getAktuellenPatient().uuid);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/kontakte/add'), body).subscribe(data => {
      if (data.success) {
        this.updateKontakte();
      }
    });
  }

  removeKontakt(id: number): void {
    let body = new HttpParams();
    body = body.set('id', id);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/kontakte/remove'), body).subscribe(data => {
      if (data.success) {
        this.updateKontakte();
      }
    });
  }

  updateKontakt(kontakt: Kontakt): void {
    let body = new HttpParams();
    body = body.set('id', kontakt.id);
    body = body.set('name', kontakt.name);
    body = body.set('art', kontakt.art);
    body = body.set('email', kontakt.email);
    body = body.set('phone', kontakt.phone);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/kontakte/update'), body).subscribe(data => {
      if (data.success) {
        this.updateKontakte();
      }
    });
  }

  updateKontakte(): void {
    if (this.lokalSpeichern.getUserModel().therapeut) {
      this.getAllFuer(this.lokalSpeichern.getAktuellenPatient().uuid);
    } else {
      this.getOwn();
    }
  }
}
