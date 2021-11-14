import {Component, OnInit} from '@angular/core';
import {ModalController} from '@ionic/angular';
import {Unterstuetzung} from 'src/app/models/unterstuetzung.model';
import {UnterstuetzungService} from '../../services/unterstuetzung.service';
import {LokalSpeichernService} from '../../services/lokalSpeichern.service';
import {Subscription} from 'rxjs';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AngularEditorConfig} from '@kolkov/angular-editor';
import {User} from '../../models/user.model';
import {ToasterService} from '../../services/toaster.service';

@Component({
  selector: 'app-unterstuetzung',
  templateUrl: './unterstuetzung.component.html',
  styleUrls: ['./unterstuetzung.component.scss'],
})
export class UnterstuetzungComponent implements OnInit {

  massnahme: Unterstuetzung = null;
  uebungen: Unterstuetzung[] = [];
  subscriptions: Subscription[] = [];
  user: User | null = null;
  aktuelleUnterstuetzung: Unterstuetzung = {
    id: 0,
    titel: '',
    text: '',
  };

  editorConfig: AngularEditorConfig = {
    editable: true,
    spellcheck: true,
    height: 'auto',
    minHeight: '250',
    maxHeight: 'auto',
    width: 'auto',
    minWidth: '0',
    translate: 'yes',
    enableToolbar: true,
    showToolbar: true,
    placeholder: 'Enter text here...',
    defaultParagraphSeparator: '',
    defaultFontName: '',
    defaultFontSize: '',
    uploadWithCredentials: false,
    sanitize: true,
    toolbarPosition: 'top',
  };

  constructor(private modalController: ModalController,
              private unterstuetzungService: UnterstuetzungService,
              private lokalSpeichern: LokalSpeichernService,
              private modalService: NgbModal,
              private toasterService: ToasterService) {
  }

  ngOnInit() {
    this.user = Object.assign(new User(), this.lokalSpeichern.getUserModel());
    this.getUebungen();
    this.subscriptions.push(
      this.unterstuetzungService.uebungenList.subscribe(u => this.uebungen = u),
    );
  }

  openFuerNeu(content){
    this.aktuelleUnterstuetzung = {id: 0, text: '', titel: ''};
    this.openAddModal(content);
  }

  openFuerBearbeitung(content, id: number){
    this.aktuelleUnterstuetzung = this.uebungen.filter(u => u.id === id)[0];
    this.openAddModal(content);
  }

  openFuerAnzeige(content, id: number){
    this.aktuelleUnterstuetzung = this.uebungen.filter(u => u.id === id)[0];
    this.openAddModal(content);
  }

  openAddModal(content) {
    this.modalService.open(content, {centered: true, size: 'xl', scrollable: true});
  }

  abschliesen(){
    if(this.user.isPatient()){
      this.modalService.dismissAll();
      return;
    }
    if(this.aktuelleUnterstuetzung.id !== 0){
      this.unterstuetzungService.updateUebung(this.aktuelleUnterstuetzung);
    }else{
      this.unterstuetzungService.addUebung(this.aktuelleUnterstuetzung, this.lokalSpeichern.getAktuellenPatient().uuid);
    }

    this.modalService.dismissAll();
  }

  delete(id: number) {
    this.unterstuetzungService.deleteUebung(id);
  }

  getUebungen() {
    if(this.user?.isTherapeut()){
      if (this.lokalSpeichern.getAktuellenPatient() == null) {
        this.toasterService.toastWithDoneButton('Patient fehlt', 'Es muss ein Patient gewählt werden');
      } else {
        this.unterstuetzungService.getUebungenFuerPatient(this.lokalSpeichern.getAktuellenPatient().uuid);
      }
    }else{
      this.unterstuetzungService.getUebungen();
    }
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
