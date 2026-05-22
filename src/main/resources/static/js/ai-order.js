document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('aiSuggestBtn');
    if (!btn) return;

    btn.addEventListener('click', async () => {
        const issueDescription = document.getElementById('description').value;
        const specializationId = document.getElementById('specializationId')?.value;
        const minRating = document.getElementById('minRating')?.value;

        if (!issueDescription) {
            alert('Заполните описание поломки');
            return;
        }

        try {
            const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
            const headers = {'Content-Type': 'application/json', 'Accept': 'application/json'};
            if (csrfToken && csrfHeader) {
                headers[csrfHeader] = csrfToken;
            }

            const resp = await fetch('/api/orders/ai-suggest', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify({issueDescription, specializationId: specializationId || null, minRating: minRating || null})
            });

            const data = await resp.json();
            if (!resp.ok) {
                throw new Error(data.error || 'Не удалось получить рекомендации');
            }

            const promptElement = document.getElementById('aiPrompt');
            const mastersElement = document.getElementById('aiMasters');
            const boxElement = document.getElementById('aiBox');

            if (promptElement) promptElement.textContent = data.prompt;
            if (mastersElement) mastersElement.innerHTML = (data.recommendedMasters || [])
                .map(m => `<li>${escapeHtml(m)}</li>`)
                .join('');
            if (boxElement) boxElement.style.display = 'block';
        } catch (e) {
            alert(e.message || 'Ошибка при AJAX-запросе');
        }
    });

    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/[&<>]/g, function(m) {
            if (m === '&') return '&amp;';
            if (m === '<') return '&lt;';
            if (m === '>') return '&gt;';
            return m;
        });
    }
});
