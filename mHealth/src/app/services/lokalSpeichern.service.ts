import {Injectable} from '@angular/core';
import {User} from '../models/user.model';
import {ToasterService} from './toaster.service';
import { DatePipe } from '@angular/common';

@Injectable({providedIn: 'root'})
export class LokalSpeichernService {

  private apitokenFlag = 'apiToken';
  private warnfunktionFlag = 'warnFunktion';
  private kontakteFlag = 'kontakte';
  private usermodelFlag = 'user.model';
  private aktuellerPatientFlag = 'aktuellerPatient';
  private lastMessageCheckFlag = 'last.time.checked.for.Messages';
  private suchFunktionFlag = 'suchfunktion.aktiv';
  private vorlageFlag = 'tmp.Vorlage';

  constructor(private toasterService: ToasterService,
              private datepipe: DatePipe) {
  }

  public getToken(): string {
    return localStorage.getItem(this.apitokenFlag) ?? '';
  }

  public setToken(token: string): void {
    localStorage.setItem(this.apitokenFlag, token);
  }

  public getVorlage(): string {
    return localStorage.getItem(this.vorlageFlag) ?? '';
  }

  public setVorlage(vorlage: string): void {
    localStorage.setItem(this.vorlageFlag, vorlage);
  }

  public sollWarnen(): boolean {
    const item = localStorage.getItem(this.warnfunktionFlag);
    return item === 'true';
  }

  public setWarnFunktionAktiv(aktiv: boolean): void {
    let item = 'false';
    if (aktiv) {
      item = 'true';
    }
    localStorage.setItem(this.warnfunktionFlag, item);
    this.toasterService.toast('Einstellung gespeichert!');
  }

  public sollSuchen(): boolean {
    const item = localStorage.getItem(this.suchFunktionFlag);
    return item === 'true';
  }

  public setSuchFunktionAktiv(aktiv: boolean): void {
    let item = 'false';
    if (aktiv) {
      item = 'true';
    }
    localStorage.setItem(this.suchFunktionFlag, item);
    this.toasterService.toast('Einstellung gespeichert!');
  }

  public setKontakte(json: string) {
    localStorage.setItem(this.kontakteFlag, json);
    this.toasterService.toast('Kontakte aktualsiert');
  }

  public getKontakt(): string {
    return localStorage.getItem(this.kontakteFlag);
  }

  public setUserModel(user: User) {
    localStorage.setItem(this.usermodelFlag, JSON.stringify(user));
  }

  public getUserModel(): User {
    const user = localStorage.getItem(this.usermodelFlag);
    if(user == null){
      return null;
    }
    return JSON.parse(user);
  }

  public removeUserModal(): void{
    localStorage.removeItem(this.usermodelFlag);
  }

  public setAktuellerPatient(patient: User): void {
    localStorage.setItem(this.aktuellerPatientFlag, JSON.stringify(patient));
  }

  public getAktuellenPatient(): User {
    const user = localStorage.getItem(this.aktuellerPatientFlag);
    if(user == null){
      return null;
    }
    return JSON.parse(user);
  }

  public removeAktuellerPatient(): void{
    localStorage.removeItem(this.aktuellerPatientFlag);
  }

  public getLastMessageCheckTime(): string {
    let item = localStorage.getItem(this.lastMessageCheckFlag);
    if(item === undefined || item === null)
    {
      item = this.getNowAsString();
    }
    this.setLastMessageCheckTime();
    return item;
  }

  getNowAsString() {
    return this.datepipe.transform(new Date(), 'yyyy-MM-dd HH:mm');
  }

  private setLastMessageCheckTime(): void{
    const s = this.getNowAsString();
    localStorage.setItem(this.lastMessageCheckFlag, s);
  }
}
