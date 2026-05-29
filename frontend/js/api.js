/* ============================================
   INGENIERÍA DEL AHORRO - API Client
   ============================================ */

const API_BASE = 'http://localhost:8080/api';

const Api = {
  getToken() {
    const sessionToken = sessionStorage.getItem('token');
    if (sessionToken) localStorage.setItem('token', sessionToken);
    return localStorage.getItem('token');
  },

  getUser() {
    const u = localStorage.getItem('user') || sessionStorage.getItem('user');
    return u ? JSON.parse(u) : null;
  },

  headers() {
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.getToken()}`
    };
  },

  async request(method, endpoint, body = null) {
    const opts = {
      method,
      headers: this.headers()
    };
    if (body) opts.body = JSON.stringify(body);

    try {
      const res = await fetch(`${API_BASE}${endpoint}`, opts);
      const text = await res.text();
      if (!text) return null;
      return JSON.parse(text);
    } catch (err) {
      console.error('API Error:', err);
      Toast.show('Error de conexión con el servidor', 'error');
      return null;
    }
  },

  get(endpoint) { return this.request('GET', endpoint); },
  post(endpoint, body) { return this.request('POST', endpoint, body); },
  put(endpoint, body) { return this.request('PUT', endpoint, body); },
  delete(endpoint) { return this.request('DELETE', endpoint); },

  async download(endpoint) {
    const res = await fetch(`${API_BASE}${endpoint}`, {
      headers: { 'Authorization': `Bearer ${this.getToken()}` }
    });
    return res;
  }
};

/* ============================================
   AUTH
   ============================================ */
const Auth = {
  isLoggedIn() {
    // Si hay token en sessionStorage, migrarlo a localStorage automáticamente
    const sessionToken = sessionStorage.getItem('token');
    if (sessionToken) {
      localStorage.setItem('token', sessionToken);
      localStorage.setItem('user', sessionStorage.getItem('user'));
    }
    return !!(localStorage.getItem('token'));
  },

  async login(correo, password, remember) {
    const res = await fetch(`${API_BASE}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ correo, password })
    });
    const data = await res.json();
    if (data.success) {
      // Siempre usar localStorage para que persista entre páginas
      localStorage.setItem('token', data.data.token);
      localStorage.setItem('user', JSON.stringify(data.data));
      return { success: true, data: data.data };
    }
    return { success: false, message: data.message };
  },

  async register(nombre, correo, password, confirmarPassword) {
    const res = await fetch(`${API_BASE}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nombre, correo, password, confirmarPassword })
    });
    const data = await res.json();
    if (data.success) {
      localStorage.setItem('token', data.data.token);
      localStorage.setItem('user', JSON.stringify(data.data));
      return { success: true, data: data.data };
    }
    return { success: false, message: data.message };
  },

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('user');
    window.location.href = 'index.html';
  },

  requireAuth() {
    if (!this.isLoggedIn()) {
      window.location.href = 'index.html';
      return false;
    }
    return true;
  }
};

/* ============================================
   TOAST NOTIFICATIONS
   ============================================ */
const Toast = {
  container: null,

  init() {
    if (!this.container) {
      this.container = document.createElement('div');
      this.container.className = 'toast-container';
      document.body.appendChild(this.container);
    }
  },

  show(message, type = 'info', duration = 3500) {
    this.init();
    const icons = { success: '✅', error: '❌', warning: '⚠️', info: 'ℹ️' };
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
      <span class="toast-icon">${icons[type] || icons.info}</span>
      <span class="toast-message">${message}</span>
      <span class="toast-close" onclick="this.parentElement.remove()">✕</span>
    `;
    this.container.appendChild(toast);
    setTimeout(() => {
      toast.style.animation = 'fadeOut 0.3s ease forwards';
      setTimeout(() => toast.remove(), 300);
    }, duration);
  }
};

/* ============================================
   FORMATTERS
   ============================================ */
const Fmt = {
  currency(value) {
    if (value === null || value === undefined) return '$ 0';
    const num = parseFloat(value) || 0;
    const formatted = new Intl.NumberFormat('es-CO', {
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(Math.abs(num));
    return (num < 0 ? '-$ ' : '$ ') + formatted;
  },

  date(dateStr) {
    if (!dateStr) return '-';
    const d = new Date(dateStr + 'T00:00:00');
    return d.toLocaleDateString('es-CO', { day: '2-digit', month: 'short', year: 'numeric' });
  },

  percent(value) {
    return `${(value || 0).toFixed(1)}%`;
  },

  monthName(num) {
    const names = ['', 'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
      'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    return names[num] || '';
  }
};

/* ============================================
   CONFIRM DIALOG
   ============================================ */
const Confirm = {
  show(message, onConfirm) {
    const overlay = document.createElement('div');
    overlay.className = 'modal-overlay active';
    overlay.innerHTML = `
      <div class="modal" style="max-width:380px">
        <div class="modal-header">
          <h3 class="modal-title">⚠️ Confirmar acción</h3>
        </div>
        <div class="modal-body">
          <p style="color:var(--text-muted);font-size:0.9rem">${message}</p>
        </div>
        <div class="modal-footer">
          <button class="btn btn-outline" id="cancelBtn">Cancelar</button>
          <button class="btn btn-danger" id="confirmBtn">Eliminar</button>
        </div>
      </div>
    `;
    document.body.appendChild(overlay);
    overlay.querySelector('#cancelBtn').onclick = () => overlay.remove();
    overlay.querySelector('#confirmBtn').onclick = () => { overlay.remove(); onConfirm(); };
    overlay.onclick = (e) => { if (e.target === overlay) overlay.remove(); };
  }
};

/* ============================================
   THEME MANAGER
   ============================================ */
const Theme = {
  init() {
    const user = Api.getUser();
    const dark = user?.darkMode || localStorage.getItem('darkMode') === 'true';
    if (dark) document.documentElement.setAttribute('data-theme', 'dark');
  },

  toggle() {
    const isDark = document.documentElement.getAttribute('data-theme') === 'dark';
    if (isDark) {
      document.documentElement.removeAttribute('data-theme');
      localStorage.setItem('darkMode', 'false');
    } else {
      document.documentElement.setAttribute('data-theme', 'dark');
      localStorage.setItem('darkMode', 'true');
    }
  }
};

/* ============================================
   SIDEBAR MANAGER
   ============================================ */
const Sidebar = {
  init() {
    const sidebar = document.getElementById('sidebar');
    const toggleBtn = document.getElementById('sidebarToggle');
    const mobileBtn = document.getElementById('mobileSidebarBtn');
    const overlay = document.getElementById('sidebarOverlay');

    if (toggleBtn) {
      toggleBtn.addEventListener('click', () => {
        sidebar.classList.toggle('collapsed');
        document.querySelector('.main-content')?.classList.toggle('sidebar-collapsed');
        const icon = toggleBtn.querySelector('span');
        if (icon) icon.textContent = sidebar.classList.contains('collapsed') ? '›' : '‹';
      });
    }

    if (mobileBtn) {
      mobileBtn.addEventListener('click', () => {
        sidebar.classList.add('mobile-open');
        overlay?.classList.add('active');
      });
    }

    if (overlay) {
      overlay.addEventListener('click', () => {
        sidebar.classList.remove('mobile-open');
        overlay.classList.remove('active');
      });
    }

    // Marcar nav item activo
    const currentPage = window.location.pathname.split('/').pop();
    document.querySelectorAll('.nav-item').forEach(item => {
      if (item.getAttribute('href') === currentPage) {
        item.classList.add('active');
      }
    });

    // Cargar info usuario en sidebar
    const user = Api.getUser();
    if (user) {
      const nameEl = document.getElementById('sidebarUserName');
      const avatarEl = document.getElementById('sidebarAvatar');
      if (nameEl) nameEl.textContent = user.nombre;
      if (avatarEl) {
        if (user.fotoPerfil) {
          avatarEl.innerHTML = `<img src="${API_BASE.replace('/api','')}${user.fotoPerfil}" alt="foto">`;
        } else {
          avatarEl.textContent = user.nombre?.charAt(0).toUpperCase() || '👤';
        }
      }
    }
  }
};

/* ============================================
   MONTH/YEAR SELECTOR
   ============================================ */
const MonthSelector = {
  mes: new Date().getMonth() + 1,
  anio: new Date().getFullYear(),
  callbacks: [],

  init() {
    const mesEl = document.getElementById('selectMes');
    const anioEl = document.getElementById('selectAnio');
    if (!mesEl || !anioEl) return;

    mesEl.value = this.mes;
    anioEl.value = this.anio;

    mesEl.addEventListener('change', () => {
      this.mes = parseInt(mesEl.value);
      this.notify();
    });

    anioEl.addEventListener('change', () => {
      this.anio = parseInt(anioEl.value);
      this.notify();
    });
  },

  onChange(cb) { this.callbacks.push(cb); },
  notify() { this.callbacks.forEach(cb => cb(this.mes, this.anio)); }
};

// Inicializar tema al cargar
document.addEventListener('DOMContentLoaded', () => {
  Theme.init();
});
