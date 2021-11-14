import {Component, OnInit} from '@angular/core';
import {StylesManager} from 'survey-angular';
import {SurveyService} from '../../services/survey.service';
import {Subscription} from 'rxjs';
import {FragebogenModel} from '../../models/fragebogen.model';
import {ToasterService} from '../../services/toaster.service';
import {LokalSpeichernService} from '../../services/lokalSpeichern.service';
import {User} from '../../models/user.model';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NavController} from '@ionic/angular';

StylesManager.applyTheme('bootstrap');

@Component({
  selector: 'app-survey',
  templateUrl: './survey.component.html',
  styleUrls: ['./survey.component.scss'],
})
export class SurveyComponent implements OnInit {

  model = {};
  form = [];
  titel = '';
  beschreibung = '';
  id = 0;
  tmpID = 0;
  subscriptions: Subscription[] = [];
  fragebogenList: FragebogenModel[] = [];
  aktuellerFragebogen: FragebogenModel = {id: 0, titel: '', beschreibung: '', author: '', json: ''};
  user: User = null;

  constructor(private surveyService: SurveyService,
              private toasterService: ToasterService,
              private lokalSpeichern: LokalSpeichernService,
              private modalService: NgbModal,
              private navCtrl: NavController) {
    this.user = Object.assign(new User(), this.lokalSpeichern.getUserModel());
  }


  ngOnInit() {
    this.surveyService.getOwn();
    this.subscriptions.push(
      this.surveyService.fragebogenList.subscribe(f => this.fragebogenList = f),
    );
  }

  open(content, id: number): void {
    if (this.user?.isPatient()) {
      this.aktuellerFragebogen = this.fragebogenList.filter(f => f.id === id)[0];
      this.lokalSpeichern.setVorlage(this.aktuellerFragebogen.json);
      this.navCtrl.navigateForward('surveydo/' + id);
    } else {
      this.openModal(content, id);
    }
  }

  zeigeVorschau(vorschau): void {
    this.modalService.dismissAll();
    const json = this.fragebogenList.filter(f => f.id === this.tmpID)[0].json;
    this.form = JSON.parse(json);
    this.modalService.open(vorschau);
  }

  openZuweisen(): void {
    this.modalService.dismissAll();
    this.navCtrl.navigateForward('/zuweisen/' + this.tmpID);
  }

  erstelleNeuAusVorlage(): void {
    const json = this.fragebogenList.filter(f => f.id === this.tmpID)[0].json;
    this.lokalSpeichern.setVorlage(json);
    this.modalService.dismissAll();
    this.navCtrl.navigateForward('/surveyCreator');
  }

  openModal(content, id: number): void {
    this.tmpID = id;
    this.modalService.open(content, {centered: true, size: 'xl', scrollable: true});
  }

  delete(id: number): void {
    this.surveyService.delFragebogen(id);
    this.surveyService.getOwn();
    this.modalService.dismissAll();
  }

  onFormSubmit(value) {
    alert(JSON.stringify(value, null, 2));
  }

  isShow(): boolean {
    return this.id === 0;
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
