import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ModalController, NavController} from '@ionic/angular';
import {interval, Subscription} from 'rxjs';
import {User} from '../models/user.model';
import {UserService} from '../services/user.service';
import {LokalSpeichernService} from '../services/lokalSpeichern.service';
import {PatientenService} from '../services/patienten.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ToasterService} from '../services/toaster.service';
import {ChatService} from '../services/chat.service';
import {BackgroundMode} from '@ionic-native/background-mode/ngx';
import {takeWhile} from 'rxjs/operators';
import {WarnService} from '../services/warn.service';
import {Geolocation} from '@ionic-native/geolocation/ngx';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  user: User | null = null;
  aktuellerPatient: User | null = null;
  patienten: User[] = [];
  subscriptions: Subscription[] = [];
  patientenSubscription: Subscription;

  private messageCheckIntervall: number = 5 * 60 * 1000; //5 Minuten
  private warnServiceIntervall: number = 5 * 60 * 1000; //5 Minuten

  constructor(
    private navCtrl: NavController,
    private userService: UserService,
    private router: Router,
    private modalController: ModalController,
    private lokalSpeichern: LokalSpeichernService,
    private patientenService: PatientenService,
    private modalService: NgbModal,
    private toasterService: ToasterService,
    private chatService: ChatService,
    private backgroundMode: BackgroundMode,
    private warnSerivce: WarnService,
    private geolocation: Geolocation
  ) {
    if (!this.userService.isLoggedIn()) {
      this.router.navigateByUrl('/login');
    }
    this.userService.user.subscribe({
      next: (user: User) => {
        this.user = Object.assign(new User(), user);
      }
    });
    this.userService.getUserData();
  }

  ngOnInit() {
    if (this.user?.isTherapeut()) {
      this.patientenService.getPatienten();
      this.subscriptions.push(
        this.patientenSubscription = this.patientenService.patientenList.subscribe(p => this.patienten = p),
      );
      this.aktuellerPatient = this.lokalSpeichern.getAktuellenPatient();
    }

    this.backgroundMode.enable();
    this.startBackgroundTasks();
    if (this.lokalSpeichern.sollSuchen()) {
      this.setLastCoordinates();
    }
  }

  setLastCoordinates(): void {
    this.geolocation.getCurrentPosition().then((resp) => {
      const lat = resp.coords.latitude;
      const lng = resp.coords.longitude;
      this.userService.setLastCoordinates(lat, lng);
    }).catch((error: GeolocationPositionError) => {
      if (error.code === GeolocationPositionError.PERMISSION_DENIED) {
        this.toasterService.toast('Geolocation wurde vom Nutzer unterbunden');
      }
      console.error(error);
    });
  }

  startBackgroundTasks(): void {
    this.startMessageCheckTask();
    this.startWarnServiceTask();
  }

  startMessageCheckTask(): void {
    interval(this.messageCheckIntervall)
      .pipe(takeWhile(() => true))
      .subscribe(() => {
        this.chatService.checkForNewMessages();
      });
  }

  startWarnServiceTask(): void {
    interval(this.warnServiceIntervall)
      .pipe(takeWhile(() => true))
      .subscribe(() => {
        if (this.user.isPatient() && this.lokalSpeichern.sollWarnen()) {
          this.warnSerivce.warnIfTooNear();
        }
      });
  }

  public openItemIfAktuellerPatientgesetzt(link: string): void {
    if (this.aktuellerPatient !== null || this.user.isPatient()) {
      this.openItem(link);
    } else {
      this.toasterService.toast('Es muss ein Patient gewählt werden');
    }
  }

  public openItem(link: string): void {
    this.navCtrl.navigateForward([link]);
  }

  logout() {
    this.userService.logout();
    this.router.navigateByUrl('login');
    this.lokalSpeichern.removeUserModal();
    this.lokalSpeichern.removeAktuellerPatient();
    this.aktuellerPatient = null;
  }

  selectPatient(patient: User) {
    this.aktuellerPatient = patient;
    this.lokalSpeichern.setAktuellerPatient(patient);
    this.modalService.dismissAll();
  }

  openPatientenAuswahl(content) {
    this.patientenService.getPatienten();
    if (!this.subscriptions.includes(this.patientenSubscription)) {
      this.subscriptions.push(
        this.patientenSubscription = this.patientenService.patientenList.subscribe(p => this.patienten = p),
      );
    }
    this.modalService.open(content);
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
