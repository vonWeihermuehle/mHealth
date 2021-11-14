import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Subject} from 'rxjs';
import {GeneralResponse} from '../models/generalResponse.model';
import {User} from '../models/user.model';
import {ToasterService} from './toaster.service';
import {LokalSpeichernService} from './lokalSpeichern.service';
import {UrlService} from './url.service';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  userModel: User | null = null;
  isUserLoggedIn = new Subject();
  didResetPasswordForUser = new Subject();
  doesEmailExist = new Subject<boolean>();
  getUserSubject = new Subject<User>();
  userList = new Subject<User[]>();
  user = new Subject<User>();
  private apiToken = this.lokalSpeichern.getToken() ?? '';

  constructor(
    private httpClient: HttpClient,
    private toaster: ToasterService,
    private router: Router,
    private lokalSpeichern: LokalSpeichernService,
    private urlService: UrlService) {
    if (this.apiToken !== '') {
      this.isUserLoggedIn.next(true);
    } else {
      this.isUserLoggedIn.next(false);
    }
  }

  login(username: string, password: string): void {
    let body = new HttpParams();
    body = body.set('username', username);
    body = body.set('passwort', password);

    this.httpClient.post<GeneralResponse<LoginResponseData>>(this.urlService.getUrl('api/user/login'), body)
      .subscribe(data => {
          if (data.success) {
            this.apiToken = data.data.string;
            this.lokalSpeichern.setToken(this.apiToken);
            this.isUserLoggedIn.next(true);
            this.router.navigateByUrl('/');
            this.updateUserData();
          } else {
            this.isUserLoggedIn.next(false);
            this.toaster.toast('Login war nicht erfolgreich!');
            this.router.navigateByUrl('/login');
          }
        },
        error => {
          console.error(error);
          this.isUserLoggedIn.next(false);
        });
  }

  async logout() {
    this.apiToken = '';
    this.lokalSpeichern.setToken('');
    this.isUserLoggedIn.next(false);
    this.router.navigateByUrl('/login');
  }

  getApiToken(): string {
    return this.apiToken;
  }

  isLoggedIn(): boolean {
    return this.apiToken !== '';
  }

  getUserData(): void {
    this.userModel = this.lokalSpeichern.getUserModel();
    if (this.userModel == null) {
      this.updateUserData();
    } else {
      this.user.next(this.userModel);
    }
  }

  updateUserData(): void {
    this.httpClient.get<GeneralResponse<User>>(this.urlService.getUrl('api/user/getUserData'))
      .subscribe(data => {
          if (data.success) {
            this.user.next(data.data as User);
            this.userModel = Object.assign(new User(), data.data);
            this.lokalSpeichern.setUserModel(this.userModel);
          }
        },
        error => {
          console.error(error);
        });
  }

  setLastCoordinates(lat: number, lng: number): void {
    let body = new HttpParams();
    body = body.set('lat', lat);
    body = body.set('lng', lng);
    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/user/setLastCoordinates'), body).subscribe(
      data => {
        // eslint-disable-next-line no-console
        console.debug(data);
      }
    );
  }

  async setSchwellwert(uuid: string, wert: number): Promise<GeneralResponse<any>> {
    try{
      let body = new HttpParams();
      body = body.set('userID', uuid);
      body = body.set('wert', wert);
      return this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/user/setSchwellwert'), body)
        .toPromise();
    }catch (error)
    {
      console.error(error);
    }
  }

  removeLastCoordinates(): void {
    let body = new HttpParams();
    body = body.set('lat', 999);
    body = body.set('lng', 999);
    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/user/setLastCoordinates'), body).subscribe(
      data => {
        // eslint-disable-next-line no-console
        console.debug(data);
      }
    );
  }

  registerTherapeut(therapeut: User): void {
    let body = new HttpParams();
    body = body.set('vorname', therapeut.vorname);
    body = body.set('nachname', therapeut.nachname);
    body = body.set('email', therapeut.email);
    body = body.set('username', therapeut.username);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/user/addTherapeut'), body).subscribe(
      data => {
        if(data.success)
        {
          this.toaster.toast('Therapeut erfolgreich angelegt');
        }else{
          this.handleRegisterError(data);
        }
      }
    );
  }

  registerPatient(patient: User): void {
    let body = new HttpParams();
    body = body.set('vorname', patient.vorname);
    body = body.set('nachname', patient.nachname);
    body = body.set('email', patient.email);
    body = body.set('username', patient.username);
    body = body.set('schwellwert', patient.schwellwert);

    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/user/addPatient'), body).subscribe(
      data => {
        if(data.success)
        {
          this.toaster.toast('Patient erfolgreich angelegt');
        }else{
          this.handleRegisterError(data);
        }
      }
    );
  }

  handleRegisterError(response: GeneralResponse<any>): void {
    if(response.message === 'Already used')
    {
      this.toaster.toastWithDoneButton('Fehler!','Username oder Email bereits vergeben');
    }
  }

  deleteOwnData(): void {
    const body = new HttpParams();
    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/user/removeOwnUserWithAllData'), body).subscribe(
      data => {
        if(data.success)
        {
          this.toaster.toast('Alle Daten wurden erfolgreich gelöscht');
          this.logout();
        }
      }
    );
  }

  changePassword(alt: string, neu: string): void {
    let body = new HttpParams();
    body = body.set('altesPasswort', alt);
    body = body.set('neuesPasswort', neu);
    this.httpClient.post<GeneralResponse<any>>(this.urlService.getUrl('api/user/changePassword'), body).subscribe(
      data => {
        if(data.success)
        {
          this.toaster.toast('Passwort erfolgreich geändert');
        }else{
          this.toaster.toast('Passwort konnte nicht geändert werden');
        }
      }
    );
  }

}

export interface LoginResponseData {
  // eslint-disable-next-line id-blacklist
  string: string;
}
