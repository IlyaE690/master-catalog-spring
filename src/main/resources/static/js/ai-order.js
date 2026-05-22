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
            const boxElement = document.getElementById('aiBox');

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
                                <button type="button" class="btn btn-sm btn-success" onclick="selectMaster(${master.id}, ${data.specializationId}, '${escapeHtml(master.fullName)}')">
                                    Выбрать
                                </button>
                            </td>
                        `;
                        mastersList.appendChild(row);
                    });
                } else {
                    mastersList.innerHTML = '<tr><td colspan="5" class="text-center">Мастера не найдены</td></tr>';
                }
            }

            if (boxElement) boxElement.style.display = 'block';
            if (mastersContainer) mastersContainer.style.display = 'block';

        } catch (e) {
            alert(e.message || 'Ошибка при AJAX-запросе');
        }
    });

    window.selectMaster = function(masterId, specializationId, masterName) {
        const title = document.getElementById('title');
        const description = document.getElementById('description');
        const address = document.getElementById('address');
        const scheduledDate = document.getElementById('scheduledDate');

        if (!title.value) {
            alert('Заполните заголовок заказа');
            return;
        }
        if (!description.value) {
            alert('Заполните описание заказа');
            return;
        }
        if (!address.value) {
            alert('Заполните адрес');
            return;
        }
        if (!scheduledDate.value) {
            alert('Выберите дату');
            return;
        }

        if (confirm(`Отправить заказ мастеру ${masterName}?`)) {
            const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

            const formData = new FormData();
            formData.append('specializationId', specializationId);
            formData.append('masterId', masterId);
            formData.append('title', title.value);
            formData.append('description', description.value);
            formData.append('address', address.value);
            formData.append('scheduledDate', scheduledDate.value);

            const photoInput = document.getElementById('orderPhoto');
            if (photoInput && photoInput.files[0]) {
                formData.append('orderPhoto', photoInput.files[0]);
            }

            const headers = {};
            if (csrfToken && csrfHeader) {
                headers[csrfHeader] = csrfToken;
            }

            fetch('/orders/create-with-master', {
                method: 'POST',
                headers: headers,
                body: formData
            }).then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    return response.json().then(data => {
                        alert(data.error || 'Ошибка при создании заказа');
                    });
                }
            }).catch(err => {
                alert('Ошибка: ' + err.message);
            });
        }
    };

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
