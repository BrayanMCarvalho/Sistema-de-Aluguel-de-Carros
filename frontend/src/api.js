let csrf;
export async function refreshCsrf() {
    const res = await fetch('/api/auth/csrf', { credentials: 'same-origin' });
    if (!res.ok) throw new Error('Não foi possível conectar ao servidor.');
    csrf = await res.json();
}
export async function api(path, method = 'GET', body, form = false) {
    const headers = {};
    if (method !== 'GET') { if (!csrf) await refreshCsrf(); headers[csrf.headerName] = csrf.token; }
    if (body) headers['Content-Type'] = form ? 'application/x-www-form-urlencoded' : 'application/json';
    const res = await fetch(`/api${path}`, { method, headers, credentials: 'same-origin', body: body ? (form ? new URLSearchParams(body) : JSON.stringify(body)) : undefined });
    if (!res.ok) {
        let data; try { data = await res.json(); } catch { /* empty response */ }
        throw new Error(data?.message || ({ 401: 'Usuário ou senha inválidos, ou sessão expirada.', 403: 'Acesso não autorizado. Atualize a página e tente novamente.', 409: 'Este registro foi alterado. Atualize a página.' }[res.status]) || 'Não foi possível concluir a operação.');
    }
    if (res.status === 204) return null;
    return res.json();
}
