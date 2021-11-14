import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {GeneralResponse} from '../models/generalResponse.model';
import {UrlService} from './url.service';
import {LokalSpeichernService} from './lokalSpeichern.service';
import {ChatPartner} from '../models/chat.partner.model';
import {Subject} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class SucheService {

  kontakteList = new Subject<ChatPartner[]>();

  constructor(
    private httpClient: HttpClient,
    private lokalSpeichern: LokalSpeichernService,
    private urlService: UrlService) {
  }

  getAllNearby(): void {
    this.httpClient.get<GeneralResponse<ChatPartner[]>>(this.urlService.getUrl('api/user/showPatientenNearby')).subscribe(
      data => {
        this.kontakteList.next(data.data);
      }
    );

  }

}
