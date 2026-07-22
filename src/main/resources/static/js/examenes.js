const EXAMS = [
  {
    id: 'basico',
    title: 'Examen básico de fundamentos',
    difficulty: 'Básico',
    minutes: 10,
    description: 'Evalúa variables, entrada/salida, operadores y condicionales simples.',
    minLevel: 'Nivel 1 a 2',
    questions: [
      {topic:'vars', q:'¿Cuál es una variable válida para guardar una edad en Java?', options:['int edad = 18;','edad int = 18;','numero edad texto;'], answer:0},
      {topic:'vars', q:'¿Qué tipo usarías para una nota con decimales?', options:['int','double','boolean'], answer:1},
      {topic:'vars', q:'¿Qué significa declarar una variable?', options:['Mostrar un mensaje','Crear un espacio con nombre para guardar datos','Cerrar el programa'], answer:1},
      {topic:'if', q:'Si nota = 15 y la condición es nota >= 13, ¿qué ocurre?', options:['La condición es verdadera','La condición es falsa','El programa no puede comparar'], answer:0},
      {topic:'if', q:'¿Qué estructura se usa para tomar decisiones?', options:['for','if / else','Scanner'], answer:1},
      {topic:'loops', q:'¿Para qué sirve un bucle?', options:['Para repetir instrucciones','Para crear colores','Para borrar variables'], answer:0},
      {topic:'arrays', q:'En Java, el primer índice de un arreglo es:', options:['1','0','-1'], answer:1},
      {topic:'vars', q:'¿Qué instrucción permite leer datos por teclado?', options:['Scanner','println','class'], answer:0}
    ]
  },
  {
    id: 'intermedio',
    title: 'Examen intermedio de lógica aplicada',
    difficulty: 'Intermedio',
    minutes: 15,
    description: 'Evalúa condicionales, bucles, acumuladores, contadores y arreglos básicos.',
    minLevel: 'Nivel 3 a 4',
    questions: [
      {topic:'if', q:'Para validar si una contraseña es igual a otra, ¿qué operador se usa?', options:['=','==','++'], answer:1},
      {topic:'loops', q:'En un for, ¿qué hace i++?', options:['Resta 1','Incrementa el contador','Finaliza el programa'], answer:1},
      {topic:'loops', q:'Si necesitas mostrar la tabla del 1 al 10, ¿qué estructura conviene?', options:['if simple','for','String'], answer:1},
      {topic:'arrays', q:'¿Qué es un arreglo?', options:['Una colección de datos del mismo tipo','Una condición','Una clase de CSS'], answer:0},
      {topic:'arrays', q:'Si int[] nums = new int[5]; ¿cuántos elementos tiene?', options:['4','5','6'], answer:1},
      {topic:'loops', q:'¿Qué variable se usa normalmente para contar repeticiones en un for?', options:['i','String','Scanner'], answer:0},
      {topic:'vars', q:'¿Qué tipo de dato usarías para verdadero/falso?', options:['boolean','double','String'], answer:0},
      {topic:'if', q:'¿Qué significa else?', options:['Rama alternativa si la condición no se cumple','Un bucle infinito','Un tipo de variable'], answer:0},
      {topic:'loops', q:'Para sumar del 1 al N, ¿qué se necesita?', options:['Acumulador','Solo println','Un arreglo obligatorio'], answer:0},
      {topic:'arrays', q:'Para recorrer todos los elementos de un arreglo normalmente se usa:', options:['for','if','package'], answer:0}
    ]
  },
  {
    id: 'avanzado',
    title: 'Examen avanzado de razonamiento algorítmico',
    difficulty: 'Avanzado',
    minutes: 20,
    description: 'Evalúa lectura de código, arreglos, bucles con condiciones y solución de problemas combinados.',
    minLevel: 'Nivel 4 a 5',
    questions: [
      {topic:'loops', q:'Si suma inicia en 0 y el for suma i desde 1 hasta 5, ¿cuál es el resultado?', options:['10','15','5'], answer:1},
      {topic:'arrays', q:'Si mayor = nums[0], ¿por qué luego se recorre desde i = 1?', options:['Porque nums[0] ya se usó como referencia','Porque Java no permite índice 0','Porque i=0 da error'], answer:0},
      {topic:'if', q:'En if (a > b && a > c), ¿qué significa &&?', options:['O lógico','Y lógico','Asignación'], answer:1},
      {topic:'loops', q:'Para contar pares entre 1 y N, ¿qué combinación se usa?', options:['Bucle + condición i % 2 == 0','Solo Scanner','Solo String'], answer:0},
      {topic:'arrays', q:'¿Qué pasa si accedes a nums[5] en un arreglo de tamaño 5?', options:['Accede al último elemento','Error por índice fuera de rango','Devuelve cero siempre'], answer:1},
      {topic:'vars', q:'¿Por qué el factorial se inicializa en 1 y no en 0?', options:['Porque multiplicar por 0 anula el resultado','Porque Java exige 1','Porque no usa bucles'], answer:0},
      {topic:'if', q:'Para clasificar positivo, negativo o cero conviene usar:', options:['if / else if / else','for solamente','Scanner únicamente'], answer:0},
      {topic:'loops', q:'Un acumulador se diferencia de un contador porque:', options:['Guarda una suma o cálculo acumulado','Siempre vale 1','Solo funciona con texto'], answer:0},
      {topic:'arrays', q:'Para sumar un arreglo se necesita:', options:['Recorrer cada elemento y acumular','Ordenarlo obligatoriamente','Eliminar sus datos'], answer:0},
      {topic:'if', q:'Si una condición principal falla y se evalúa otra, se usa:', options:['else if','public class','new Scanner'], answer:0},
      {topic:'vars', q:'¿Qué tipo evita perder decimales en un promedio?', options:['double','int','boolean'], answer:0},
      {topic:'arrays', q:'¿Qué representa nums[i]?', options:['El elemento del arreglo en la posición i','El tamaño completo del arreglo','El nombre de la clase'], answer:0}
    ]
  }
];

let selectedExam = EXAMS[0];
let lastResult = null;

const form = document.getElementById('examForm');
const resultBox = document.getElementById('examResultBox');
const feedback = document.getElementById('examFeedback');
const preview = document.getElementById('examLevelPreview');
const btnExportPdf = document.getElementById('btnExportPdf');

function renderExamCards(){
  const box = document.getElementById('examCards');
  box.innerHTML = EXAMS.map(exam => `
    <button type="button" class="exam-card ${exam.id === selectedExam.id ? 'active' : ''}" data-exam="${exam.id}">
      <span class="pill ${exam.id === 'avanzado' ? 'danger' : exam.id === 'intermedio' ? 'warning' : ''}">${exam.difficulty}</span>
      <strong>${exam.title}</strong>
      <small>${exam.questions.length} preguntas · ${exam.minutes} min · ${exam.minLevel}</small>
      <p>${exam.description}</p>
    </button>
  `).join('');

  box.querySelectorAll('.exam-card').forEach(card => {
    card.addEventListener('click', () => selectExam(card.dataset.exam));
  });
}

function selectExam(id){
  const exam = EXAMS.find(e => e.id === id);
  if(!exam) return;
  selectedExam = exam;
  lastResult = null;
  document.getElementById('selectedExamTitle').textContent = exam.title;
  document.getElementById('selectedExamDifficulty').textContent = exam.difficulty;
  document.getElementById('selectedExamDescription').textContent = exam.description;
  resetExam(false);
  renderExamCards();
  renderExam();
}

function renderExam(){
  form.innerHTML = selectedExam.questions.map((item, idx) => `
    <fieldset class="question-card">
      <legend><span>${idx + 1}</span> ${item.q}</legend>
      ${item.options.map((op, i) => `
        <label class="option-row">
          <input type="radio" name="q${idx}" value="${i}"> ${op}
        </label>
      `).join('')}
    </fieldset>
  `).join('');
}

function finishExam(){
  let correct = 0;
  const byTopic = {vars:{ok:0,total:0}, if:{ok:0,total:0}, loops:{ok:0,total:0}, arrays:{ok:0,total:0}};
  const details = [];

  selectedExam.questions.forEach((item, idx) => {
    byTopic[item.topic].total++;
    const selected = form.querySelector(`input[name="q${idx}"]:checked`);
    const selectedValue = selected ? Number(selected.value) : null;
    const isCorrect = selectedValue === item.answer;
    if(isCorrect){
      correct++;
      byTopic[item.topic].ok++;
    }
    details.push({
      number: idx + 1,
      question: item.q,
      selected: selectedValue === null ? 'Sin responder' : item.options[selectedValue],
      correct: item.options[item.answer],
      ok: isCorrect
    });
  });

  const percent = Math.round((correct / selectedExam.questions.length) * 100);
  const levelInfo = getLevel(percent);
  const strengths = buildStrengths(byTopic);
  const result = {
    examId: selectedExam.id,
    examTitle: selectedExam.title,
    difficulty: selectedExam.difficulty,
    correct,
    total: selectedExam.questions.length,
    percent,
    level: levelInfo.level,
    advice: levelInfo.advice,
    byTopic,
    strengths,
    details,
    at: new Date().toISOString()
  };

  lastResult = result;
  saveResult(result);
  renderResult(result);
  renderHistory();
  btnExportPdf.disabled = false;
}

function getLevel(percent){
  if(percent >= 90) return { level: 'Nivel 5 · Experto lógico', advice: 'Puedes resolver retos avanzados, arreglos y algoritmos combinados.' };
  if(percent >= 75) return { level: 'Nivel 4 · Avanzado', advice: 'Dominas la base. Refuerza problemas con bucles, condiciones y arreglos.' };
  if(percent >= 55) return { level: 'Nivel 3 · Intermedio', advice: 'Tienes una buena base. Practica más condicionales, bucles y lectura de código.' };
  if(percent >= 35) return { level: 'Nivel 2 · Aprendiz', advice: 'Empieza por retos básicos de variables, entrada/salida y condicionales simples.' };
  return { level: 'Nivel 1 · Novato lógico', advice: 'Refuerza desde cero: variables, tipos de datos y orden lógico entrada-proceso-salida.' };
}

function buildStrengths(byTopic){
  const labels = {vars:'Variables', if:'Condicionales', loops:'Bucles', arrays:'Arreglos'};
  return Object.entries(byTopic).map(([key, data]) => {
    const pct = data.total ? Math.round((data.ok / data.total) * 100) : 0;
    return { topic: labels[key], pct, ok: data.ok, total: data.total };
  });
}

function renderResult(result){
  resultBox.innerHTML = `<strong>${result.percent}%</strong><span>${result.level}<br>${result.correct} de ${result.total} correctas</span>`;
  if (preview) preview.textContent = `${result.level.replace(' · ', ' - ')} (${result.difficulty})`;
  feedback.className = 'feedback success';
  feedback.innerHTML = `<strong>Diagnóstico completado</strong><p>${result.advice}</p>`;
  setRubric('rubricVars', result.byTopic.vars);
  setRubric('rubricIf', result.byTopic.if);
  setRubric('rubricLoops', result.byTopic.loops);
  setRubric('rubricArrays', result.byTopic.arrays);
}

function setRubric(id, data){
  const el = document.getElementById(id);
  if(!data || !data.total){ el.textContent = '—'; el.className = ''; return; }
  const pct = Math.round((data.ok / data.total) * 100);
  el.textContent = pct >= 70 ? '●' : pct >= 40 ? '◐' : '○';
  el.className = pct >= 70 ? 'ok' : pct >= 40 ? 'medium' : '';
}

function saveResult(result){
  const history = getHistory();
  history.unshift(result);
  localStorage.setItem('codelevels_exam_history', JSON.stringify(history.slice(0, 10)));
  localStorage.setItem('codelevels_exam_result', JSON.stringify(result));
}

function getHistory(){
  try { return JSON.parse(localStorage.getItem('codelevels_exam_history') || '[]'); }
  catch(e){ return []; }
}

function renderHistory(){
  const box = document.getElementById('examHistory');
  const history = getHistory();
  if(!history.length){
    box.innerHTML = '<p class="muted-text">Aún no tienes exámenes registrados.</p>';
    return;
  }
  box.innerHTML = history.map((r, idx) => `
    <div class="history-item">
      <div><strong>${r.difficulty}</strong><span>${r.percent}% · ${r.level}</span><small>${formatDate(r.at)}</small></div>
      <button type="button" class="btn mini" data-history="${idx}">PDF</button>
    </div>
  `).join('');
  box.querySelectorAll('[data-history]').forEach(btn => {
    btn.addEventListener('click', () => exportPdf(history[Number(btn.dataset.history)]));
  });
}

function resetExam(clearSelection = true){
  if(clearSelection) form.reset();
  resultBox.innerHTML = '<strong>—</strong><span>Completa el examen para ver tu nivel.</span>';
  feedback.className = 'feedback';
  feedback.innerHTML = '<strong>Guía</strong><p>El examen clasifica al estudiante como Novato, Aprendiz, Intermedio, Avanzado o Experto.</p>';
  ['rubricVars','rubricIf','rubricLoops','rubricArrays'].forEach(id => { const e=document.getElementById(id); e.textContent='○'; e.className=''; });
  btnExportPdf.disabled = true;
}

function restore(){
  const last = localStorage.getItem('codelevels_exam_result');
  if(last){
    try {
      const result = JSON.parse(last);
      if (preview) preview.textContent = `${result.level.replace(' · ', ' - ')} (${result.difficulty})`;
      lastResult = result;
    } catch(e) {}
  }
  renderHistory();
}

function formatDate(iso){
  try { return new Date(iso).toLocaleString('es-PE', {dateStyle:'medium', timeStyle:'short'}); }
  catch(e){ return iso; }
}

function exportPdf(result = lastResult){
  if(!result){
    alert('Primero completa un examen para exportar el resultado.');
    return;
  }

  const strengthsRows = result.strengths.map(s => `
    <tr><td>${s.topic}</td><td>${s.ok}/${s.total}</td><td>${s.pct}%</td></tr>
  `).join('');

  const detailsRows = result.details.map(d => `
    <tr>
      <td>${d.number}</td>
      <td>${escapeHtml(d.question)}</td>
      <td>${escapeHtml(d.selected)}</td>
      <td>${escapeHtml(d.correct)}</td>
      <td>${d.ok ? 'Correcto' : 'Revisar'}</td>
    </tr>
  `).join('');

  const reportHtml = `
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Reporte PDF CodeLevels</title>
<style>
  body{font-family:Arial, sans-serif; color:#111827; margin:32px;}
  .header{display:flex; justify-content:space-between; gap:20px; align-items:flex-start; border-bottom:4px solid #3157ff; padding-bottom:18px; margin-bottom:22px;}
  .logo{width:58px;height:58px;border-radius:16px;background:linear-gradient(135deg,#3157ff,#06b6d4);color:white;display:grid;place-items:center;font-weight:900;font-size:22px;}
  h1{margin:0;font-size:26px;} h2{margin:24px 0 10px;font-size:18px;} p{line-height:1.55;}
  .badge{display:inline-block;padding:7px 12px;border-radius:999px;background:#eef3ff;color:#3157ff;font-weight:bold;}
  .summary{display:grid;grid-template-columns:repeat(4,1fr);gap:10px;margin:18px 0;}
  .card{border:1px solid #dbe4f3;border-radius:14px;padding:12px;background:#f8fafc;}
  .card strong{display:block;font-size:22px;color:#3157ff;}
  table{width:100%;border-collapse:collapse;margin-top:10px;font-size:12px;} th,td{border:1px solid #dbe4f3;padding:8px;text-align:left;vertical-align:top;} th{background:#eef3ff;}
  .advice{padding:14px;border-left:5px solid #16a34a;background:#f0fdf4;border-radius:10px;margin-top:12px;}
  .footer{margin-top:28px;font-size:11px;color:#667085;border-top:1px solid #dbe4f3;padding-top:10px;}
  @media print { body{margin:18mm;} .no-print{display:none;} }
</style>
</head>
<body>
  <div class="header">
    <div>
      <span class="badge">${result.difficulty}</span>
      <h1>Reporte de examen CodeLevels</h1>
      <p>Resultado del examen: <strong>${result.examTitle}</strong><br>Fecha: ${formatDate(result.at)}</p>
    </div>
    <div class="logo">CL</div>
  </div>

  <div class="summary">
    <div class="card"><span>Porcentaje</span><strong>${result.percent}%</strong></div>
    <div class="card"><span>Correctas</span><strong>${result.correct}/${result.total}</strong></div>
    <div class="card"><span>Nivel</span><strong>${result.level.split(' · ')[0]}</strong></div>
    <div class="card"><span>Dificultad</span><strong>${result.difficulty}</strong></div>
  </div>

  <h2>Nivel diagnosticado</h2>
  <p><strong>${result.level}</strong></p>
  <div class="advice"><strong>Recomendación:</strong><br>${result.advice}</div>

  <h2>Desempeño por tema</h2>
  <table><thead><tr><th>Tema</th><th>Aciertos</th><th>Porcentaje</th></tr></thead><tbody>${strengthsRows}</tbody></table>

  <h2>Detalle de respuestas</h2>
  <table><thead><tr><th>#</th><th>Pregunta</th><th>Respuesta del alumno</th><th>Respuesta correcta</th><th>Estado</th></tr></thead><tbody>${detailsRows}</tbody></table>

  <div class="footer">CodeLevels · Plataforma de aprendizaje de lógica de programación para Java y NetBeans.</div>
  <script>window.onload = () => setTimeout(() => window.print(), 350);<\/script>
</body>
</html>`;

  const reportWindow = window.open('', '_blank');
  if(!reportWindow){
    alert('El navegador bloqueó la ventana emergente. Permite pop-ups para exportar el PDF.');
    return;
  }
  reportWindow.document.open();
  reportWindow.document.write(reportHtml);
  reportWindow.document.close();
}

function escapeHtml(text){
  return String(text).replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c]));
}

document.getElementById('btnFinishExam').addEventListener('click', finishExam);
document.getElementById('btnResetExam').addEventListener('click', () => resetExam(true));
document.getElementById('btnExportPdf').addEventListener('click', () => exportPdf(lastResult));
document.getElementById('btnClearHistory').addEventListener('click', () => {
  if(confirm('¿Deseas borrar el historial local de exámenes?')){
    localStorage.removeItem('codelevels_exam_history');
    localStorage.removeItem('codelevels_exam_result');
    lastResult = null;
    if (preview) preview.textContent = 'Sin evaluar';
    renderHistory();
    resetExam(true);
  }
});

renderExamCards();
renderExam();
restore();
