import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    title: 'JACK | Bestiário digital',
    loadComponent: () => import('./home/home').then((module) => module.Home),
  },
  { path: '**', redirectTo: '' },
];
