import {Component, OnInit} from '@angular/core';
import {LokalSpeichernService} from '../../services/lokalSpeichern.service';
import {ActivatedRoute} from '@angular/router';
import {NavController} from '@ionic/angular';
import {SurveyService} from '../../services/survey.service';

@Component({
  selector: 'app-survey-do',
  templateUrl: './survey-do.component.html',
  styleUrls: ['./survey-do.component.scss'],
})
export class SurveyDoComponent implements OnInit {

  fragebogenId = 0;
  model = {};
  form = [];

  constructor(private navCtrl: NavController,
              private route: ActivatedRoute,
              private lokalSpeichern: LokalSpeichernService,
              private surveyService: SurveyService) {
    this.fragebogenId = Number(this.route.snapshot.paramMap.get('id'));
  }

  ngOnInit() {
    const vorlage = this.lokalSpeichern.getVorlage();
    if (vorlage.length > 5) {
      this.form = JSON.parse(vorlage);
    }
    this.lokalSpeichern.setVorlage('');
  }

  onFormSubmit(value) {
    this.surveyService.addAbgeschlossen(JSON.stringify(this.model), this.fragebogenId);
    this.navCtrl.navigateForward('/');
  }

}
