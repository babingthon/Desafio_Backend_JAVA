import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../../services/user';
import { UserProfileDTO } from '../../models/UserProfileDTO';

import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { AvatarModule } from 'primeng/avatar';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule, FormsModule, InputTextModule,
    PasswordModule, ButtonModule, ToastModule, AvatarModule,
    IconFieldModule,
    InputIconModule
  ],
  providers: [MessageService],
  templateUrl: './profile.html',
  styleUrl: './profile.scss'
})
export class ProfileComponent implements OnInit {
  private userService = inject(User);
  private messageService = inject(MessageService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  user: UserProfileDTO = { name: '', jobTitle: '', email: '' };

  passwordData = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };

  loading = false;

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.userService.getLoggedUserProfile().subscribe({
      next: (data) => {
        this.user = data;
        this.cdr.detectChanges();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load profile' });
      }
    });
  }

  saveChanges() {

    if (!this.passwordData.currentPassword || !this.passwordData.newPassword || !this.passwordData.confirmPassword) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Required Fields',
        detail: 'Please fill in all password fields'
      });
      return;
    }

    if (this.passwordData.newPassword !== this.passwordData.confirmPassword) {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'New password and confirmation do not match'
      });
      return;
    }

    this.loading = true;

    this.userService.updatePassword({
      currentPassword: this.passwordData.currentPassword,
      newPassword: this.passwordData.newPassword
    }).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Password updated successfully!'
        });
        this.clearPasswordFields();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.loading = false;
        const errorMessage = err.status === 400 ? 'Current password is incorrect' : 'Failed to update password';
        this.messageService.add({
          severity: 'error',
          summary: 'Security Error',
          detail: errorMessage
        });
        this.cdr.detectChanges();
      }
    });
  }

  clearPasswordFields() {
    this.passwordData = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
  }

  getInitials(name: string): string {
    if (!name) return '';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2);
  }

  goBack() {
    this.router.navigate(['/dashboard']);
  }
}
