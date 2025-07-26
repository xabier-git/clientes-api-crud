import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/clientes', pathMatch: 'full' },
  { 
    path: 'clientes', 
    loadComponent: () => import('./components/cliente-list/cliente-list.component').then(m => m.ClienteListComponent)
  },
  { 
    path: 'clientes/nuevo', 
    loadComponent: () => import('./components/cliente-form/cliente-form.component').then(m => m.ClienteFormComponent)
  },
  { 
    path: 'clientes/editar/:id', 
    loadComponent: () => import('./components/cliente-form/cliente-form.component').then(m => m.ClienteFormComponent)
  },
  { 
    path: 'clientes/detalle/:id', 
    loadComponent: () => import('./components/cliente-detail/cliente-detail.component').then(m => m.ClienteDetailComponent)
  }
];
