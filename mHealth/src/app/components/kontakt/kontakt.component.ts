import {Component, OnInit} from '@angular/core';
import {NavController} from '@ionic/angular';
import {LokalSpeichernService} from 'src/app/services/lokalSpeichern.service';
import {Kontakt} from '../../models/kontakt.model';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {KontakteService} from '../../services/kontakte.service';
import {Subscription} from 'rxjs';
import {User} from '../../models/user.model';
import {ToasterService} from '../../services/toaster.service';

@Component({
  selector: 'app-kontakt',
  templateUrl: './kontakt.component.html',
  styleUrls: ['./kontakt.component.scss'],
})
export class KontaktComponent implements OnInit {

  kontakte: Kontakt[] = [];
  subscriptions: Subscription[] = [];
  user: User | null = null;

  aktuellerKontakt: Kontakt = {
    id: 0,
    art: '',
    email: '',
    phone: '',
    name: '',
    userID: ''
  };

  constructor(private navCtrl: NavController,
              private lokalSpeichern: LokalSpeichernService,
              private modalService: NgbModal,
              private kontakteService: KontakteService,
              private toasterService: ToasterService) {

    const user = this.lokalSpeichern.getUserModel();
    if (user == null) {
      return;
    }
    this.user = Object.assign(new User(), user);
  }

  ngOnInit() {
    if (this.user.isPatient()) {
      this.kontakteService.getOwn();
    } else {
      if (this.lokalSpeichern.getAktuellenPatient() == null) {
        this.toasterService.toastWithDoneButton('Patient fehlt', 'Es muss ein Patient gewählt werden');
      } else {
        this.kontakteService.getAllFuer(this.lokalSpeichern.getAktuellenPatient().uuid);
      }
    }
    this.subscriptions.push(
      this.kontakteService.kontakteList.subscribe(k => this.kontakte = k),
    );
  }


  openFuerBearbeitung(content, id: number) {
    this.aktuellerKontakt = this.kontakte.filter(kontakt => kontakt.id === id)[0];
    this.openModal(content);
  }

  openFuerNeuenEintrag(content) {
    this.aktuellerKontakt = {id: 0, art: '', email: '', phone: '', name: '', userID: ''};
    this.openModal(content);
  }

  openModal(content) {
    this.modalService.open(content, {centered: true, size: 'xl', scrollable: true});
  }

  abschliesen() {
    if (this.aktuellerKontakt.id === 0) {
      this.kontakteService.addKontakt(this.aktuellerKontakt);
    } else {
      this.kontakteService.updateKontakt(this.aktuellerKontakt);
    }
    this.modalService.dismissAll();
  }

  delete(id: number) {
    this.kontakteService.removeKontakt(id);
    this.kontakteService.updateKontakte();
  }

  isValidEmail(email: string) {
    let re: RegExp;
    // eslint-disable-next-line max-len,prefer-const
    re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  getIconName(art: string): string{
    switch (art){
      case 'Therapeut': return 'person-circle-outline';
      case 'Bezugsperson': return 'person-outline';
      case 'Beratungsstelle': return 'business-outline';
      default: return 'person-outline';
    }
  }

  isArtTherapeut(art: string): boolean {
    return art === 'Therapeut';
  }

  gotoChat(uuid: string, name: string): void {
    this.navCtrl.navigateForward(['/chat/' + uuid + '/' + name]);
  }
}
