document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('aiSuggestBtn');
    if (!btn) return;

    btn.addEventListener('click', async () => {
        const issueDescription = document.getElementById('description').value;
        const specializationId = document.getElementById('specializationId').value;
        const minRating = document.getElementById('minRating').value;

        if (!issueDescription) {
            alert('Заполните описание поломки');
            return;
        }

        const resp = await fetch('/api/orders/ai-suggest', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({issueDescription, specializationId, minRating: minRating || null})
        });

        const data = await resp.json();
        document.getElementById('aiPrompt').textContent = data.prompt;
        document.getElementById('aiMasters').innerHTML = (data.recommendedMasters || [])
            .map(m => `<li>${m}</li>`)
            .join('');
        document.getElementById('aiBox').style.display = 'block';
    });
});
