import { Component, OnInit } from '@angular/core';
import {PatientenService} from '../../services/patienten.service';
import {ToasterService} from '../../services/toaster.service';
import {SurveyService} from '../../services/survey.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {User} from '../../models/user.model';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FragebogenZuweisung} from '../../models/zuweisung.model';

@Component({
  selector: 'app-zuweisen',
  templateUrl: './zuweisen.component.html',
  styleUrls: ['./zuweisen.component.scss'],
})
export class ZuweisenComponent implements OnInit {

  fragebogenId: number;
  patientenList: User[] = [];
  zuweisungList: FragebogenZuweisung[] = [];
  zuweisungsListUsernames: string[] = [];

  subscriptions: Subscription[] = [];

  tmpPatient: User | null;
  tmpCron = '';
  tmpZuweisungID = 0;

  constructor(private patientenService: PatientenService,
              private toasterService: ToasterService,
              private surveyService: SurveyService,
              private route: ActivatedRoute,
              private modalService: NgbModal,) {

    this.fragebogenId = Number(this.route.snapshot.paramMap.get('id'));
  }

  ngOnInit() {
    this.surveyService.getZuweisungenFuerFragebogen(this.fragebogenId);
    this.patientenService.getPatienten();
    this.subscriptions.push(
      this.patientenService.patientenList.subscribe(p => this.patientenList = p),
      this.surveyService.zuweisungenList.subscribe(z => {
        this.zuweisungList = z;
        this.zuweisungsListUsernames = this.zuweisungList.map(u => u.empfaenger.username);
      })
    );
  }

  zuweisen(): void {
    this.surveyService.addZuweisung(this.fragebogenId, this.tmpPatient.uuid, this.tmpCron);
    this.modalService.dismissAll();
  }

  openZuweisungModal(zuweisenModal, uuid: string): void {
    this.setTmpPatient(uuid);
    this.modalService.open(zuweisenModal);
  }

  openDelete(deleteModal, uuid: string, zuweisungID: number): void {
    this.setTmpPatient(uuid);
    this.tmpZuweisungID = zuweisungID;
    this.modalService.open(deleteModal);
  }

  delete(): void {
    this.surveyService.delZuweisung(this.tmpZuweisungID, this.fragebogenId);
    this.tmpZuweisungID = 0;
    this.modalService.dismissAll();
  }

  setTmpPatient(uuid: string): void {
    this.tmpPatient = this.patientenList.filter(p => p.uuid === uuid)[0];
  }

  toogle(user: User): void {
    user.toggle = !user.toggle;
  }

  isInZuweisung(username: string): boolean {
    return this.zuweisungsListUsernames.includes(username);
  }

  getTurnusText(cron: string): string {
    if(cron === undefined || cron === null ||  cron === '' || Number(cron) < 1)
    {
      return 'einmalig abfragen';
    }else if(Number(cron) === 1) {
      return 'wird wöchentlich abgefragt';
    }else{
      return 'wird im ' + cron + ' Wochen Ryhtmus abgefragt';
    }
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
