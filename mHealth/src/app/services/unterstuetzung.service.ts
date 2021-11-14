import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {GeneralResponse} from '../models/generalResponse.model';
import {LokalSpeichernService} from './lokalSpeichern.service';
import {UrlService} from './url.service';
import {Unterstuetzung} from '../models/unterstuetzung.model';


@Injectable({
  providedIn: 'root'
})
export class UnterstuetzungService {
  uebungenList = new Subject<Unterstuetzung[]>();

  constructor(
    private httpClient: HttpClient,
    private lokalSpeichern: LokalSpeichernService,
    private urlService: UrlService) {
  }

  getUebungenFuerPatient(uuid: string): void {
    let body = new HttpParams();
    body = body.set('uuid', uuid);

    this.httpClient.post<GeneralResponse<Unterstuetzung[]>>(this.urlService.getUrl('api/unterstuetzung/getUebungenFuerPatient'), body)
      .subscribe(data => {
          this.uebungenList.next(data.data);
        },
        error => {
          console.error(error);
        });
  }

  getUebungen(): void {
    this.httpClient.get<GeneralResponse<Unterstuetzung[]>>(this.urlService.getUrl('api/unterstuetzung/getUebungen'))
      .subscribe(data => {
          console.log(data);
          this.uebungenList.next(data.data);
        },
        error => {
          console.error(error);
        });
  }

  deleteUebung(id: number) {
    let body = new HttpParams();
    body = body.set('uebungID', id);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/unterstuetzung/delUebung'), body)
      .subscribe(data => {
          if (data.success) {
            this.updateUebungen();
          }
        },
        error => {
          console.error(error);
        });
  }

  addUebung(uebung: Unterstuetzung, uuid: string) {
    let body = new HttpParams();
    body = body.set('titel', uebung.titel);
    body = body.set('text', uebung.text);
    body = body.set('empfaenger', uuid);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/unterstuetzung/addUebung'), body)
      .subscribe(data => {
        if (data.success) {
          this.updateUebungen();
        }
      });
  }

  updateUebung(uebung: Unterstuetzung) {
    let body = new HttpParams();
    body = body.set('uebungID', uebung.id);
    body = body.set('titel', uebung.titel);
    body = body.set('text', uebung.text);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/unterstuetzung/updateUebung'), body)
      .subscribe(data => {
        if (data.success) {
          this.updateUebungen();
        }
      });
  }

  updateUebungen() {
    if (this.lokalSpeichern.getUserModel().therapeut) {
      this.getUebungenFuerPatient(this.lokalSpeichern.getAktuellenPatient().uuid);
    } else {
      this.getUebungen();
    }
  }

}

export interface LoginResponseData {
  // eslint-disable-next-line id-blacklist
  string: string;
}
