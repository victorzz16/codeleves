let current = null;
let solution = [];
let hintIndex = 0;

const list = document.querySelectorAll('.challenge-item');
const title = document.getElementById('challengeTitle');
const description = document.getElementById('challengeDescription');
const difficulty = document.getElementById('difficulty');
const category = document.getElementById('category');
const blockBank = document.getElementById('blockBank');
const solutionZone = document.getElementById('solutionZone');
const feedbackBox = document.getElementById('feedbackBox');
const javaCode = document.getElementById('javaCode');

list.forEach(btn => btn.addEventListener('click', () => loadChallenge(btn)));

document.getElementById('btnClear').addEventListener('click', () => {
  solution = [];
  renderSolution();
});

document.getElementById('btnHint').addEventListener('click', () => {
  if (!current) return showFeedback('error', 'Selecciona un reto primero.');
  const hints = JSON.parse(current.hints);
  if (hintIndex >= hints.length) return showFeedback('error', 'Ya usaste todas las pistas disponibles.');
  showFeedback('', '💡 ' + hints[hintIndex]);
  hintIndex++;
});

document.getElementById('btnValidate').addEventListener('click', validate);

function loadChallenge(btn) {
  list.forEach(b => b.classList.remove('active'));
  btn.classList.add('active');
  current = {
    id: Number(btn.dataset.id),
    title: btn.dataset.title,
    difficulty: btn.dataset.difficulty,
    category: btn.dataset.category,
    description: btn.dataset.description,
    blocks: JSON.parse(btn.dataset.blocks),
    solution: JSON.parse(btn.dataset.solution),
    hints: btn.dataset.hints,
    java: btn.dataset.java
  };
  solution = [];
  hintIndex = 0;
  title.textContent = current.title;
  description.textContent = current.description;
  difficulty.textContent = current.difficulty;
  category.textContent = current.category;
  javaCode.textContent = '// Completa correctamente el reto para generar código Java.';
  showFeedback('', 'Ordena los bloques y valida tu lógica.');
  renderBlocks();
  renderSolution();
}

function renderBlocks() {
  blockBank.innerHTML = '';
  current.blocks.forEach((label, index) => {
    const button = document.createElement('button');
    button.className = 'block-item';
    button.textContent = label;
    button.addEventListener('click', () => {
      solution.push(index);
      renderSolution();
    });
    blockBank.appendChild(button);
  });
}

function renderSolution() {
  if (!solution.length) {
    solutionZone.innerHTML = '<span class="empty">Agrega bloques para construir la solución.</span>';
    return;
  }
  solutionZone.innerHTML = '';
  solution.forEach((index, position) => {
    const item = document.createElement('div');
    item.className = 'solution-block';
    item.innerHTML = `<span>${position + 1}. ${current.blocks[index]}</span><button class="btn-remove">×</button>`;
    item.querySelector('button').addEventListener('click', () => {
      solution.splice(position, 1);
      renderSolution();
    });
    solutionZone.appendChild(item);
  });
}

function validate() {
  if (!current) return showFeedback('error', 'Selecciona un reto primero.');
  const ok = JSON.stringify(solution) === JSON.stringify(current.solution);
  if (ok) {
    showFeedback('success', '✅ Lógica correcta. Ahora puedes revisar el código Java para NetBeans.');
    javaCode.textContent = current.java;
    fetch('/api/alumno/progreso/guardar', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({retoId: current.id, completado: true})
    }).catch(() => console.warn('No se pudo guardar progreso'));
  } else {
    showFeedback('error', '⚠️ Revisa el orden de los bloques. Primero identifica entrada, proceso, decisión y salida.');
    fetch('/api/alumno/progreso/guardar', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({retoId: current.id, completado: false})
    }).catch(() => console.warn('No se pudo guardar intento'));
  }
}

function showFeedback(type, msg) {
  feedbackBox.className = 'feedback ' + type;
  feedbackBox.innerHTML = `<strong>Feedback</strong><p>${msg}</p>`;
}

// ──────────────────────────────────────────────
// Reconocimiento de variables Java
// Detecta declaraciones simples y ayuda al alumno a identificar tipo, nombre y valor.
// ──────────────────────────────────────────────
const variableCode = document.getElementById('variableCode');
const variableResult = document.getElementById('variableResult');
const variableTableWrap = document.getElementById('variableTableWrap');
const btnAnalyzeVariables = document.getElementById('btnAnalyzeVariables');
const btnClearVariables = document.getElementById('btnClearVariables');
const btnVariableExample = document.getElementById('btnVariableExample');

if (btnAnalyzeVariables) {
  btnAnalyzeVariables.addEventListener('click', analyzeVariables);
  btnClearVariables.addEventListener('click', () => {
    variableCode.value = '';
    variableTableWrap.innerHTML = '';
    variableResult.className = 'feedback';
    variableResult.innerHTML = '<strong>Guía</strong><p>Escribe declaraciones Java para reconocer variables.</p>';
  });
  btnVariableExample.addEventListener('click', () => {
    variableCode.value = 'int edad = 18;\ndouble promedio = 14.5;\nString nombre = "Mariana";\nboolean aprobado = true;\nchar seccion = \'A\';';
    analyzeVariables();
  });
}

function analyzeVariables() {
  const text = variableCode.value.trim();
  if (!text) {
    variableResult.className = 'feedback error';
    variableResult.innerHTML = '<strong>Sin código</strong><p>Primero escribe una o más declaraciones de variables.</p>';
    return;
  }

  const allowedTypes = ['int','double','String','boolean','char','long','float'];
  const lines = text.split(/\n|;/).map(l => l.trim()).filter(Boolean);
  const found = [];
  const errors = [];

  lines.forEach((line, index) => {
    const match = line.match(/^(int|double|String|boolean|char|long|float)\s+([a-zA-Z_$][\w$]*)(\s*=\s*(.+))?$/);
    if (match) {
      const type = match[1];
      const name = match[2];
      const value = (match[4] || 'Sin asignar').trim();
      found.push({line:index + 1, type, name, value, usage: explainType(type)});
    } else {
      const startsWithType = allowedTypes.some(t => line.startsWith(t + ' '));
      errors.push(`Línea ${index + 1}: "${line}" ${startsWithType ? 'parece incompleta o tiene nombre inválido.' : 'no inicia con un tipo de dato Java reconocido.'}`);
    }
  });

  if (found.length) {
    variableTableWrap.innerHTML = `
      <table class="variable-table">
        <thead><tr><th>Línea</th><th>Tipo</th><th>Variable</th><th>Valor</th><th>Uso</th></tr></thead>
        <tbody>${found.map(v => `<tr><td>${v.line}</td><td>${v.type}</td><td><b>${v.name}</b></td><td>${escapeHtml(v.value)}</td><td>${v.usage}</td></tr>`).join('')}</tbody>
      </table>`;
  } else {
    variableTableWrap.innerHTML = '';
  }

  const duplicated = found.map(v => v.name).filter((name, i, arr) => arr.indexOf(name) !== i);
  let msg = `Se reconocieron ${found.length} variable(s).`;
  if (duplicated.length) msg += ` Ojo: la variable ${[...new Set(duplicated)].join(', ')} aparece repetida.`;
  if (errors.length) msg += ` Hay ${errors.length} línea(s) por revisar.`;

  variableResult.className = errors.length ? 'feedback error' : 'feedback success';
  variableResult.innerHTML = `<strong>${errors.length ? 'Revisión con observaciones' : 'Variables reconocidas'}</strong><p>${msg}</p>${errors.length ? '<ul>' + errors.map(e => `<li>${escapeHtml(e)}</li>`).join('') + '</ul>' : ''}`;
}

function explainType(type) {
  return {
    int:'Número entero',
    double:'Número decimal',
    String:'Texto',
    boolean:'Verdadero/Falso',
    char:'Un carácter',
    long:'Entero grande',
    float:'Decimal simple'
  }[type] || 'Dato';
}

function escapeHtml(value) {
  return String(value).replace(/[&<>"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]));
}
