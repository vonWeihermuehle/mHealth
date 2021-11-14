import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../models/user.model';
import {Subscription} from 'rxjs';
import {LokalSpeichernService} from '../../services/lokalSpeichern.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ToasterService} from '../../services/toaster.service';
import {PatientenService} from '../../services/patienten.service';

@Component({
  selector: 'app-sonstiges',
  templateUrl: './sonstiges.component.html',
  styleUrls: ['./sonstiges.component.scss'],
})
export class SonstigesComponent implements OnInit {

  user: User | null = null;
  patientenList: User[] = [];
  subscriptions: Subscription[] = [];
  tmpUser: User = new User();
  art = '';
  altesPasswort = '';
  neuesPasswort = '';
  neuesPasswort2 = '';

  constructor(private userService: UserService,
              private lokalSpeichern: LokalSpeichernService,
              private modalService: NgbModal,
              private toasterService: ToasterService,
              private patientenSerivce: PatientenService) {
    const user = this.lokalSpeichern.getUserModel();
    if (user == null) {
      return;
    }
    this.user = Object.assign(new User(), user);
  }

  ngOnInit() {
    this.patientenSerivce.getPatienten();
    this.subscriptions.push(
      this.patientenSerivce.patientenList.subscribe(p => this.patientenList = p)
    );
  }

  setSchwellwert(user: User) {
    this.userService.setSchwellwert(user.uuid, user.schwellwert).then(response => {
      if (response.success) {
        this.patientenSerivce.getPatienten();
      }
      this.modalService.dismissAll();
    });
  }

  openModal(content, art: string) {
    this.art = art;
    this.modalService.open(content, {centered: true, size: 'xl', scrollable: true});
  }

  datenLoeschen(): void {
    this.modalService.dismissAll();
    this.userService.deleteOwnData();
  }

  register(): void {
    if (this.art === 'Patient') {
      this.userService.registerPatient(this.tmpUser);
    }
    if (this.art === 'Therapeut') {
      this.userService.registerTherapeut(this.tmpUser);
    }
    this.tmpUser = new User();
    this.art = '';
    this.modalService.dismissAll();
  }

  changePassword(): void {
    if ((this.neuesPasswort2 !== this.neuesPasswort) || (this.neuesPasswort.trim().length < 1) || (this.altesPasswort.trim().length < 1)) {
      this.toasterService.toast('Passwörter stimmen nicht überein');
      return;
    }
    this.userService.changePassword(this.altesPasswort, this.neuesPasswort);
    this.altesPasswort = '';
    this.neuesPasswort = '';
    this.neuesPasswort2 = '';
    this.modalService.dismissAll();
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
