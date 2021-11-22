import {Injectable} from '@angular/core';
import {PlatformService} from './platform.service';

@Injectable({
  providedIn: 'root'
})
export class UrlService {

  constructor(private platformService: PlatformService) {}

  // eslint-disable-next-line @typescript-eslint/member-ordering
  port = '8083';

  getUrl(path: string): string {
    const protocol = window.location.protocol;
    let hostname = window.location.hostname;
    if(this.platformService.isApp()){
      hostname = '192.168.2.30';
    }
    // eslint-disable-next-line no-console
    console.debug(`${protocol}//${hostname}:${this.port}/${path}`);
    return `${protocol}//${hostname}:${this.port}/${path}`;
  }

}
