const flow = document.getElementById('flow');
const feedback = document.getElementById('labFeedback');

document.querySelectorAll('[data-chip]').forEach(btn => {
  btn.addEventListener('click', () => {
    const item = document.createElement('div');
    item.className = 'flow-step';
    item.innerHTML = `<span>${btn.dataset.chip}</span><button class="btn-remove">×</button>`;
    item.querySelector('button').addEventListener('click', () => item.remove());
    flow.appendChild(item);
  });
});

document.getElementById('analyze').addEventListener('click', () => {
  const steps = [...flow.querySelectorAll('.flow-step')].map(x => x.textContent);
  const hasInput = steps.some(s => s.includes('Entrada'));
  const hasProcess = steps.some(s => s.includes('Operación') || s.includes('variable'));
  const hasOutput = steps.some(s => s.includes('Mostrar'));
  if (hasInput && hasProcess && hasOutput) {
    feedback.className = 'feedback success';
    feedback.innerHTML = '<strong>✅ Flujo completo</strong><p>Tu algoritmo tiene entrada, proceso y salida.</p>';
  } else {
    feedback.className = 'feedback error';
    feedback.innerHTML = '<strong>⚠️ Flujo incompleto</strong><p>Todo algoritmo debe tener entrada, proceso y salida.</p>';
  }
});
