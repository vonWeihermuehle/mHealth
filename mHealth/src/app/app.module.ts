import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouteReuseStrategy} from '@angular/router';

import {IonicModule, IonicRouteStrategy} from '@ionic/angular';

import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {SucheComponent} from './components/suche/suche.component';
import {KontaktComponent} from './components/kontakt/kontakt.component';
import {WarnComponent} from './components/warn/warn.component';
import {UnterstuetzungComponent} from './components/unterstuetzung/unterstuetzung.component';
import {DatenComponent} from './components/daten/daten.component';
import {SonstigesComponent} from './components/sonstiges/sonstiges.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SurveyCreatorComponent} from './components/survey-creator/survey-creator.component';
import {SurveyComponent} from './components/survey/survey.component';
import {AngularEditorModule} from '@kolkov/angular-editor';
import {LoginComponent} from './components/login/login.component';
import {HomeComponent} from './home/home.component';
import {AuthInterceptor} from './auth.interceptor';
import {AgmCoreModule} from '@agm/core';
import {Geolocation} from '@ionic-native/geolocation/ngx';
import {ChatComponent} from './components/chat/chat.component';
import {DatePipe} from '@angular/common';
import {BackgroundMode} from '@ionic-native/background-mode/ngx';
import {LocalNotifications} from '@ionic-native/local-notifications/ngx';
import {NgxSurveyModule} from '../libs/ngx-surveys/src/lib';
import {LayoutModule} from '@angular/cdk/layout';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatTabsModule} from '@angular/material/tabs';
import {ZuweisenComponent} from './components/zuweisen/zuweisen.component';
import {SurveyDoComponent} from './components/survey-do/survey-do.component';
import {SurveyOldComponent} from './components/survey-old/survey-old.component';
import {DownloadComponent} from './components/download/download.component';

@NgModule({
  declarations: [AppComponent,
    HomeComponent,
    LoginComponent,
    SucheComponent,
    KontaktComponent,
    WarnComponent,
    UnterstuetzungComponent,
    DatenComponent,
    ChatComponent,
    SonstigesComponent,
    SurveyCreatorComponent,
    SurveyComponent,
    SurveyDoComponent,
    SurveyOldComponent,
    ZuweisenComponent,
    DownloadComponent
  ],
  entryComponents: [],
  imports: [
    BrowserModule,
    IonicModule.forRoot(),
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    FormsModule,
    AngularEditorModule,
    NgxSurveyModule,
    LayoutModule, MatToolbarModule, MatButtonModule, MatSidenavModule, MatIconModule, MatListModule, MatTabsModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyBBOorSihNw14JnNoQMyyBB5CRU9yNM5bk'
    })
  ],
  providers: [
    Geolocation,
    DatePipe,
    BackgroundMode,
    LocalNotifications,
    {
      provide: RouteReuseStrategy,
      useClass: IonicRouteStrategy
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent, SurveyCreatorComponent],
})


export class AppModule {
}

