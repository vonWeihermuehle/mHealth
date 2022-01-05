import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs-compat/Observable';
import {UserService} from './services/user.service';
import {tap} from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private userService: UserService) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (this.userService.isLoggedIn()) {
      request = request.clone({
        setHeaders: {
          token: this.userService.getApiToken(),
          host: window.location.hostname
        }
      });
    }
    return next.handle(request).pipe(tap(evt => {
      if (evt instanceof HttpResponse) {
        const noPermissionResponse = (evt.body as NoPermissionResponse);
        if (noPermissionResponse.message === 'No Permission') {
          this.userService.logout();
        }
      }
    }));
  }
}

export interface NoPermissionResponse {
  success: boolean;
  message: string;
}
