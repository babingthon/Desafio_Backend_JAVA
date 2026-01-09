import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { User } from '../../services/user';
import { UserProfileDTO } from '../../models/UserProfileDTO';

// PrimeNG Modules
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { ProgressBarModule } from 'primeng/progressbar';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    TagModule,
    ProgressBarModule,
    ButtonModule,
    AvatarModule,
    MenuModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent implements OnInit {
  private router = inject(Router);
  private userService = inject(User);
  private cdr = inject(ChangeDetectorRef);

  currentUser: UserProfileDTO | null = null;

  profileMenuItems: MenuItem[] | undefined;

  stats = [
    { label: 'Total Tasks', value: 10, icon: 'pi pi-th-large', color: 'blue' },
    { label: 'Completed', value: 4, icon: 'pi pi-check-circle', color: 'green' },
    { label: 'In Progress', value: 3, icon: 'pi pi-clock', color: 'yellow' },
    { label: 'Overdue', value: 1, icon: 'pi pi-exclamation-triangle', color: 'red' }
  ];

  ngOnInit() {
    this.fetchUserProfile();
    this.setupProfileMenu();
  }

  fetchUserProfile() {
    this.userService.getLoggedUserProfile().subscribe({
      next: (data) => {
        this.currentUser = data;
        // Força o Angular a perceber que o objeto mudou e remover o Skeleton
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading profile:', err);
        if (err.status === 401) {
          this.logout();
        }
      }
    });
  }

  setupProfileMenu() {
    this.profileMenuItems = [
      {
        label: 'Account',
        items: [
          {
            label: 'Profile Settings',
            icon: 'pi pi-user-edit',
            command: () => {
              this.router.navigate(['/profile']);
            }
          },
          {
            label: 'Sign Out',
            icon: 'pi pi-sign-out',
            command: () => {
              this.logout();
            }
          }
        ]
      }
    ];
  }

  getInitials(name: string | undefined): string {
    if (!name) return '';
    const names = name.split(' ').filter(n => n.length > 0);
    if (names.length >= 2) {
      return (names[0][0] + names[names.length - 1][0]).toUpperCase();
    }
    return name.substring(0, 2).toUpperCase();
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
