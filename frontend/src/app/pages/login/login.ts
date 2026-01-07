import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    IconFieldModule,
    InputIconModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class LoginComponent {
  username = '';
  password = '';

  private authService = inject(AuthService);
  private router = inject(Router);

  onLogin() {
    if (this.username && this.password) {
      this.authService.login(this.username, this.password).subscribe({
        next: (res) => {
          console.log('Login bem-sucedido!', res);
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error('Falha no login. Verifique suas credenciais e o console Network.');
          alert('Erro ao logar: ' + (err.error?.message || 'Verifique usuário e senha'));
        }
      });
    }
  }
}
