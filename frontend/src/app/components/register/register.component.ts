import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/authService';
import { RegisterModule } from './register.module';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-root',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  firstName: string;
  surname: string;
  email: string;
  password: string;
  confirmPassword: string;
  isSeller: boolean;
  registerErrorPassword: string;
  registerErrorConfirmedPassword: string;
  showPassword: boolean = false;
  showPasswordConfirmation: boolean = false;
  submitted: boolean = false;

  constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) {
    this.firstName = '';
    this.surname = '';
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
    this.registerErrorPassword = '';
    this.registerErrorConfirmedPassword = '';
    this.isSeller = false;
  }

  register() {
    this.submitted = true;
    if (!this.isPasswordValid()) {
      this.registerErrorPassword = 'Hasło musi zawierać co najmniej jedną małą literę, jedną wielką literę, jedną cyfrę i mieć co najmniej 8 znaków.';
      return;
    }
    if (!this.arePasswordsMatching()) {
      this.registerErrorConfirmedPassword = 'Hasło i potwierdzenie hasła nie są identyczne.';
      return;
    }
    this.authService.register(this.firstName, this.surname, this.email, this.password, this.isSeller).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (error) => {
        if (error.status == 403) {
          this.showError('Adres email jest już zajęty!', 'Błąd rejestracji');
        } else {
          this.showError('Spróbuj ponownie później.', 'Błąd rejestracji');
        }
        console.error('Błąd rejestracji:', error);
      },
      complete: () => {
        this.showSuccess('Zarejestrowano pomyślnie', 'Sukces');
      }
    });
  }
  showError(message: string, title: string) {
    this.toastr.error(message, title);
  }
  showSuccess(message: string, title: string) {
    this.toastr.success(message, title);
  }
  goToLoginScreen() {
    this.router.navigate(['/']);
  }
  isPasswordValid(): boolean {
    const passwordRegex = /^(?=.*[a-ząęćłńóśźż])(?=.*[A-ZĄĘĆŁŃÓŚŹŻ])(?=.*[!@#$%^&*])[a-zA-ZąęćłńóśźżĄĘĆŁŃÓŚŹŻ\d!@#$%^&*]{8,}$/;
    return passwordRegex.test(this.password);
  }
  arePasswordsMatching(): boolean {
    return this.password === this.confirmPassword;
  }
}
