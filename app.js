// Native Domain Models simulating Java Logic in JS

class Client {
    constructor(client_id, name, contact_email, company_name) {
        this.client_id = client_id;
        this.name = name;
        this.contact_email = contact_email;
        this.company_name = company_name;
        this.projects = [];
    }
}

class Project {
    constructor(project_id, name, budget, type) {
        this.project_id = project_id;
        this.name = name;
        this.budget = budget;
        this.type = type;
        this.stage = "PLANNING";
    }
}

class Company {
    constructor(name) {
        this.name = name;
        this.clients = [];
        this.projects = [];
        this.employees = [];
    }
    
    registerClient(client) {
        this.clients.push(client);
    }
    
    registerProject(project) {
        this.projects.push(project);
    }
}

// 1. System State Bootstrapping
const company = new Company("Global Tech Corp");
document.getElementById('company-name').textContent = company.name;

// 2. Pre-seed mock data
company.registerClient(new Client("C001", "Wayne Bruce", "bruce@wayne.com", "Wayne Enterprise"));
company.registerClient(new Client("C002", "Tony Stark", "tony@stark.com", "Stark Ind"));
company.registerProject(new Project("PRJ-101", "Stark OS Rewrite", 150000, "Web"));
company.registerProject(new Project("PRJ-102", "Wayne Security App", 85000, "Mobile"));
company.employees = [1,2,3,4,5,6]; // Mock employee count

// 3. UI Controller
const AppController = {
    init() {
        this.bindEvents();
        this.renderAll();
    },

    bindEvents() {
        // Navigation Handler
        document.querySelectorAll('.nav-item').forEach(el => {
            el.addEventListener('click', (e) => {
                document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
                document.querySelectorAll('.content-section').forEach(sec => sec.classList.remove('active'));
                
                e.target.classList.add('active');
                const targetId = e.target.getAttribute('data-target');
                document.getElementById(targetId).classList.add('active');
            });
        });

        // Form Submit Handler
        document.getElementById('create-project-form').addEventListener('submit', (e) => {
            e.preventDefault();
            const name = document.getElementById('proj-name').value;
            const budget = parseFloat(document.getElementById('proj-budget').value);
            const type = document.getElementById('proj-type').value;
            
            const newProj = new Project(`PRJ-${100 + company.projects.length + 1}`, name, budget, type);
            company.registerProject(newProj);
            
            e.target.reset();
            this.renderAll();
        });
    },

    renderAll() {
        // Update Grid Stats
        document.getElementById('stat-projects').textContent = company.projects.length;
        document.getElementById('stat-clients').textContent = company.clients.length;
        document.getElementById('stat-employees').textContent = company.employees.length;

        // Populate Form Select Dropdown
        const clientSelect = document.getElementById('proj-client');
        clientSelect.innerHTML = '';
        company.clients.forEach(c => {
            const opt = document.createElement('option');
            opt.value = c.client_id;
            opt.textContent = `${c.name} (${c.company_name})`;
            clientSelect.appendChild(opt);
        });

        // Populate Projects Table
        const projBody = document.getElementById('projects-table-body');
        projBody.innerHTML = '';
        company.projects.forEach(p => {
            let typeColor = p.type === 'Web' ? '#3b82f6' : (p.type === 'Mobile' ? '#8b5cf6' : '#10b981');
            projBody.innerHTML += `
                <tr>
                    <td>${p.project_id}</td>
                    <td>${p.name}</td>
                    <td><span style="background:rgba(255,255,255,0.1); border-left:3px solid ${typeColor}; padding:4px 8px; border-radius:4px; font-size:0.8rem;">${p.type}</span></td>
                    <td>$${p.budget.toLocaleString()}</td>
                    <td>${p.stage}</td>
                </tr>
            `;
        });

        // Populate Clients Table
        const cliBody = document.getElementById('clients-table-body');
        cliBody.innerHTML = '';
        company.clients.forEach(c => {
            cliBody.innerHTML += `
                <tr>
                    <td>${c.client_id}</td>
                    <td>${c.name}</td>
                    <td>${c.company_name}</td>
                    <td>${c.contact_email}</td>
                </tr>
            `;
        });
    }
};

// Launch App Core
document.addEventListener('DOMContentLoaded', () => AppController.init());
