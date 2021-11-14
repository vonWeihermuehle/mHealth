import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {UrlService} from './url.service';
import {LokalSpeichernService} from './lokalSpeichern.service';
import {Ort} from '../models/ort.model';
import {Subscription} from 'rxjs';
import {OrteService} from './orte.service';
import {Geolocation} from '@ionic-native/geolocation/ngx';
import {ToasterService} from './toaster.service';
import {LocalNotifications} from '@ionic-native/local-notifications/ngx';
import {Platform} from '@ionic/angular';


@Injectable({
  providedIn: 'root'
})
export class WarnService {
  orteList: Ort[] = [];
  subscriptions: Subscription[] = [];
  lat = 51.678418;
  lng = 7.809007;

  private minKM = 3; //warnung ab 3 Kilometer Nähe zu bestimmten Orten


  constructor(
    private httpClient: HttpClient,
    private lokalSpeichern: LokalSpeichernService,
    private orteService: OrteService,
    private urlService: UrlService,
    private geolocation: Geolocation,
    private toasterService: ToasterService,
    private localNotifications: LocalNotifications,
    private platform: Platform,) {
  }

  warnIfTooNear(): void {
    this.orteService.getAllFuer(this.lokalSpeichern.getUserModel().uuid);
    if (this.subscriptions.length < 1) {
      this.subscriptions.push(
        this.orteService.orteList.subscribe(o => this.orteList = o)
      );
    }
    this.setCoordinates();
    if (this.checkIfTooNear()) {
      if (this.isApp()) {
        this.localNotifications.schedule({
          id: 1,
          text: 'Zu nah',
          sound: this.isAndroid() ? 'file://sound.mp3' : 'file://beep.caf',
        });
      } else {
        this.toasterService.toastWithDoneButton('', 'Zu nah');
      }
    }
  }

  checkIfTooNear(): boolean {
    const numbers = this.orteList
      .filter(o => this.berechneEntfernungInKm(o) < this.minKM);
    return numbers.length > 0;
  }

  berechneEntfernungInKm(ort: Ort): number {
    const lat = Number(ort.lat);
    const lng = Number(ort.lng);
    const dx = 71.5 * (this.lng - lng);
    const dy = 111.3 * (this.lat - lat);
    return Math.sqrt(((dx * dx) + (dy * dy)));
  }

  setCoordinates(): void {
    this.geolocation.getCurrentPosition().then((resp) => {
      this.lat = resp.coords.latitude;
      this.lng = resp.coords.longitude;
    }).catch((error: GeolocationPositionError) => {
      if (error.code === GeolocationPositionError.PERMISSION_DENIED) {
        this.toasterService.toast('Geolocation wurde vom Nutzer unterbunden');
      }
      console.error(error);
    });
  }

  private isApp(): boolean {
    return this.isAndroid() || this.platform.is('ios');
  }

  private isAndroid(): boolean {
    return this.platform.is('android');
  }

}
