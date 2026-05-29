/* ============================================
   LAYOUT - Sidebar HTML compartido
   ============================================ */

function svgIcon(name) {
  const icons = {
    home:     `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>`,
    income:   `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>`,
    fixed:    `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>`,
    small:    `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>`,
    debt:     `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>`,
    budget:   `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="3" width="20" height="14" rx="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg>`,
    goal:     `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><circle cx="12" cy="12" r="6"/><circle cx="12" cy="12" r="2"/></svg>`,
    calendar: `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>`,
    chart:    `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="20" x2="18" y2="10"/><line x1="12" y1="20" x2="12" y2="4"/><line x1="6" y1="20" x2="6" y2="14"/></svg>`,
    learn:    `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/></svg>`,
    report:   `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><line x1="10" y1="9" x2="8" y2="9"/></svg>`,
    user:     `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>`,
    settings: `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83-2.83l.06-.06A1.65 1.65 0 0 0 4.68 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 2.83-2.83l.06.06A1.65 1.65 0 0 0 9 4.68a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 2.83l-.06.06A1.65 1.65 0 0 0 19.4 9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/></svg>`,
  };
  return icons[name] || '';
}

function renderLayout(activeNav) {
  const navItems = [
    { href: 'dashboard.html', icon: svgIcon('home'), label: 'Dashboard', section: 'principal' },
    { href: 'ingresos.html', icon: svgIcon('income'), label: 'Ingresos', section: 'finanzas' },
    { href: 'gastos-fijos.html', icon: svgIcon('fixed'), label: 'Gastos Fijos', section: 'finanzas' },
    { href: 'gastos-hormiga.html', icon: svgIcon('small'), label: 'Gastos Menores', section: 'finanzas' },
    { href: 'deudas.html', icon: svgIcon('debt'), label: 'Deudas', section: 'finanzas' },
    { href: 'presupuesto.html', icon: svgIcon('budget'), label: 'Presupuesto', section: 'planificacion' },
    { href: 'metas.html', icon: svgIcon('goal'), label: 'Metas de Ahorro', section: 'planificacion' },
    { href: 'resumen.html', icon: svgIcon('calendar'), label: 'Resumen Mensual', section: 'analisis' },
    { href: 'graficas.html', icon: svgIcon('chart'), label: 'Gráficas', section: 'analisis' },
    { href: 'aprende.html', icon: svgIcon('learn'), label: 'Aprende Finanzas', section: 'extra' },
    { href: 'reportes.html', icon: svgIcon('report'), label: 'Reportes', section: 'extra' },
    { href: 'perfil.html', icon: svgIcon('user'), label: 'Mi Perfil', section: 'cuenta' },
    { href: 'configuracion.html', icon: svgIcon('settings'), label: 'Configuración', section: 'cuenta' },
  ];

  const sections = {
    principal: 'Principal',
    finanzas: 'Finanzas',
    planificacion: 'Planificación',
    analisis: 'Análisis',
    extra: 'Recursos',
    cuenta: 'Cuenta'
  };

  let lastSection = '';
  let navHTML = '';
  navItems.forEach(item => {
    if (item.section !== lastSection) {
      navHTML += `<div class="nav-section-title">${sections[item.section]}</div>`;
      lastSection = item.section;
    }
    const isActive = item.href === activeNav ? 'active' : '';
    navHTML += `
      <a href="${item.href}" class="nav-item ${isActive}">
        <div class="nav-icon">${item.icon}</div>
        <span class="nav-label">${item.label}</span>
      </a>
    `;
  });

  const user = Api.getUser();
  const avatarContent = user?.fotoPerfil
    ? `<img src="http://localhost:8080${user.fotoPerfil}" alt="foto" style="width:36px;height:36px;border-radius:50%;object-fit:cover">`
    : `<div class="user-avatar" style="font-size:14px;font-weight:700;color:white">${user?.nombre?.charAt(0).toUpperCase() || 'U'}</div>`;

  return `
    <div class="sidebar-overlay" id="sidebarOverlay"></div>
    <aside class="sidebar" id="sidebar">
      <div class="sidebar-logo">
        <div class="logo-icon" style="font-size:15px;font-weight:800;color:white;letter-spacing:-0.5px">IA</div>
        <div class="logo-text">
          Ingeniería del Ahorro
          <span>Finanzas Personales</span>
        </div>
        <button class="sidebar-toggle" id="sidebarToggle"><span>‹</span></button>
      </div>
      <nav class="sidebar-nav">${navHTML}</nav>
      <div class="sidebar-footer">
        <div class="sidebar-user" onclick="window.location.href='perfil.html'">
          ${avatarContent}
          <div class="user-info">
            <div class="user-name" id="sidebarUserName">${user?.nombre || 'Usuario'}</div>
            <div class="user-role">Mi cuenta</div>
          </div>
        </div>
      </div>
    </aside>
  `;
}

function renderTopbar(title, subtitle, showMonthSelector = false) {
  const now = new Date();
  const months = ['', 'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
    'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];

  let monthSelectorHTML = '';
  if (showMonthSelector) {
    let monthOptions = '';
    for (let i = 1; i <= 12; i++) {
      monthOptions += `<option value="${i}" ${i === now.getMonth() + 1 ? 'selected' : ''}>${months[i]}</option>`;
    }
    let yearOptions = '';
    for (let y = now.getFullYear() - 2; y <= now.getFullYear() + 1; y++) {
      yearOptions += `<option value="${y}" ${y === now.getFullYear() ? 'selected' : ''}>${y}</option>`;
    }
    monthSelectorHTML = `
      <div class="month-selector">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
        <select id="selectMes">${monthOptions}</select>
        <select id="selectAnio">${yearOptions}</select>
      </div>
    `;
  }

  const user = Api.getUser();
  const dateStr = now.toLocaleDateString('es-CO', { weekday: 'long', day: 'numeric', month: 'long' });

  return `
    <header class="topbar">
      <div class="topbar-left">
        <button class="topbar-btn" id="mobileSidebarBtn" style="display:none">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="18" x2="21" y2="18"/></svg>
        </button>
        <div class="topbar-title">
          <h1>${title}</h1>
          <p>${subtitle || dateStr}</p>
        </div>
      </div>
      <div class="topbar-right">
        ${monthSelectorHTML}
        <button class="topbar-btn" onclick="Theme.toggle()" title="Cambiar tema">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>
        </button>
        <button class="topbar-btn" onclick="Auth.logout()" title="Cerrar sesión">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
        </button>
        <div style="display:flex;align-items:center;gap:8px;cursor:pointer" onclick="window.location.href='perfil.html'">
          <div style="width:36px;height:36px;border-radius:50%;background:var(--primary);display:flex;align-items:center;justify-content:center;color:white;font-weight:700;font-size:14px">
            ${user?.nombre?.charAt(0).toUpperCase() || '?'}
          </div>
        </div>
      </div>
    </header>
  `;
}

// Mostrar botón hamburguesa en móvil
window.addEventListener('resize', () => {
  const btn = document.getElementById('mobileSidebarBtn');
  if (btn) btn.style.display = window.innerWidth <= 768 ? 'flex' : 'none';
});
