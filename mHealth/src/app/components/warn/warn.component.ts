import {Component, NgZone, OnInit} from '@angular/core';
import {LokalSpeichernService} from 'src/app/services/lokalSpeichern.service';
import {User} from '../../models/user.model';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Subscription} from 'rxjs';
import {Ort} from '../../models/ort.model';
import {OrteService} from '../../services/orte.service';
import {ToasterService} from '../../services/toaster.service';
import { Geolocation } from '@ionic-native/geolocation/ngx';


@Component({
  selector: 'app-warn',
  templateUrl: './warn.component.html',
  styleUrls: ['./warn.component.scss'],
})
export class WarnComponent implements OnInit {

  user: User | null = null;
  orte: Ort[] = [];
  subscriptions: Subscription[] = [];
  public isToggled: boolean;

  lat = 51.678418;
  lng = 7.809007;

  map: google.maps.Map;
  markers: Marker[] = [];

  aktuellerOrt: Ort = {
    id: 0,
    titel: '',
    beschreibung: '',
    patientUUID: '',
    lng: '',
    lat: ''
  };

  private mapClickListener: google.maps.MapsEventListener;

  constructor(private lokalSpeichern: LokalSpeichernService,
              private zone: NgZone,
              private modalService: NgbModal,
              private orteService: OrteService,
              private toasterService: ToasterService,
              private geolocation: Geolocation) {
    this.isToggled = lokalSpeichern.sollWarnen();
  }

  ngOnInit() {
    const user = this.lokalSpeichern.getUserModel();
    this.user = Object.assign(new User(), user);

    if (this.user.isPatient()) {
      this.orteService.getAllFuer(this.user.uuid, false);
    } else {
      if (this.lokalSpeichern.getAktuellenPatient() == null) {
        this.toasterService.toastWithDoneButton('Patient fehlt', 'Es muss ein Patient gewählt werden');
      } else {
        this.orteService.getAllFuer(this.lokalSpeichern.getAktuellenPatient().uuid, false);
      }
    }
    this.subscriptions.push(
      this.orteService.orteList.subscribe(k => this.orte = k),
    );

    this.setCoordinates();
  }

  setCoordinates(): void{
    this.geolocation.getCurrentPosition().then((resp)=> {
      this.lat = resp.coords.latitude;
      this.lng = resp.coords.longitude;
    }).catch((error: GeolocationPositionError) => {
      if (error.code === GeolocationPositionError.PERMISSION_DENIED)
      {
        this.toasterService.toast('Geolocation wurde vom Nutzer unterbunden');
      }
      console.error(error);
    });
  }

  public change() {
    this.isToggled = !this.isToggled;
    this.lokalSpeichern.setWarnFunktionAktiv(this.isToggled);
  }

  delete(ortId: number) {
    this.orteService.removeOrt(ortId);
  }

  openAddModal(content) {
    this.aktuellerOrt = {id: 0, lat: null, lng: null, patientUUID: null, beschreibung: '', titel: ''};
    this.openModal(content);
  }

  openUpdateModal(content, ortId: number) {
    this.aktuellerOrt = this.orte.filter(o => o.id === ortId)[0];
    this.markers = [];
    this.markers.push({
      lat: Number(this.aktuellerOrt.lat),
      lng: Number(this.aktuellerOrt.lng)
    });
    this.openModal(content);
  }

  openModal(content) {
    this.modalService.open(content, {centered: true, size: 'xl', scrollable: true});
  }

  public mapReadyHandler(map: google.maps.Map): void {
    this.map = map;
    this.mapClickListener = this.map.addListener('click', (e: google.maps.MouseEvent) => {
      this.zone.run(() => {
        this.markers = [];
        const latValue = e.latLng.lat();
        const lngValue = e.latLng.lng();
        this.markers.push({
          lat: latValue,
          lng: lngValue,
        });
        this.aktuellerOrt.lat = String(latValue);
        this.aktuellerOrt.lng = String(lngValue);
      });
    });
  }

  bestaetigen() {
    //this.modalController.dismiss();
    if (this.user.isPatient()) {
      this.aktuellerOrt.patientUUID = this.user.uuid;
    } else {
      this.aktuellerOrt.patientUUID = this.lokalSpeichern.getAktuellenPatient().uuid;
    }

    if(this.aktuellerOrt.id < 1){
      this.orteService.addOrt(this.aktuellerOrt);
    }else {
      this.orteService.updateOrt(this.aktuellerOrt);
    }
    this.modalService.dismissAll();
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy(): void {
    if (this.mapClickListener) {
      this.mapClickListener.remove();
    }
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}

export interface Marker {
  lat: number;
  lng: number;
}
