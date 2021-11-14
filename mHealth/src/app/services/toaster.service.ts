import {Injectable} from '@angular/core';
import {ToastController} from '@ionic/angular';

@Injectable({
  providedIn: 'root'
})
export class ToasterService {

  private duration = 2000;

  constructor(private toastController: ToastController) {
  }

  async toast(msg: string) {
    const toast = await this.toastController.create({
      message: msg,
      duration: this.duration
    });
    await toast.present();
  }

  async toastWithOptions(header: string, msg: string) {
    const toast = await this.toastController.create({
      header,
      message: msg,
      position: 'bottom',
      buttons: [
        {
          side: 'start',
          icon: 'star',
          text: 'Favorite',
          handler: () => {
          }
        }, {
          text: 'Done',
          role: 'cancel',
          handler: () => {
          }
        }
      ]
    });
    await toast.present();
    //const {role} = await toast.onDidDismiss();
  }

  async toastWithDoneButton(header: string, msg: string) {
    const toast = await this.toastController.create({
      header,
      message: msg,
      position: 'bottom',
      buttons: [
        {
          text: 'OK',
          role: 'cancel',
          handler: () => {
          }
        }
      ]
    });
    await toast.present();
  }

}
