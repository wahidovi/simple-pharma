// import { NgModule } from '@angular/core';
// import { Routes, RouterModule } from '@angular/router';
// import { AdminComponent } from './Components/admin/admin.component';
// import { EditItemComponent } from './Components/admin/edit-item/edit-item.component';
// import { OrderItemComponent } from './Components/admin/order-item/order-item.component';
// import { AddressComponent } from './Components/home/address/address.component';
// import { CartItemComponent } from './Components/home/cart-item/cart-item.component';
// import { HomeComponent } from './Components/home/home.component';
// import { LoginComponent } from './Components/login/login.component';
// import { RegisterComponent } from './Components/register/register.component';
// import { AuthguardGuard } from './Service/authguard.guard';

// const routes: Routes = [
//   { path: '',
//     redirectTo: '/login',
//     pathMatch: 'full'
//   },
// {
//   path:'login',
//   component: LoginComponent
// },
// {
//   path:'register',
//   component: RegisterComponent
// },
// {
//   path:'admin',
//   component: AdminComponent
// }
// ,
// {
//   path:'home',
//   component: HomeComponent,
//   canActivate:[AuthguardGuard]
// },
// {
//   path:'home/cart',
//   component: CartItemComponent,
//   canActivate:[AuthguardGuard]
// },
// {
//   path:'home/address',
//   component: AddressComponent,
//   canActivate:[AuthguardGuard]
// },
// {
//   path:'admin/edit',
//   component: EditItemComponent,
//   canActivate:[AuthguardGuard]
// },
// {
//   path:'admin/order',
//   component: OrderItemComponent,
//   canActivate:[AuthguardGuard]
// }
// ];

// @NgModule({
//   imports: [RouterModule.forRoot(routes)],
//   exports: [RouterModule]
// })
// export class AppRoutingModule { }
