const responses = {
  variables: 'Una variable es una caja con nombre donde guardas un dato. En Java: int edad = 18; Primero define el tipo, luego el nombre y finalmente el valor.',
  condicionales: 'Una condicional permite tomar decisiones. En Java se usa if / else. Ejemplo: si nota >= 13 entonces aprobado, si no, desaprobado.',
  bucles: 'Un bucle repite una acción. Usa for cuando sabes cuántas veces repetir y while cuando depende de una condición.',
  entrada: 'La entrada de datos permite leer información del usuario. En Java se usa Scanner: Scanner sc = new Scanner(System.in); int n = sc.nextInt();',
  salida: 'La salida muestra resultados. En Java se usa System.out.println(). Sirve para comunicar al usuario lo que calculó el programa.',
  operaciones: 'Las operaciones básicas son +, -, *, / y %. El operador % devuelve el residuo y sirve para saber si un número es par o impar.',
  arreglos: 'Un arreglo guarda varios valores del mismo tipo. En Java: int[] notas = new int[5]; Se recorre normalmente con un bucle for.',
  default: 'Primero identifica Entrada, Proceso y Salida. Luego revisa si necesitas una decisión o una repetición. Esa estructura te ayuda a pasar la lógica a Java.'
};

function detectTopic(question) {
  const q = question.toLowerCase();
  if (q.includes('variable') || q.includes('dato')) return 'variables';
  if (q.includes('if') || q.includes('condicion') || q.includes('decisión') || q.includes('decision')) return 'condicionales';
  if (q.includes('bucle') || q.includes('for') || q.includes('while') || q.includes('repetir')) return 'bucles';
  if (q.includes('scanner') || q.includes('leer') || q.includes('entrada')) return 'entrada';
  if (q.includes('mostrar') || q.includes('println') || q.includes('salida')) return 'salida';
  if (q.includes('suma') || q.includes('resta') || q.includes('multiplica') || q.includes('operaci')) return 'operaciones';
  if (q.includes('arreglo') || q.includes('array') || q.includes('vector')) return 'arreglos';
  return 'default';
}

function appendMessage(type, html) {
  const box = document.getElementById('chatBox');
  const div = document.createElement('div');
  div.className = `chat-msg ${type}`;
  div.innerHTML = html;
  box.appendChild(div);
  box.scrollTop = box.scrollHeight;
}

function askTutor(text) {
  const question = (text || document.getElementById('tutorQuestion').value || '').trim();
  if (!question) return;
  appendMessage('user', question);
  const topic = detectTopic(question);
  setTimeout(() => appendMessage('tutor', `<strong>🎓 Tutor CodeLevels:</strong> ${responses[topic]}<br><em>Tip: intenta representarlo como bloque lógico antes de escribir Java.</em>`), 250);
  document.getElementById('tutorQuestion').value = '';
}

document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('btnAskTutor')?.addEventListener('click', () => askTutor());
  document.getElementById('tutorQuestion')?.addEventListener('keydown', e => { if (e.key === 'Enter') askTutor(); });
  document.querySelectorAll('.tutor-topic').forEach(btn => btn.addEventListener('click', () => askTutor(btn.dataset.topic)));
});
