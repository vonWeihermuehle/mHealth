import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {LokalSpeichernService} from './lokalSpeichern.service';
import {UrlService} from './url.service';
import {GeneralResponse} from '../models/generalResponse.model';
import {FragebogenAbgeschlossenModel, FragebogenModel} from '../models/fragebogen.model';
import {ToasterService} from './toaster.service';
import {FragebogenZuweisung} from '../models/zuweisung.model';

@Injectable({
  providedIn: 'root'
})
export class SurveyService {

  fragebogenList = new Subject<FragebogenModel[]>();
  zuweisungenList = new Subject<FragebogenZuweisung[]>();
  abgeschlossenList = new Subject<FragebogenAbgeschlossenModel[]>();
  id = 0;

  constructor(
    private httpClient: HttpClient,
    private lokalSpeichern: LokalSpeichernService,
    private urlService: UrlService,
    private toasterService: ToasterService) {

  }

  getOwn(): void {
    this.httpClient.get<GeneralResponse<FragebogenModel[]>>(this.urlService.getUrl('api/fragebogen/getOwn')).subscribe(
      data => {
        this.fragebogenList.next(data.data);
      }
    );
  }

  addFragebogen(fragebogen: FragebogenModel): void {
    let body = new HttpParams();
    body = body.set('titel', fragebogen.titel);
    body = body.set('beschreibung', fragebogen.beschreibung);
    body = body.set('json', fragebogen.json);
    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/fragebogen/addFragebogen'), body).subscribe(
      data => {
        this.handleSimpleResponse(data, 'Fragebogen gespeichert');
        if (data.success) {
          this.getOwn();
        }
      }
    );
  }

  delFragebogen(id: number): void {
    let body = new HttpParams();
    body = body.set('id', id);
    this.httpClient.post<GeneralResponse<FragebogenZuweisung[]>>(this.urlService.getUrl('api/fragebogen/delFragebogen'), body).subscribe(
      data => {
        this.handleSimpleResponse(data, 'Fragebogen gelöscht');
        if (data.success) {
          this.getOwn();
        }
      }
    );
  }

  getZuweisungenFuerFragebogen(id: number): void {
    let body = new HttpParams();
    body = body.set('id', id);
    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/fragebogen/getZuweisungenFuerFragebogen'), body).subscribe(
      data => {
        if (data.success) {
          this.zuweisungenList.next(data.data);
        }
      }
    );
  }

  addZuweisung(fragebogenId: number, empfaengerUUID: string, cron: string): void {
    let body = new HttpParams();
    body = body.set('id', fragebogenId);
    body = body.set('empfaengerID', empfaengerUUID);
    body = body.set('cron', cron);
    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/fragebogen/addZuweisung'), body).subscribe(
      data => {
        this.handleSimpleResponse(data, 'Zuweisung gespeichert!');
        if (data.success) {
          this.getZuweisungenFuerFragebogen(fragebogenId);
        }
      }
    );
  }

  delZuweisung(zuweisungId: number, fragebogenId: number): void {
    let body = new HttpParams();
    body = body.set('id', zuweisungId);
    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/fragebogen/delZuweisung'), body).subscribe(
      data => {
        this.handleSimpleResponse(data, 'Zuweisung gelöscht!');
        if (data.success) {
          this.getZuweisungenFuerFragebogen(fragebogenId);
        }
      }
    );
  }

  addAbgeschlossen(ergebnis: string, id: number): void {
    let body = new HttpParams();
    body = body.set('ergebnis', ergebnis);
    body = body.set('id', id);
    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/fragebogen/addAbgeschlossen'), body).subscribe(
      data => {
        this.handleSimpleResponse(data, 'Fragebogen erfolgreich gesendet!');
      }
    );
  }

  getAbgeschlossenFuer(uuid: string): void {
    let body = new HttpParams();
    body = body.set('uuid', uuid);
    this.httpClient.post<GeneralResponse<FragebogenAbgeschlossenModel[]>>(this.urlService.getUrl('api/fragebogen/getAbgeschlossenFuer'), body).subscribe(
      data => {
        if (data.success) {
          this.abgeschlossenList.next(data.data);
        }
      }
    );
  }

  handleSimpleResponse(data: GeneralResponse<any>, onSuccessMessage: string) {
    if (data.success) {
      this.toasterService.toast(onSuccessMessage);
    } else {
      this.toasterService.toast('Etwas ist schiefgelaufen');
    }
  }


}
