import {NgModule} from '@angular/core';
import {NoPreloading, RouterModule, Routes} from '@angular/router';
import {SucheComponent} from './components/suche/suche.component';
import {DatenComponent} from './components/daten/daten.component';
import {KontaktComponent} from './components/kontakt/kontakt.component';
import {SonstigesComponent} from './components/sonstiges/sonstiges.component';
import {WarnComponent} from './components/warn/warn.component';
import {UnterstuetzungComponent} from './components/unterstuetzung/unterstuetzung.component';
import {SurveyComponent} from './components/survey/survey.component';
import {SurveyCreatorComponent} from './components/survey-creator/survey-creator.component';
import {LoginComponent} from './components/login/login.component';
import {HomeComponent} from './home/home.component';
import {ChatComponent} from './components/chat/chat.component';
import {ZuweisenComponent} from './components/zuweisen/zuweisen.component';
import {SurveyDoComponent} from './components/survey-do/survey-do.component';
import {SurveyOldComponent} from './components/survey-old/survey-old.component';
import {DownloadComponent} from './components/download/download.component';

const routes: Routes = [
  //{path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'suche', component: SucheComponent},
  {path: 'kontakt', component: KontaktComponent},
  {path: 'daten', component: DatenComponent},
  {path: 'warn', component: WarnComponent},
  {path: 'unterstuetzung', component: UnterstuetzungComponent},
  {path: 'sonstiges', component: SonstigesComponent},
  {path: 'survey', component: SurveyComponent},
  {path: 'surveyCreator', component: SurveyCreatorComponent},
  {path: 'surveydo/:id', component: SurveyDoComponent},
  {path: 'zuweisen/:id', component: ZuweisenComponent},
  {path: 'survey-old', component: SurveyOldComponent},
  {path: 'chat/:uuid/:name', component: ChatComponent},
  {path: 'download', component: DownloadComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {preloadingStrategy: NoPreloading})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
