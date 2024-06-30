import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { User } from '../models/user';

const authApiPrefix = 'http://localhost:8080/api/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loginSuccessSubject: BehaviorSubject<boolean>;
  public loginSuccess$: Observable<boolean>;
  private registerSuccessSubject: BehaviorSubject<boolean>;
  public registerSuccess$: Observable<boolean>;

  constructor(private readonly http: HttpClient) {
    this.loginSuccessSubject = new BehaviorSubject<boolean>(false);
    this.loginSuccess$ = this.loginSuccessSubject.asObservable();
    this.registerSuccessSubject = new BehaviorSubject<boolean>(false);
    this.registerSuccess$ = this.registerSuccessSubject.asObservable();
  }

  login(login: string, password: string): Observable<User[]> {
    return this.http.post<User[]>(`${authApiPrefix}/login`, { login, password })
  }

  register(firstname: string, surname: string, login: string, password: string, isSeller: boolean): Observable<User> {
    return this.http.post<User>(`${authApiPrefix}/register`, { firstname, surname, login, password, isSeller })
  }

  getAuthHeaders(): HttpHeaders {
    const accessToken = this.getAccessToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`
    });
  }

  logout(): Observable<void> {
    localStorage.removeItem('accessToken');
    return of(undefined);
  }

  getAccessToken(): string | null {
    if (typeof window !== 'undefined' && window.localStorage) {
      return localStorage.getItem('accessToken');
    }
    return null;
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${authApiPrefix}/current-user`, { headers: this.getAuthHeaders() });
  }

}
