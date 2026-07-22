(function () {
  const STORAGE_KEY = 'codelevels_accessibility';

  const defaults = {
    daltonico: false,
    altoContraste: false,
    textoGrande: false,
    reducirMovimiento: false,
    subrayarEnlaces: false,
    modoOscuro: false
  };

  function readPrefs() {
    try {
      return { ...defaults, ...JSON.parse(localStorage.getItem(STORAGE_KEY) || '{}') };
    } catch (e) {
      return { ...defaults };
    }
  }

  function savePrefs(prefs) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(prefs));
  }

  function applyPrefs(prefs) {
    document.body.classList.toggle('access-daltonico', prefs.daltonico);
    document.body.classList.toggle('access-contraste', prefs.altoContraste);
    document.body.classList.toggle('access-texto-grande', prefs.textoGrande);
    document.body.classList.toggle('access-reducir-movimiento', prefs.reducirMovimiento);
    document.body.classList.toggle('access-subrayar-enlaces', prefs.subrayarEnlaces);
    document.body.classList.toggle('dark-mode', prefs.modoOscuro);

    document.querySelectorAll('[data-access-toggle]').forEach((btn) => {
      const key = btn.getAttribute('data-access-toggle');
      btn.setAttribute('aria-pressed', String(Boolean(prefs[key])));
      btn.classList.toggle('active', Boolean(prefs[key]));
    });
  }

  function announce(message) {
    let live = document.getElementById('accessLiveRegion');
    if (!live) {
      live = document.createElement('div');
      live.id = 'accessLiveRegion';
      live.className = 'sr-only';
      live.setAttribute('aria-live', 'polite');
      document.body.appendChild(live);
    }
    live.textContent = message;
  }

  function buildWidget() {
    if (document.getElementById('accessibilityWidget')) return;

    const widget = document.createElement('section');
    widget.id = 'accessibilityWidget';
    widget.className = 'accessibility-widget';
    widget.setAttribute('aria-label', 'Herramientas de accesibilidad');
    widget.innerHTML = `
      <button type="button" class="accessibility-trigger" id="accessibilityTrigger" aria-expanded="false" aria-controls="accessibilityPanel" title="Abrir opciones de accesibilidad">
        ♿ Accesibilidad
      </button>
      <div class="accessibility-panel" id="accessibilityPanel" hidden>
        <div class="accessibility-head">
          <strong>Accesibilidad</strong>
          <span>CodeLevels</span>
        </div>
        <button type="button" data-access-toggle="daltonico">Modo daltónico</button>
        <button type="button" data-access-toggle="altoContraste">Alto contraste</button>
        <button type="button" data-access-toggle="textoGrande">Texto grande</button>
        <button type="button" data-access-toggle="reducirMovimiento">Reducir animaciones</button>
        <button type="button" data-access-toggle="subrayarEnlaces">Subrayar enlaces</button>
        <button type="button" data-access-toggle="modoOscuro">Modo oscuro</button>
        <button type="button" class="accessibility-reset" id="accessibilityReset">Restablecer</button>
        <small>Atajo: Alt + A abre este panel.</small>
      </div>
    `;
    document.body.appendChild(widget);

    const trigger = document.getElementById('accessibilityTrigger');
    const panel = document.getElementById('accessibilityPanel');

    trigger.addEventListener('click', () => {
      const isHidden = panel.hasAttribute('hidden');
      panel.toggleAttribute('hidden');
      trigger.setAttribute('aria-expanded', String(isHidden));
    });

    let prefs = readPrefs();
    widget.querySelectorAll('[data-access-toggle]').forEach((btn) => {
      btn.addEventListener('click', () => {
        const key = btn.getAttribute('data-access-toggle');
        prefs[key] = !prefs[key];
        savePrefs(prefs);
        applyPrefs(prefs);
        announce(`${btn.textContent.trim()} ${prefs[key] ? 'activado' : 'desactivado'}`);
      });
    });

    document.getElementById('accessibilityReset').addEventListener('click', () => {
      prefs = { ...defaults };
      savePrefs(prefs);
      applyPrefs(prefs);
      announce('Opciones de accesibilidad restablecidas');
    });

    document.addEventListener('keydown', (event) => {
      if (event.altKey && event.key.toLowerCase() === 'a') {
        event.preventDefault();
        trigger.click();
      }
      if (event.key === 'Escape' && !panel.hasAttribute('hidden')) {
        panel.setAttribute('hidden', '');
        trigger.setAttribute('aria-expanded', 'false');
      }
    });

    applyPrefs(prefs);
  }

  document.addEventListener('DOMContentLoaded', buildWidget);
})();
