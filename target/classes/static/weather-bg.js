// ...new file...
(function () {
    function rand(min, max) { return Math.random() * (max - min) + min; }
    function clearEffects(container) {
        while (container.firstChild) container.removeChild(container.firstChild);
    }

    function makeRain(container, amount = 80) {
        for (let i = 0; i < amount; i++) {
            const d = document.createElement('div');
            d.className = 'drop';
            const left = rand(0, 100);
            const dur = rand(0.6, 1.6);
            const delay = rand(0, 5);
            const height = rand(50, 140);
            d.style.left = left + 'vw';
            d.style.width = (rand(1, 2.5)) + 'px';
            d.style.height = height + 'px';
            d.style.animationDuration = dur + 's';
            d.style.animationDelay = delay + 's';
            container.appendChild(d);
        }
    }

    function makeSnow(container, amount = 40) {
        for (let i = 0; i < amount; i++) {
            const f = document.createElement('div');
            f.className = 'flake';
            const left = rand(0, 100);
            const dur = rand(6, 18);
            const delay = rand(0, 10);
            const size = rand(4, 12);
            f.style.left = left + 'vw';
            f.style.width = size + 'px';
            f.style.height = size + 'px';
            f.style.borderRadius = (size/2) + 'px';
            f.style.animationDuration = dur + 's, ' + rand(6,14) + 's';
            f.style.animationDelay = delay + 's, ' + delay + 's';
            container.appendChild(f);
        }
    }

    function makeClouds(container, amount = 3) {
        for (let i = 0; i < amount; i++) {
            const c = document.createElement('div');
            c.className = 'cloud';
            const top = rand(5, 45);
            const width = rand(140, 320);
            const height = Math.max(40, width * 0.28);
            const dur = rand(30, 120);
            c.style.top = top + 'vh';
            c.style.width = width + 'px';
            c.style.height = height + 'px';
            c.style.left = (-rand(10,40)) + 'vw';
            c.style.animationDuration = dur + 's';
            container.appendChild(c);
        }
    }

    function makeSun(container) {
        const s = document.createElement('div');
        s.className = 'sun';
        container.appendChild(s);
    }

    function makeThunder(container) {
        const flash = document.createElement('div');
        flash.className = 'flash';
        container.appendChild(flash);
        // also add rain
        makeRain(container, 60);
    }

    // normalize condition to classes we use
    function mapCondition(cond) {
        if (!cond) return 'Clear';
        cond = cond.toLowerCase();
        if (cond.includes('rain') || cond.includes('drizzle')) return 'Rain';
        if (cond.includes('snow')) return 'Snow';
        if (cond.includes('cloud')) return 'Clouds';
        if (cond.includes('thunder') || cond.includes('storm')) return 'Thunder';
        if (cond.includes('mist') || cond.includes('fog') || cond.includes('haze')) return 'Clouds';
        return 'Clear';
    }

    window.setWeatherBackground = function (rawCondition) {
        const cls = mapCondition(rawCondition);
        const body = document.body;
        // remove previous weather classes (known set)
        ['Clear','Rain','Snow','Clouds','Thunder'].forEach(c => body.classList.remove(c));
        body.classList.add(cls);

        let container = document.getElementById('weather-effects');
        if (!container) {
            container = document.createElement('div');
            container.id = 'weather-effects';
            document.body.appendChild(container);
        }
        clearEffects(container);

        // create effect nodes
        if (cls === 'Rain') {
            makeRain(container, 100);
        } else if (cls === 'Snow') {
            makeSnow(container, 40);
        } else if (cls === 'Clouds') {
            makeClouds(container, 4);
        } else if (cls === 'Thunder') {
            makeThunder(container);
        } else { // Clear
            makeSun(container);
        }
    };

    // Auto-init if window.weatherCondition (server may set this)
    document.addEventListener('DOMContentLoaded', function () {
        if (window.weatherCondition) {
            setWeatherBackground(window.weatherCondition);
        }
    });
})();