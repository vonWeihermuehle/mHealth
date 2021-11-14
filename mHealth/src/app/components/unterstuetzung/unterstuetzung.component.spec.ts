import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {IonicModule} from '@ionic/angular';

import {UnterstuetzungComponent} from './unterstuetzung.component';

describe('UnterstuetzungComponent', () => {
  let component: UnterstuetzungComponent;
  let fixture: ComponentFixture<UnterstuetzungComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [UnterstuetzungComponent],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(UnterstuetzungComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
