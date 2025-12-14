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

    // Make MANY clouds; start from the left immediately, move right fast, infinite loop
    // makeClouds: spawn clouds off-screen on the left; slow, continuous rightward drift
    function makeClouds(container, amount = 120) {
        const nearShare = 0.6;
        const nearCount = Math.round(amount * nearShare);
        const farCount = amount - nearCount;
    
        function spawnCloud(layerClass) {
            const c = document.createElement('div');
            c.className = 'cloud ' + layerClass;
    
            // Always spawn BEFORE the left edge so formation is never visible
            const top = Math.random() * (70 - 6) + 6;           // 6vh .. 70vh
            const left = Math.random() * (-10 - (-40)) + (-40); // -40vw .. -10vw (off-screen left)
    
            // Simple pill sizes: near = bigger, far = smaller
            const baseWidth = layerClass === 'cloud--near'
                ? Math.random() * (360 - 240) + 240             // 240px .. 360px
                : Math.random() * (240 - 160) + 160;            // 160px .. 240px
            const baseHeight = Math.max(58, baseWidth * 0.30);
    
            c.style.top = top + 'vh';
            c.style.left = left + 'vw';
            c.style.width = baseWidth + 'px';
            c.style.height = baseHeight + 'px';
    
            // Natural, slower drift; NO negative delay (prevents mid-path spawning around center)
            const dur  = layerClass === 'cloud--near' ? Math.random() * (34 - 24) + 24  // 24s .. 34s
                                                       : Math.random() * (46 - 32) + 32; // 32s .. 46s
            const delay = '0s';
    
            c.style.setProperty('--dur', dur + 's');
            c.style.setProperty('--delay', delay);
    
            container.appendChild(c);
        }
    
        for (let i = 0; i < nearCount; i++) spawnCloud('cloud--near');
        for (let i = 0; i < farCount; i++)  spawnCloud('cloud--far');
    }

    // Make LOTS of visible snowflakes with layered depth and varied timings
    function makeSnow(container, amount = 260) {
        for (let i = 0; i < amount; i++) {
            const f = document.createElement('div');
            const isNear = Math.random() < 0.6;
            f.className = 'flake ' + (isNear ? 'flake--near' : 'flake--far');

            const left = rand(0, 100);
            const fallDur = isNear ? rand(10, 16) : rand(16, 26);
            const driftDur = rand(8, 14);
            const delay = -rand(0, fallDur);

            f.style.left = left + 'vw';
            f.style.setProperty('--fallDur', fallDur + 's');
            f.style.setProperty('--driftDur', driftDur + 's');
            f.style.setProperty('--delay', delay + 's');

            container.appendChild(f);
        }
    }

    function makeThunder(container) {
        const flash = document.createElement('div');
        flash.className = 'flash';
        container.appendChild(flash);
        makeRain(container, 60);
    }

    function makeSun(container) {
        const s = document.createElement('div');
        s.className = 'sun';
        container.appendChild(s);
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
        ['Clear','Rain','Snow','Clouds','Thunder'].forEach(c => body.classList.remove(c));
        body.classList.add(cls);

        let container = document.getElementById('weather-effects');
        if (!container) {
            container = document.createElement('div');
            container.id = 'weather-effects';
            document.body.appendChild(container);
        }
        clearEffects(container);

        if (cls === 'Rain') {
            makeRain(container, 100);
        } else if (cls === 'Snow') {
            makeSnow(container, 260);  // lots of flakes
        } else if (cls === 'Clouds') {
            makeClouds(container, 140); // dense field, full-height coverage
        } else if (cls === 'Thunder') {
            makeThunder(container);
        } else { // Clear
            makeSun(container);
        }
    };

    document.addEventListener('DOMContentLoaded', function () {
        if (window.weatherCondition) {
            setWeatherBackground(window.weatherCondition);
        }
    });

    // Spawn one cloud; seed=true scatters it across width/height initially (already mid-drift)
    function spawnCloud(container, layerClass, seed = false) {
        const c = document.createElement('div');
        c.className = 'cloud ' + layerClass;
    
        // Full vertical coverage
        const top = Math.random() * (99 - 1) + 1; // 1vh .. 99vh
    
        // Horizontal placement:
        // - seed: anywhere from off-screen left to far right (already mid-path)
        // - subsequent: off-screen left only for natural entry
        const left = seed
            ? Math.random() * (120 - (-60)) + (-60) // -60vw .. 120vw
            : Math.random() * (-12 - (-60)) + (-60); // -60vw .. -12vw
    
        // Sizes per layer
        const baseWidth = layerClass === 'cloud--near'
            ? Math.random() * (360 - 240) + 240
            : Math.random() * (240 - 160) + 160;
        const baseHeight = Math.max(56, baseWidth * 0.30);
    
        c.style.top = top + 'vh';
        c.style.left = left + 'vw';
        c.style.width = baseWidth + 'px';
        c.style.height = baseHeight + 'px';
    
        // Drift speed; near slightly faster
        const dur  = layerClass === 'cloud--near'
            ? Math.random() * (36 - 24) + 24   // 24s .. 36s
            : Math.random() * (50 - 34) + 34;  // 34s .. 50s
    
        // Seeded clouds start mid-animation so the screen is already filled
        const delay = seed ? -Math.random() * dur : 0;
    
        c.style.setProperty('--dur', dur + 's');
        c.style.setProperty('--delay', delay + 's');
    
        // Fluffy silhouette: core + two lobes (looks like clouds)
        const shape = document.createElement('div');
        shape.className = 'cloud-shape';
        const core  = document.createElement('span');
        core.className = 'core';
        const lobeL = document.createElement('span');
        lobeL.className = 'lobe-left';
        const lobeR = document.createElement('span');
        lobeR.className = 'lobe-right';
        shape.appendChild(core);
        shape.appendChild(lobeL);
        shape.appendChild(lobeR);
        c.appendChild(shape);
    
        container.appendChild(c);
    
        // Compute remaining time until drift finishes (for seeded clouds with negative delay)
        const remaining = dur - Math.min(Math.abs(delay), dur);
    
        // When finished, remove and respawn off-screen left, staggered
        setTimeout(() => {
            if (c.parentNode === container) {
                container.removeChild(c);
                setTimeout(() => spawnCloud(container, layerClass, false), Math.random() * 250);
            }
        }, remaining * 1000);
    }

    // Initial full-screen field, then per-cloud continuous respawn — avoids bunching
    function makeClouds(container, amount = 140) {
        const nearShare = 0.6;
        const targetNear = Math.round(amount * nearShare);
        const targetFar  = amount - targetNear;
    
        // Seed: scatter across width/height with negative delays so screen starts full
        for (let i = 0; i < targetNear; i++) spawnCloud(container, 'cloud--near', true);
        for (let i = 0; i < targetFar; i++)  spawnCloud(container, 'cloud--far',  true);
    }
})();