import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule, Route} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {NavbarComponent} from './nav/nav.component';
import {HomeComponent} from './home/home.component';
import {AboutComponent} from './about/about.component';
import {ArticleComponent} from './article/article.component';

import {HomeService} from './home/home.service';
import {AuthenticationService} from './login/authentication.service';
import {AlertService} from './login/alert.service';
import {UserService} from './nav/user.service';
import {LoginComponent} from './login/login.component';
import {EditorComponent} from './editor/editor.component';
import {ArticleService} from './editor/article.service';
import {FormBuilder} from '@angular/forms';
import { FroalaEditorModule, FroalaViewModule } from 'angular-froala-wysiwyg';

const ROUTES: Route[] = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: 'about', component: AboutComponent},
  {path: 'post/:id', component: ArticleComponent},
  {path: 'editor', component: EditorComponent},
  {path: 'editor/:id', component: EditorComponent}
];

@NgModule({
  imports: [BrowserModule,
    FormsModule,
    RouterModule.forRoot(ROUTES),
    HttpClientModule,
    FroalaEditorModule.forRoot(), FroalaViewModule.forRoot()
  ],
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    AboutComponent,
    ArticleComponent,
    LoginComponent,
    EditorComponent,
  ],
  providers: [HomeService, AuthenticationService, AlertService, UserService, ArticleService, FormBuilder],
  bootstrap: [AppComponent]
})
export class AppModule {}
