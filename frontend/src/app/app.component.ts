import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <div class="container">
      <header>
        <h1>Gestión de Clientes</h1>
        <nav>
          <a routerLink="/clientes" class="btn btn-primary mr-2">Lista de Clientes</a>
          <a routerLink="/clientes/nuevo" class="btn btn-success">Nuevo Cliente</a>
        </nav>
      </header>
      <main>
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styles: [`
    header {
      background: white;
      padding: 20px;
      margin-bottom: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    
    header h1 {
      color: #333;
      margin-bottom: 20px;
    }
    
    nav {
      display: flex;
      gap: 10px;
    }
    
    main {
      min-height: 500px;
    }
  `]
})
export class AppComponent {
  title = 'Gestión de Clientes';
}
