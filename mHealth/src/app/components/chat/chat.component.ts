import {AfterViewChecked, Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {IonContent, NavController} from '@ionic/angular';
import {Message} from '../../models/message.model';
import {ChatService} from '../../services/chat.service';
import {ChatPartner} from '../../models/chat.partner.model';
import {CustomDateModel} from '../../models/customDate.model';


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent implements OnInit, AfterViewChecked {
  @ViewChild('content') content: IonContent;

  subscriptions: Subscription[] = [];
  nachrichten: Message[] = [];
  chatPartner: ChatPartner[] = [];
  empfaengerID = '';

  note = '';
  titel = '';

  constructor(private route: ActivatedRoute,
              private chatService: ChatService,
              private navCtrl: NavController,) {

    this.empfaengerID = this.route.snapshot.paramMap.get('uuid');
    this.titel = this.route.snapshot.paramMap.get('name');

    this.subscriptions.push(
      this.chatService.messagesList.subscribe(n => this.nachrichten = n),
      this.chatService.chatPartnerList.subscribe(c => this.chatPartner = c)
    );

    if (this.empfaengerID !== 'show') {
      this.chatService.getAllFrom(this.empfaengerID);
      this.chatService.markMessagesAsRead(this.empfaengerID);
    } else {
      this.chatService.getChatPartner();
    }

  }

  ngOnInit() {
  }

  formatDate(date: string): string {
    const customDateModel = new Object(date as unknown as CustomDateModel) as CustomDateModel;
    const customDate = customDateModel.date;
    const time = customDateModel.time;
    return customDate.day + '.' + customDate.month + '.' + customDate.year + ' ' + time.hour + ':' + time.minute + ':' + time.second;
  }

  isUebersicht(): boolean {
    return this.empfaengerID === 'show';
  }

  openChat(userID: string, titel: string): void {
    this.navCtrl.navigateForward(['/chat/' + userID + '/' + titel]);
  }

  isReceived(nachricht: Message): boolean {
    return nachricht.authorID === this.empfaengerID;
  }

  sendMessage(): void {
    this.chatService.add(this.note, this.empfaengerID);
    this.chatService.getAllFrom(this.empfaengerID);
    this.note = ' ';
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  ngAfterViewChecked(): void {
    if (!this.isUebersicht()) {
      this.scrollToBottom();
    }
  }

  scrollToBottom() {
    this.content.scrollToBottom();
  }

}
