document.addEventListener('DOMContentLoaded', function() {
    var roleSelect = document.getElementById('role');
    var specializationsBlock = document.getElementById('specializationsBlock');
    var specializationsError = document.getElementById('specializationsError');
    var registerForm = document.getElementById('registerForm');

    function toggleSpecializations() {
        if (roleSelect && specializationsBlock) {
            if (roleSelect.value === 'MASTER') {
                specializationsBlock.style.display = 'block';
            } else {
                specializationsBlock.style.display = 'none';
            }
        }
    }

    if (roleSelect) {
        roleSelect.addEventListener('change', toggleSpecializations);
        toggleSpecializations();
    }

    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            if (roleSelect && roleSelect.value === 'MASTER') {
                var checkedBoxes = document.querySelectorAll('input[name="specializationIds"]:checked');
                if (checkedBoxes.length === 0) {
                    e.preventDefault();
                    if (specializationsError) {
                        specializationsError.style.display = 'block';
                    }
                }
            }
        });
    }
});