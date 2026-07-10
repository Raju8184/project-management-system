// ── Data Store ──────────────────────────────────────────────────────────────

const STAGES = ['PLANNING', 'DEVELOPMENT', 'TESTING', 'DEPLOYMENT', 'MAINTENANCE'];

const company = {
    name: 'Global Tech Corp',
    employees: [
        { id: 'E001', name: 'Alice Johnson', designation: 'Senior Designer', skills: ['Figma', 'CSS', 'UX'] },
        { id: 'E002', name: 'Mark Spencer', designation: 'Backend Engineer', skills: ['Java', 'Spring', 'SQL'] },
        { id: 'E003', name: 'Sarah Connor', designation: 'Data Scientist', skills: ['Python', 'TensorFlow'] },
        { id: 'E004', name: 'James Wilson', designation: 'Mobile Developer', skills: ['Swift', 'Kotlin'] },
    ],
    clients: [
        { id: 'C001', name: 'Acme Retail', org: 'Acme Inc.', email: 'contact@acme.com' },
        { id: 'C002', name: 'Stark Industries', org: 'Stark Ind.', email: 'tony@stark.com' },
        { id: 'C003', name: 'Wayne Corp', org: 'Wayne Ent.', email: 'wayne@corp.com' },
    ],
    projects: [
        { id: 'PRJ001', name: 'Acme E-Commerce Portal', type: 'Web', budget: 50000, stage: 2, clientIdx: 0, assignments: [{ empIdx: 0, role: 'Lead Designer' }] },
        { id: 'PRJ002', name: 'Stark Health Tracker', type: 'Mobile', budget: 80000, stage: 1, clientIdx: 1, assignments: [{ empIdx: 3, role: 'App Developer' }] },
        { id: 'PRJ003', name: 'Crime Predictor AI', type: 'Data', budget: 120000, stage: 0, clientIdx: 2, assignments: [{ empIdx: 2, role: 'ML Engineer' }] },
        { id: 'PRJ004', name: 'Employee Intranet Portal', type: 'Web', budget: 20000, stage: 0, clientIdx: 0, assignments: [] },
    ],
};

// ── View Navigation ──────────────────────────────────────────────────────────

function showView(name) {
    document.querySelectorAll('.view').forEach(v => v.classList.remove('active'));
    document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
    document.getElementById('view-' + name).classList.add('active');
    document.querySelector(`.nav-btn[data-view="${name}"]`).classList.add('active');
    renderAll();
}

document.querySelectorAll('.nav-btn').forEach(btn => {
    btn.addEventListener('click', () => showView(btn.dataset.view));
});

// ── Render Functions ─────────────────────────────────────────────────────────

function renderAll() {
    renderOverview();
    renderProjects();
    renderClients();
    renderEmployees();
    renderAssignments();
    populateClientDropdowns();
}

function stageColor(stage) {
    return `<span class="stage-badge stage-${STAGES[stage]}">${STAGES[stage]}</span>`;
}

function budgetFmt(n) {
    return '$' + Number(n).toLocaleString();
}

// Overview
function renderOverview() {
    document.getElementById('company-name-side').textContent = company.name;
    document.getElementById('company-name-badge').textContent = company.name;
    document.getElementById('stat-projects').textContent = company.projects.length;
    document.getElementById('stat-clients').textContent = company.clients.length;
    document.getElementById('stat-employees').textContent = company.employees.length;
    const totalAssignments = company.projects.reduce((s, p) => s + p.assignments.length, 0);
    document.getElementById('stat-assignments').textContent = totalAssignments;

    // Recent projects
    const rpl = document.getElementById('recent-projects-list');
    rpl.innerHTML = company.projects.slice(0, 5).map(p => `
    <div class="mini-item">
      <div class="mini-dot" style="background:var(--blue)"></div>
      <span><strong>${p.name}</strong> — ${STAGES[p.stage]}</span>
    </div>`).join('');

    // Employees
    const rel = document.getElementById('recent-employees-list');
    rel.innerHTML = company.employees.slice(0, 5).map(e => `
    <div class="mini-item">
      <div class="mini-dot" style="background:var(--amber)"></div>
      <span><strong>${e.name}</strong> — ${e.designation}</span>
    </div>`).join('');
}

// Projects table
function renderProjects() {
    const body = document.getElementById('projects-body');
    if (!body) return;
    body.innerHTML = company.projects.map((p, i) => `
    <tr>
      <td>${p.id}</td>
      <td><strong>${p.name}</strong></td>
      <td>${p.type}</td>
      <td>${budgetFmt(p.budget)}</td>
      <td>${stageColor(p.stage)}</td>
      <td class="action-cell">
        <button class="btn btn-blue btn-sm" onclick="advanceStage(${i})">▶ Advance</button>
        <button class="btn btn-danger btn-sm" onclick="deleteProject(${i})">🗑</button>
      </td>
    </tr>`).join('') || '<tr><td colspan="6" style="text-align:center;color:var(--muted);padding:28px">No projects yet</td></tr>';
}

// Clients table
function renderClients() {
    const body = document.getElementById('clients-body');
    if (!body) return;
    body.innerHTML = company.clients.map((c, i) => {
        const count = company.projects.filter(p => p.clientIdx === i).length;
        return `<tr>
      <td>${c.id}</td>
      <td><strong>${c.name}</strong></td>
      <td>${c.org}</td>
      <td>${c.email}</td>
      <td>${count} project(s)</td>
      <td class="action-cell">
        <button class="btn btn-danger btn-sm" onclick="deleteClient(${i})">🗑 Remove</button>
      </td>
    </tr>`;
    }).join('') || '<tr><td colspan="6" style="text-align:center;color:var(--muted);padding:28px">No clients yet</td></tr>';
}

// Employees table
function renderEmployees() {
    const body = document.getElementById('employees-body');
    if (!body) return;
    body.innerHTML = company.employees.map((e, i) => {
        const active = company.projects.filter(p => p.assignments.some(a => a.empIdx === i)).length;
        const skillsHtml = e.skills.map(s => `<span class="skill-chip">${s}</span>`).join('') || '<span style="color:var(--muted)">—</span>';
        return `<tr>
      <td>${e.id}</td>
      <td><strong>${e.name}</strong></td>
      <td>${e.designation}</td>
      <td>${skillsHtml}</td>
      <td>${active} project(s)</td>
      <td class="action-cell">
        <button class="btn btn-blue btn-sm" onclick="openAssignModal(${i})">📋 Assign</button>
        <button class="btn btn-amber btn-sm" onclick="openSkillModal(${i})">+ Skill</button>
        <button class="btn btn-danger btn-sm" onclick="deleteEmployee(${i})">🗑</button>
      </td>
    </tr>`;
    }).join('') || '<tr><td colspan="6" style="text-align:center;color:var(--muted);padding:28px">No employees yet</td></tr>';
}

// Assignments table
function renderAssignments() {
    const body = document.getElementById('assignments-body');
    if (!body) return;
    const rows = [];
    company.projects.forEach(p => {
        p.assignments.forEach(a => {
            const emp = company.employees[a.empIdx];
            if (!emp) return;
            rows.push(`<tr>
        <td>
          <strong>${emp.name}</strong><br>
          <span style="color:var(--muted);font-size:11px">${emp.skills.map(s => `<span class="skill-chip">${s}</span>`).join('')}</span>
        </td>
        <td>${a.role}</td>
        <td><strong>${p.name}</strong></td>
        <td>${stageColor(p.stage)}</td>
        <td><span style="color:var(--muted);font-size:12px">Today</span></td>
      </tr>`);
        });
    });
    body.innerHTML = rows.join('') || '<tr><td colspan="5" style="text-align:center;color:var(--muted);padding:28px">No assignments yet</td></tr>';
}

function populateClientDropdowns() {
    ['pClient', 'assign-project-select'].forEach(id => {
        const el = document.getElementById(id);
        if (!el) return;
        // only repopulate the project dropdown when opening modal
    });
    const pc = document.getElementById('pClient');
    if (pc) {
        pc.innerHTML = company.clients.map((c, i) => `<option value="${i}">${c.name}</option>`).join('');
    }
}

// ── CRUD Actions ─────────────────────────────────────────────────────────────

function advanceStage(i) {
    if (company.projects[i].stage < STAGES.length - 1) {
        company.projects[i].stage++;
        renderAll();
        toast('Stage advanced to ' + STAGES[company.projects[i].stage]);
    } else {
        toast('Project is already at final stage.');
    }
}

function deleteProject(i) {
    company.projects.splice(i, 1);
    renderAll();
    toast('Project deleted.');
}

function deleteClient(i) {
    company.clients.splice(i, 1);
    // Remove orphaned project references
    company.projects.forEach(p => { if (p.clientIdx === i) p.clientIdx = null; });
    renderAll();
    toast('Client removed.');
}

function deleteEmployee(i) {
    company.employees.splice(i, 1);
    // Patch assignment indices
    company.projects.forEach(p => {
        p.assignments = p.assignments.filter(a => a.empIdx !== i);
        p.assignments.forEach(a => { if (a.empIdx > i) a.empIdx--; });
    });
    renderAll();
    toast('Employee removed.');
}

// ── Form Submissions ──────────────────────────────────────────────────────────

document.getElementById('form-project').addEventListener('submit', e => {
    e.preventDefault();
    const name = document.getElementById('pName').value.trim();
    const budget = parseFloat(document.getElementById('pBudget').value);
    const type = document.getElementById('pType').value;
    const cIdx = parseInt(document.getElementById('pClient').value);
    if (!name || isNaN(budget)) return;
    const id = 'PRJ' + String(company.projects.length + 1).padStart(3, '0');
    company.projects.push({ id, name, type, budget, stage: 0, clientIdx: isNaN(cIdx) ? null : cIdx, assignments: [] });
    renderAll();
    e.target.reset();
    toast('Project registered!');
});

document.getElementById('form-client').addEventListener('submit', e => {
    e.preventDefault();
    const name = document.getElementById('cName').value.trim();
    const org = document.getElementById('cOrg').value.trim();
    const email = document.getElementById('cEmail').value.trim();
    if (!name) return;
    const id = 'C' + String(company.clients.length + 1).padStart(3, '0');
    company.clients.push({ id, name, org, email });
    renderAll();
    e.target.reset();
    toast('Client onboarded!');
});

document.getElementById('form-employee').addEventListener('submit', e => {
    e.preventDefault();
    const name = document.getElementById('eName').value.trim();
    const des = document.getElementById('eDes').value.trim();
    if (!name) return;
    const id = 'E' + String(company.employees.length + 1).padStart(3, '0');
    company.employees.push({ id, name, designation: des, skills: [] });
    renderAll();
    e.target.reset();
    toast('Employee hired!');
});

// ── Assign Modal ──────────────────────────────────────────────────────────────

function openAssignModal(empIdx) {
    document.getElementById('assign-emp-idx').value = empIdx;
    document.getElementById('assign-emp-name').value = company.employees[empIdx].name;
    document.getElementById('assign-role').value = '';
    const sel = document.getElementById('assign-project-select');
    sel.innerHTML = company.projects.map((p, i) => `<option value="${i}">${p.name}</option>`).join('');
    document.getElementById('modal-assign').style.display = 'grid';
}

function confirmAssign() {
    const empIdx = parseInt(document.getElementById('assign-emp-idx').value);
    const projIdx = parseInt(document.getElementById('assign-project-select').value);
    const role = document.getElementById('assign-role').value.trim() || 'Member';
    company.projects[projIdx].assignments.push({ empIdx, role });
    closeModal('modal-assign');
    renderAll();
    toast(`${company.employees[empIdx].name} assigned to ${company.projects[projIdx].name}!`);
}

// ── Skill Modal ───────────────────────────────────────────────────────────────

function openSkillModal(empIdx) {
    document.getElementById('skill-emp-idx').value = empIdx;
    document.getElementById('skill-emp-name').value = company.employees[empIdx].name;
    document.getElementById('skill-input').value = '';
    document.getElementById('modal-skill').style.display = 'grid';
}

function confirmSkill() {
    const empIdx = parseInt(document.getElementById('skill-emp-idx').value);
    const skill = document.getElementById('skill-input').value.trim();
    if (!skill) { toast('Enter a skill name.'); return; }
    company.employees[empIdx].skills.push(skill);
    closeModal('modal-skill');
    renderAll();
    toast(`Skill "${skill}" added!`);
}

// ── Utils ─────────────────────────────────────────────────────────────────────

function closeModal(id) {
    document.getElementById(id).style.display = 'none';
}

// Close modal on overlay click
document.querySelectorAll('.modal-overlay').forEach(overlay => {
    overlay.addEventListener('click', e => {
        if (e.target === overlay) overlay.style.display = 'none';
    });
});

let toastTimer;
function toast(msg) {
    const t = document.getElementById('toast');
    t.textContent = msg;
    t.classList.add('show');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => t.classList.remove('show'), 3000);
}

// ── Boot ──────────────────────────────────────────────────────────────────────
renderAll();
