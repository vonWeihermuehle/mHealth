import {Component, OnInit} from '@angular/core';
import {NavController} from '@ionic/angular';
import {LokalSpeichernService} from '../../services/lokalSpeichern.service';
import {User} from '../../models/user.model';


@Component({
  selector: 'app-daten',
  templateUrl: './daten.component.html',
  styleUrls: ['./daten.component.scss'],
})
export class DatenComponent implements OnInit {

  user: User = null;

  items = [
    {
      link: '/survey',
      title: 'Zu den Fragebögen',
      icon: 'contract-outline',
      description:
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit.',
    },
    {
      link: '/surveyCreator',
      title: 'Fragebogen erstellen',
      icon: 'chatbubbles-outline',
      description:
        'Vestibulum mollis enim et laoreet accumsan.',
    },
    {
      link: '/surveyCreator',
      title: 'Fragebogen aus Vorlage erstellen',
      icon: 'chatbubbles-outline',
      description:
        'Vestibulum mollis enim et laoreet accumsan.',
    },
    {
      link: '/surveyCreator',
      title: 'Fragebogen zuweisen',
      icon: 'chatbubbles-outline',
      description:
        'Vestibulum mollis enim et laoreet accumsan.',
    }
  ];

  constructor(private navCtrl: NavController,
              private lokalSpeichern: LokalSpeichernService) {
    this.user = Object.assign(new User(), this.lokalSpeichern.getUserModel());
  }

  ngOnInit() {

  }

  public openItem(link: string): void {
    this.navCtrl.navigateForward([link]);
  }

}
