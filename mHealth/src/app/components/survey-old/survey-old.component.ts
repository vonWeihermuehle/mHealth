import {Component, OnInit} from '@angular/core';
import {User} from '../../models/user.model';
import {LokalSpeichernService} from '../../services/lokalSpeichern.service';
import {PatientenService} from '../../services/patienten.service';
import {Subscription} from 'rxjs';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FragebogenAbgeschlossenModel} from '../../models/fragebogen.model';
import {SurveyService} from '../../services/survey.service';
import {CustomDate} from '../../models/customDate.model';

@Component({
  selector: 'app-survey-old',
  templateUrl: './survey-old.component.html',
  styleUrls: ['./survey-old.component.scss'],
})
export class SurveyOldComponent implements OnInit {

  subscriptions: Subscription[] = [];
  patientenSubscription: Subscription;
  patienten: User[] = [];
  aktuellerPatient: User | null = null;
  abgeschlossene: FragebogenAbgeschlossenModel[] = [];
  fragen: string[] = [];


  constructor(private lokalSpeichern: LokalSpeichernService,
              private patientenService: PatientenService,
              private modalService: NgbModal,
              private surveyService: SurveyService) {
  }

  ngOnInit() {
    this.patientenService.getPatienten();
    this.subscriptions.push(
      this.patientenSubscription = this.patientenService.patientenList.subscribe(p => this.patienten = p),
      this.surveyService.abgeschlossenList.subscribe(a => this.abgeschlossene = a)
    );
  }

  formatDate(date: string): string {
    const customDate = new Object(date as unknown as CustomDate) as CustomDate;
    return customDate.day + '.' + customDate.month + '.' + customDate.year;
  }

  show(show, id: number) {
    const text = this.abgeschlossene.filter(a => a.id === id)[0].json;
    this.fragen = this.prepareText(text);
    this.modalService.open(show);
  }

  prepareText(text: string): string[] {
    const lines = text.split(',');
    let i = 0;
    while (i < lines.length) {
      lines[i] = this.replaceUnusedChar(lines[i]);
      i++;
    }
    return lines;
  }

  replaceUnusedChar(text: string): string {
    return text
      .replace(',', '')
      .replace('\"', '')
      .replace('"', '')
      .replace('"', '')
      .replace('"', '')
      .replace('}', '')
      .replace('{', '');
  }

  openPatientenAuswahl(content) {
    if (!this.subscriptions.includes(this.patientenSubscription)) {
      this.patientenService.getPatienten();
      this.subscriptions.push(
        this.patientenSubscription = this.patientenService.patientenList.subscribe(p => this.patienten = p),
      );
    }
    this.modalService.open(content);
  }

  selectPatient(patient: User) {
    this.aktuellerPatient = patient;
    this.modalService.dismissAll();
    this.surveyService.getAbgeschlossenFuer(patient.uuid);
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
