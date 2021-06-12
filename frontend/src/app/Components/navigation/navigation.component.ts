import { Component, OnInit } from '@angular/core';
import { ApiService } from 'src/app/Service/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  public loggedType: string;
  constructor(public auth: ApiService, private route: Router) {

    if (this.auth.getAuthType() == null) {
      this.loggedType = "home";
     //this.loggedType = "customer";
    } else {
      if (this.auth.getAuthType() == "customer") {
        this.loggedType = "customer";
      } else if (this.auth.getAuthType() == "admin") {
        this.loggedType = "admin";
      }
    }
  }

  ngOnInit() {
    //console.log(this.auth.getAuthType());

  }
  logout() {
    this.loggedType = "home";
    this.auth.removeToken();
    this.auth.logout();
    this.route.navigate(['/home']);
  }

  // logout() {
  //   this.loggedType = "customer";
  //   this.auth.removeToken();
  //   this.auth.logout();
  //   this.route.navigate(['/home']);
  // }

}
