import {Injectable} from '@angular/core';
import {Platform} from '@ionic/angular';

@Injectable({
  providedIn: 'root'
})
export class PlatformService {

  constructor(private platform: Platform) {
  }

  isApp(): boolean {
    const isAndroid = this.platform.is('android');
    const isIos = this.platform.is('ios');
    return isAndroid || isIos;
  }
}
