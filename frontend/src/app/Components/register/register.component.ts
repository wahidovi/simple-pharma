import { Component, OnInit } from '@angular/core';
import { ApiService } from 'src/app/Service/api.service';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  public registerForm: any;
  constructor(public apiService: ApiService,
    private router: Router,
    public formBuilder: FormBuilder) {
    this.createForm();
  }

  ngOnInit() {
  }
  createForm() {
    this.registerForm = this.formBuilder.group({
      email: '',
      password: '',
      username: '',
      age: '',
      isEnabled: true,
      usertype: 'admin'
    });
  }
  register(): void {

    this.apiService.register(this.registerForm.value).
      subscribe(res => {
        if (res.status == "400") {
          console.log("Details cannot be empty");
        } else {
          this.router.navigate(['/login']);
        }
      },
        err => {
          alert("An error has occured, Please try again !!!");
        });
  }
}

