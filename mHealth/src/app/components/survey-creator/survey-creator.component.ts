/* eslint-disable no-console */
import {Component, OnInit} from '@angular/core';
import {SurveyService} from '../../services/survey.service';
import {FragebogenModel} from '../../models/fragebogen.model';
import {NavController} from '@ionic/angular';
import {ToasterService} from '../../services/toaster.service';
import {LokalSpeichernService} from '../../services/lokalSpeichern.service';


@Component({
  selector: 'app-survey-creator',
  templateUrl: './survey-creator.component.html',
  styleUrls: ['./survey-creator.component.scss'],
})
export class SurveyCreatorComponent implements OnInit {

  options = {};
  model = {};
  form = [];

  tmpBeschreibung = '';
  tmpTitel = '';

  constructor(private surveyService: SurveyService,
              private navCtrl: NavController,
              private toasterService: ToasterService,
              private lokalSpeichern: LokalSpeichernService) {
  }

  ngOnInit() {
    if(this.lokalSpeichern.getVorlage().trim().length > 5){
      this.form = JSON.parse(this.lokalSpeichern.getVorlage());
      this.lokalSpeichern.setVorlage('');
    }
  }

  onFormSubmit(value) {
    if (this.tmpBeschreibung.trim().length < 1 || this.tmpTitel.trim().length < 1) {
      this.toasterService.toast('Titel und Beschreibung dürfen nicht leer sein');
      return;
    }
    const fragebogen: FragebogenModel = {
      id: 0,
      titel: this.tmpTitel,
      beschreibung: this.tmpBeschreibung,
      json: JSON.stringify(this.form),
      author: ''
    };
    this.surveyService.addFragebogen(fragebogen);
    this.navCtrl.back();
  }

  onChange(value) {
    console.debug(value);
  }

  onChangeEvent(event: MouseEvent) {
    console.debug(event, event.toString(), JSON.stringify(event));

  }

  onValueChange(value: boolean) {
    console.debug(value);
  }

}
