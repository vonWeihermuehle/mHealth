import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {Message} from '../models/message.model';
import {GeneralResponse} from '../models/generalResponse.model';
import {UrlService} from './url.service';
import {ChatPartner} from '../models/chat.partner.model';
import {LokalSpeichernService} from './lokalSpeichern.service';
import {Platform} from '@ionic/angular';
import { LocalNotifications } from '@ionic-native/local-notifications/ngx';
import {ToasterService} from './toaster.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  messagesList = new Subject<Message[]>();
  chatPartnerList = new Subject<ChatPartner[]>();

  constructor(
    private httpClient: HttpClient,
    private lokalSpeichern: LokalSpeichernService,
    private urlService: UrlService,
    private platform: Platform,
    private localNotifications: LocalNotifications,
    private toasterService: ToasterService) {
  }

  getAllFrom(uuid: string): void {
    let body = new HttpParams();
    body = body.set('authorID', uuid);

    this.httpClient.post<GeneralResponse<Message[]>>(this.urlService.getUrl('api/chat/get'), body).subscribe(
      data => {
        this.messagesList.next(data.data);
      }
    );
  }

  getChatPartner(): void {
    const body = new HttpParams();

    this.httpClient.post<GeneralResponse<ChatPartner[]>>(this.urlService.getUrl('api/chat/getPartner'), body).subscribe(
      data => {
        this.chatPartnerList.next(data.data);
      }
    );

  }

  add(nachricht: string, empfaengerId: string): void {
    let body = new HttpParams();
    body = body.set('nachricht', nachricht);
    body = body.set('empfaengerID', empfaengerId);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/chat/add'), body).subscribe(data => {
      if (data.success) {
        this.getAllFrom(empfaengerId);
      }
    });
  }

  markMessagesAsRead(empfaengerId: string): void {
    let body = new HttpParams();
    body = body.set('authorID', empfaengerId);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/chat/markAsUnread'), body).subscribe(data => {
      if (data.success) {
        this.getChatPartner();
      }
    });
  }

  checkForNewMessages(): void {
    const lastMessageCheckTime = this.lokalSpeichern.getLastMessageCheckTime();
    let body = new HttpParams();
    body = body.set('datetime', lastMessageCheckTime);

    this.httpClient.post<GeneralResponse<Response>>(this.urlService.getUrl('api/chat/check'), body).subscribe(data => {
      if(data.success)
      {
        if(Number(data.data.string) > 0)
        {
          if(this.isApp())
          {
            this.localNotifications.schedule({
              id: 1,
              text: 'Neue Nachricht',
              sound: this.isAndroid() ? 'file://sound.mp3': 'file://beep.caf',
            });
          }else{
            this.toasterService.toastWithDoneButton('', 'Neue Nachricht');
          }
          this.getChatPartner();
        }
      }
    });
  }

  private isApp(): boolean {
    return this.isAndroid() || this.platform.is('ios');
  }

  private isAndroid(): boolean {
    return this.platform.is('android');
  }

}

export interface Response{
  string: string;
}
