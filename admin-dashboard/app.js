// --- State Management ---
let currentView = 'dashboard';
let notices = [];
let projects = [];
let feedbacks = [];
let currentFeedbackId = null;

// --- Initialization ---
document.addEventListener('DOMContentLoaded', async () => {
    checkUser();
    setupEventListeners();
});

async function checkUser() {
    const { data: { session } } = await supabaseClient.auth.getSession();
    if (session) {
        showDashboard();
    } else {
        showLogin();
    }
}

function showLogin() {
    document.getElementById('auth-section').classList.remove('hidden');
    document.getElementById('dashboard-section').classList.add('hidden');
}

function showDashboard() {
    document.getElementById('auth-section').classList.add('hidden');
    document.getElementById('dashboard-section').classList.remove('hidden');
    loadView('dashboard');
}

// --- Navigation ---
function setupEventListeners() {
    // Auth
    document.getElementById('login-form').addEventListener('submit', handleLogin);
    document.getElementById('logout-btn').addEventListener('click', handleLogout);

    // Sidebar
    document.querySelectorAll('.nav-link[data-view]').forEach(link => {
        link.addEventListener('click', (e) => {
            const view = e.currentTarget.getAttribute('data-view');
            loadView(view);
        });
    });

    // Forms
    document.getElementById('notice-form').addEventListener('submit', saveNotice);
    document.getElementById('project-form').addEventListener('submit', saveProject);
}

async function loadView(view) {
    currentView = view;
    
    // Update Sidebar UI
    document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
    document.querySelector(`.nav-link[data-view="${view}"]`)?.classList.add('active');

    // Hide all views
    document.querySelectorAll('.view').forEach(v => v.classList.add('hidden'));
    document.getElementById(`view-${view}`).classList.remove('hidden');

    // Load Data
    if (view === 'dashboard') updateDashboardStats();
    if (view === 'notices') fetchNotices();
    if (view === 'projects') fetchProjects();
    if (view === 'feedbacks') fetchFeedbacks();
    
    lucide.createIcons();
}

// --- Auth Handlers ---
async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    const errorEl = document.getElementById('login-error');

    const { error } = await supabaseClient.auth.signInWithPassword({ email, password });

    if (error) {
        errorEl.textContent = error.message;
        errorEl.style.display = 'block';
    } else {
        showDashboard();
    }
}

async function handleLogout() {
    await supabaseClient.auth.signOut();
    showLogin();
}

// --- Notices CRUD ---
async function fetchNotices() {
    const { data, error } = await supabaseClient
        .from('notices')
        .select('*')
        .order('date', { ascending: false });

    if (error) return console.error(error);
    notices = data;
    renderNotices();
}

function renderNotices() {
    const tbody = document.getElementById('notices-table-body');
    tbody.innerHTML = notices.map(notice => `
        <tr>
            <td>${notice.title_en}</td>
            <td>${notice.category}</td>
            <td><span class="badge badge-${notice.priority}">${notice.priority}</span></td>
            <td>${new Date(notice.date).toLocaleDateString()}</td>
            <td>
                <button class="btn" onclick="editNotice('${notice.notice_id}')" style="padding: 0.25rem;">
                    <i data-lucide="edit-2" style="width: 16px;"></i>
                </button>
                <button class="btn" onclick="deleteNotice('${notice.notice_id}')" style="padding: 0.25rem; color: var(--danger);">
                    <i data-lucide="trash-2" style="width: 16px;"></i>
                </button>
            </td>
        </tr>
    `).join('');
    lucide.createIcons();
}

function openNoticeModal(id = null) {
    const modal = document.getElementById('notice-modal');
    const form = document.getElementById('notice-form');
    const title = document.getElementById('notice-modal-title');
    
    form.reset();
    document.getElementById('notice-id').value = '';
    title.textContent = 'Create Notice';

    if (id) {
        const notice = notices.find(n => n.notice_id === id);
        if (notice) {
            title.textContent = 'Edit Notice';
            document.getElementById('notice-id').value = notice.notice_id;
            document.getElementById('notice-title-en').value = notice.title_en;
            document.getElementById('notice-title-kn').value = notice.title_kn;
            document.getElementById('notice-content-en').value = notice.content_en;
            document.getElementById('notice-content-kn').value = notice.content_kn;
            document.getElementById('notice-category').value = notice.category;
            document.getElementById('notice-priority').value = notice.priority;
        }
    }
    
    modal.classList.remove('hidden');
}

async function saveNotice(e) {
    e.preventDefault();
    const id = document.getElementById('notice-id').value;
    const noticeData = {
        title_en: document.getElementById('notice-title-en').value,
        title_kn: document.getElementById('notice-title-kn').value,
        content_en: document.getElementById('notice-content-en').value,
        content_kn: document.getElementById('notice-content-kn').value,
        category: document.getElementById('notice-category').value,
        priority: document.getElementById('notice-priority').value,
        date: new Date().toISOString()
    };

    let result;
    if (id) {
        result = await supabaseClient.from('notices').update(noticeData).eq('notice_id', id);
    } else {
        result = await supabaseClient.from('notices').insert([noticeData]);
    }

    if (result.error) alert(result.error.message);
    else {
        closeModals();
        fetchNotices();
    }
}

async function deleteNotice(id) {
    if (confirm('Are you sure you want to delete this notice?')) {
        const { error } = await supabaseClient.from('notices').delete().eq('notice_id', id);
        if (error) alert(error.message);
        else fetchNotices();
    }
}

// --- Projects CRUD ---
async function fetchProjects() {
    const { data, error } = await supabaseClient.from('projects').select('*');
    if (error) return console.error(error);
    projects = data;
    renderProjects();
}

function renderProjects() {
    const tbody = document.getElementById('projects-table-body');
    tbody.innerHTML = projects.map(project => `
        <tr>
            <td>${project.title_en}</td>
            <td>${project.budget}</td>
            <td>
                <div style="width: 100px; background: var(--bg-primary); border-radius: 4px; height: 8px;">
                    <div style="width: ${project.progress_percentage}%; background: var(--accent); height: 100%; border-radius: 4px;"></div>
                </div>
                <span style="font-size: 0.75rem;">${project.progress_percentage}%</span>
            </td>
            <td>
                <button class="btn" onclick="editProject('${project.project_id}')" style="padding: 0.25rem;">
                    <i data-lucide="edit-2" style="width: 16px;"></i>
                </button>
                <button class="btn" onclick="deleteProject('${project.project_id}')" style="padding: 0.25rem; color: var(--danger);">
                    <i data-lucide="trash-2" style="width: 16px;"></i>
                </button>
            </td>
        </tr>
    `).join('');
    lucide.createIcons();
}

function openProjectModal(id = null) {
    const modal = document.getElementById('project-modal');
    const form = document.getElementById('project-form');
    const title = document.getElementById('project-modal-title');
    
    form.reset();
    document.getElementById('project-id').value = '';
    title.textContent = 'Create Project';

    if (id) {
        const project = projects.find(p => p.project_id === id);
        if (project) {
            title.textContent = 'Edit Project';
            document.getElementById('project-id').value = project.project_id;
            document.getElementById('project-title-en').value = project.title_en;
            document.getElementById('project-title-kn').value = project.title_kn;
            document.getElementById('project-desc-en').value = project.description_en;
            document.getElementById('project-desc-kn').value = project.description_kn;
            document.getElementById('project-budget').value = project.budget;
            document.getElementById('project-progress').value = project.progress_percentage;
            document.getElementById('project-before-url').value = project.before_url;
            document.getElementById('project-current-url').value = project.current_url;
        }
    }
    
    modal.classList.remove('hidden');
}

async function saveProject(e) {
    e.preventDefault();
    const id = document.getElementById('project-id').value;
    const projectData = {
        title_en: document.getElementById('project-title-en').value,
        title_kn: document.getElementById('project-title-kn').value,
        description_en: document.getElementById('project-desc-en').value,
        description_kn: document.getElementById('project-desc-kn').value,
        budget: document.getElementById('project-budget').value,
        progress_percentage: parseInt(document.getElementById('project-progress').value),
        before_url: document.getElementById('project-before-url').value,
        current_url: document.getElementById('project-current-url').value
    };

    let result;
    if (id) {
        result = await supabaseClient.from('projects').update(projectData).eq('project_id', id);
    } else {
        result = await supabaseClient.from('projects').insert([projectData]);
    }

    if (result.error) alert(result.error.message);
    else {
        closeModals();
        fetchProjects();
    }
}

async function deleteProject(id) {
    if (confirm('Are you sure you want to delete this project?')) {
        const { error } = await supabaseClient.from('projects').delete().eq('project_id', id);
        if (error) alert(error.message);
        else fetchProjects();
    }
}

// --- Feedbacks CRUD ---
async function fetchFeedbacks() {
    const { data, error } = await supabaseClient
        .from('feedbacks')
        .select('*, projects(title_en)')
        .order('timestamp', { ascending: false });

    if (error) return console.error(error);
    feedbacks = data;
    renderFeedbacks();
}

function renderFeedbacks() {
    const tbody = document.getElementById('feedbacks-table-body');
    tbody.innerHTML = feedbacks.map(fb => `
        <tr>
            <td>${fb.is_anonymous ? 'Anonymous' : (fb.user_id?.substring(0, 8) || 'User')}</td>
            <td>${fb.projects?.title_en || 'Unknown Project'}</td>
            <td>
                <div class="rating-stars">
                    ${'★'.repeat(fb.rating)}${'☆'.repeat(5 - fb.rating)}
                </div>
            </td>
            <td><div class="feedback-text-truncate">${fb.feedback_text}</div></td>
            <td><span class="badge badge-${fb.status}">${fb.status}</span></td>
            <td>${new Date(fb.timestamp).toLocaleDateString()}</td>
            <td>
                <button class="btn" onclick="viewFeedback('${fb.feedback_id}')" style="padding: 0.25rem;">
                    <i data-lucide="eye" style="width: 16px;"></i>
                </button>
                <button class="btn" onclick="deleteFeedback('${fb.feedback_id}')" style="padding: 0.25rem; color: var(--danger);">
                    <i data-lucide="trash-2" style="width: 16px;"></i>
                </button>
            </td>
        </tr>
    `).join('');
    lucide.createIcons();
}

function viewFeedback(id) {
    const fb = feedbacks.find(f => f.feedback_id === id);
    if (!fb) return;

    currentFeedbackId = id;
    const modal = document.getElementById('feedback-modal');
    const content = document.getElementById('feedback-detail-content');
    const statusSelect = document.getElementById('feedback-status-update');

    content.innerHTML = `
        <p><strong>Project:</strong> ${fb.projects?.title_en || 'N/A'}</p>
        <p><strong>Category:</strong> ${fb.category}</p>
        <p><strong>Rating:</strong> ${'★'.repeat(fb.rating)}${'☆'.repeat(5 - fb.rating)}</p>
        <p><strong>User:</strong> ${fb.is_anonymous ? 'Anonymous' : fb.user_id}</p>
        <p><strong>Date:</strong> ${new Date(fb.timestamp).toLocaleString()}</p>
        <hr style="margin: 1rem 0; border: none; border-top: 1px solid var(--border);">
        <p><strong>Comment:</strong></p>
        <p style="background: var(--bg-primary); padding: 1rem; border-radius: 0.5rem; margin-top: 0.5rem;">${fb.feedback_text}</p>
    `;

    statusSelect.value = fb.status;
    modal.classList.remove('hidden');
}

async function saveFeedbackStatus() {
    const newStatus = document.getElementById('feedback-status-update').value;
    if (!currentFeedbackId) return;

    const { error } = await supabaseClient
        .from('feedbacks')
        .update({ status: newStatus })
        .eq('feedback_id', currentFeedbackId);

    if (error) alert(error.message);
    else {
        closeModals();
        fetchFeedbacks();
    }
}

async function deleteFeedback(id) {
    if (confirm('Are you sure you want to delete this feedback?')) {
        const { error } = await supabaseClient.from('feedbacks').delete().eq('feedback_id', id);
        if (error) alert(error.message);
        else fetchFeedbacks();
    }
}

// --- Helpers ---
function closeModals() {
    document.querySelectorAll('.modal-backdrop').forEach(m => m.classList.add('hidden'));
}

async function updateDashboardStats() {
    const { count: noticeCount } = await supabaseClient.from('notices').select('*', { count: 'exact', head: true });
    const { data: projectData } = await supabaseClient.from('projects').select('progress_percentage');
    const { count: feedbackCount } = await supabaseClient.from('feedbacks').select('*', { count: 'exact', head: true });
    
    document.getElementById('count-notices').textContent = noticeCount || 0;
    document.getElementById('count-projects').textContent = projectData?.length || 0;
    document.getElementById('count-feedbacks').textContent = feedbackCount || 0;
    
    if (projectData && projectData.length > 0) {
        const total = projectData.reduce((acc, curr) => {
            const val = Number(curr.progress_percentage) || 0;
            return acc + val;
        }, 0);
        const avg = total / projectData.length;
        document.getElementById('avg-progress').textContent = Math.round(avg) + '%';
    } else {
        document.getElementById('avg-progress').textContent = '0%';
    }
}

// Global functions for inline onclicks
window.editNotice = openNoticeModal;
window.editProject = openProjectModal;
window.closeModals = closeModals;
window.deleteNotice = deleteNotice;
window.deleteProject = deleteProject;
window.viewFeedback = viewFeedback;
window.saveFeedbackStatus = saveFeedbackStatus;
window.deleteFeedback = deleteFeedback;
