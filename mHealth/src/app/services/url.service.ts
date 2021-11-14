import {Injectable} from '@angular/core';
import { Platform } from '@ionic/angular';

@Injectable({
  providedIn: 'root'
})
export class UrlService {

  constructor(private platform: Platform) {
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  port = '8083';

  getUrl(path: string): string {
    const protocol = window.location.protocol;
    let hostname = window.location.hostname;
    if(this.isApp()){
      hostname = '192.168.2.30';
    }
    // eslint-disable-next-line no-console
    console.debug(`${protocol}//${hostname}:${this.port}/${path}`);
    return `${protocol}//${hostname}:${this.port}/${path}`;
  }

  isApp(): boolean {
    return this.platform.is('android') || this.platform.is('ios');
  }
}
