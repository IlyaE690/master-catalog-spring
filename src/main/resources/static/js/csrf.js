(function() {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    if (csrfToken && csrfHeader) {
        window.csrfToken = csrfToken;
        window.csrfHeader = csrfHeader;

        const originalFetch = window.fetch;
        window.fetch = function(url, options = {}) {
            if (options.method && options.method !== 'GET' && options.method !== 'HEAD') {
                options.headers = options.headers || {};
                options.headers[csrfHeader] = csrfToken;
            }
            return originalFetch.call(this, url, options);
        };

        const originalOpen = XMLHttpRequest.prototype.open;
        XMLHttpRequest.prototype.open = function(method, url, async, user, password) {
            this._method = method;
            return originalOpen.call(this, method, url, async, user, password);
        };

        const originalSend = XMLHttpRequest.prototype.send;
        XMLHttpRequest.prototype.send = function(body) {
            if (this._method && this._method !== 'GET' && this._method !== 'HEAD') {
                this.setRequestHeader(csrfHeader, csrfToken);
            }
            return originalSend.call(this, body);
        };
    }
})();
