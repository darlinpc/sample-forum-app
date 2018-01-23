import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {User} from './user';

@Component({
  selector: 'app-nav',
  templateUrl: 'nav.template.html',
  styleUrls: ['./nav.component.css']
})

export class NavbarComponent implements OnInit {

  currentUser: User;
  users: User[] = [];

  constructor() {

  }

  ngOnInit() {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
  }

}
