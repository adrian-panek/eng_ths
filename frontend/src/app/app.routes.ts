import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { MainScreenComponent } from './components/main-screen/main-screen.component';
import { OfferDetailsComponent } from './components/offer-details/offer-details.component';
import { RegisterComponent } from './components/register/register.component';

export const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'main', component: MainScreenComponent },
    { path: 'offer-details/:id', component: OfferDetailsComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
