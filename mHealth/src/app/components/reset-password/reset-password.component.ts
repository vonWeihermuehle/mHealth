import {Component, OnInit, Renderer2} from '@angular/core';
import {UserService} from 'src/app/services/user.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss'],
})
export class ResetPasswordComponent implements OnInit {

  email = '';

  // eslint-disable-next-line @typescript-eslint/ban-types
  globalListenFunc: Function;

  constructor(
    private userService: UserService,
    private renderer: Renderer2) {
  }

  ngOnInit() {
    this.globalListenFunc = this.renderer.listen('document', 'keypress', e => {
      if (e.keyCode === 13) {
        this.reset();
      }
    });
  }

  reset() {
    this.userService.resetPassword(this.email);
  }

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnDestroy() {
    this.globalListenFunc();
  }

}
