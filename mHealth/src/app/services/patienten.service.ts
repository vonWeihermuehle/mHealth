import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {GeneralResponse} from '../models/generalResponse.model';
import {User} from '../models/user.model';
import {UrlService} from './url.service';
import {Subject} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class PatientenService {
  patientenList = new Subject<User[]>();

  constructor(
    private httpClient: HttpClient,
    private urlService: UrlService) {
  }

  getPatienten(): void {
    this.httpClient.get<GeneralResponse<User[]>>(this.urlService.getUrl('api/user/showPatienten')).subscribe(data => {
      if(data.success){
        this.patientenList.next(data.data);
      }
      console.log(data);
      }
    );
  }
}
