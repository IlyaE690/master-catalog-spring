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

        const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
        const headers = {'Content-Type': 'application/json', 'Accept': 'application/json'};
        if (csrfToken && csrfHeader) {
            headers[csrfHeader] = csrfToken;
        }

        try {
            const resp = await fetch('/api/orders/ai-suggest', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify({
                    issueDescription,
                    specializationId: specializationId || null,
                    minRating: minRating || null
                })
            });

            const data = await resp.json();
            if (!resp.ok) {
                throw new Error(data.error || 'Не удалось получить рекомендации');
            }

            const mastersContainer = document.getElementById('aiMastersContainer');
            const mastersList = document.getElementById('aiMastersList');

            if (mastersList) {
                mastersList.innerHTML = '';

                if (data.recommendedMasters && data.recommendedMasters.length > 0) {
                    data.recommendedMasters.forEach(master => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${escapeHtml(master.fullName)}</td>
                            <td>${master.rating} ★</td>
                            <td>${escapeHtml(master.specializations.join(', '))}</td>
                            <td>${master.completedOrdersCount} заказов</td>
                            <td>
                                <form method="get" action="/masters/${master.id}" style="display: inline;">
                                    <input type="hidden" name="${csrfHeader}" value="${csrfToken}"/>
                                    <button type="submit" class="btn btn-sm btn-success">
                                        Выбрать
                                    </button>
                                </form>
                            </td>
                        `;
                        mastersList.appendChild(row);
                    });
                } else {
                    mastersList.innerHTML = '<tr><td colspan="5" class="text-center">Мастера не найдены</td></tr>';
                }
            }

            if (mastersContainer) mastersContainer.style.display = 'block';

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