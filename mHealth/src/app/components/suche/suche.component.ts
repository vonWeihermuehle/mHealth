import {Component, OnInit} from '@angular/core';
import {SucheService} from '../../services/suche.service';
import {LokalSpeichernService} from '../../services/lokalSpeichern.service';
import {ChatPartner} from '../../models/chat.partner.model';
import {Subscription} from 'rxjs';
import {NavController} from '@ionic/angular';
import {UserService} from '../../services/user.service';
import {Geolocation} from '@ionic-native/geolocation/ngx';
import {ToasterService} from '../../services/toaster.service';

@Component({
  selector: 'app-suche',
  templateUrl: './suche.component.html',
  styleUrls: ['./suche.component.scss'],
})
export class SucheComponent implements OnInit {

  kontakte: ChatPartner[] = [];
  subscriptions: Subscription[] = [];
  public isToggled: boolean;

  constructor(private sucheService: SucheService,
              private lokalSpeichern: LokalSpeichernService,
              private navCtrl: NavController,
              private userService: UserService,
              private geolocation: Geolocation,
              private toasterService: ToasterService) {
    this.isToggled = lokalSpeichern.sollSuchen();
  }

  ngOnInit() {
    this.sucheService.getAllNearby();
    this.subscriptions.push(
      this.sucheService.kontakteList.subscribe(k => this.kontakte = k)
    );
  }

  startChat(uuid: string, name: string): void {
    this.navCtrl.navigateForward(['/chat/' + uuid + '/' + name]);
  }

  change() {
    this.isToggled = !this.isToggled;
    this.lokalSpeichern.setSuchFunktionAktiv(this.isToggled);
    if (!this.isToggled) {
      this.userService.removeLastCoordinates();
    } else {
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

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
