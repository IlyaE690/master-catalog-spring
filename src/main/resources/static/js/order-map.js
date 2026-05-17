document.addEventListener('DOMContentLoaded', () => {
    const mapContainer = document.getElementById('order-map');
    const addressInput = document.getElementById('address');
    if (!mapContainer || !addressInput || typeof ymaps === 'undefined') return;

    ymaps.ready(() => {
        const map = new ymaps.Map('order-map', {center: [55.7887, 49.1221], zoom: 11});
        let placemark = null;

        const updateMap = () => {
            const rawAddress = (addressInput.value || '').trim();
            if (!rawAddress) return;
            const safeAddress = rawAddress.replace(/[<>]/g, '');
            ymaps.geocode(safeAddress).then((res) => {
                const first = res.geoObjects.get(0);
                if (!first) return;
                const coords = first.geometry.getCoordinates();
                map.setCenter(coords, 15);
                if (placemark) map.geoObjects.remove(placemark);
                placemark = new ymaps.Placemark(coords);
                map.geoObjects.add(placemark);
            });
        };

        addressInput.addEventListener('change', updateMap);
    });
});
