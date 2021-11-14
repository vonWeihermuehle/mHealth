import {Component, OnInit} from '@angular/core';
import {UserService} from 'src/app/services/user.service';
import {Router} from '@angular/router';
import {PatientenService} from '../../services/patienten.service';
import { Renderer2 } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  username = '';
  passwort = '';

  // eslint-disable-next-line @typescript-eslint/ban-types
  globalListenFunc: Function;


  constructor(
    private userService: UserService,
    private router: Router,
    private patientenService: PatientenService,
    private renderer: Renderer2) {
  }


  ngOnInit() {
    localStorage.removeItem('apiToken');
    this.userService.isUserLoggedIn.subscribe(isUserLoggedIn => {
      if (isUserLoggedIn) {
        this.router.navigateByUrl('/home');
      }
    });

    this.globalListenFunc = this.renderer.listen('document', 'keypress', e => {
      if(e.keyCode === 13){
        this.login();
      }
    });

  }

  login() {
    this.userService.login(this.username, this.passwort);
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy(){
    this.globalListenFunc();
  }

}
