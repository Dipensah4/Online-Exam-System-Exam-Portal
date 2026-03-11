/**
 * timer.js — Exam Countdown Timer
 *
 * Usage: Include this script in take-exam.html and call:
 *   ExamTimer.init(durationMinutes, 'timer', 'examForm');
 *
 * Features:
 * - Countdown from given duration in minutes
 * - Warning animation when ≤ 5 minutes remain
 * - Auto-submits the exam form when time expires
 * - Persists remaining time in sessionStorage (survives page refresh)
 */
const ExamTimer = (() => {

    let intervalId = null;

    function init(durationMinutes, timerElementId, formId) {
        const timerEl = document.getElementById(timerElementId);
        const form    = document.getElementById(formId);
        if (!timerEl || !form) return;

        const storageKey = 'exam_timer_' + formId;

        // Restore remaining seconds from sessionStorage (survives refresh)
        let seconds;
        const stored = sessionStorage.getItem(storageKey);
        if (stored !== null && !isNaN(parseInt(stored))) {
            seconds = parseInt(stored);
        } else {
            seconds = durationMinutes * 60;
        }

        function tick() {
            seconds--;
            sessionStorage.setItem(storageKey, seconds);

            const m = Math.floor(seconds / 60).toString().padStart(2, '0');
            const s = (seconds % 60).toString().padStart(2, '0');
            timerEl.textContent = '⏱ ' + m + ':' + s;

            // Warning at 5 minutes
            if (seconds <= 300) {
                timerEl.classList.add('warn');
            }

            // Auto-submit at 0
            if (seconds <= 0) {
                clearInterval(intervalId);
                sessionStorage.removeItem(storageKey);
                form.submit();
            }
        }

        // Initial display
        const initM = Math.floor(seconds / 60).toString().padStart(2, '0');
        const initS = (seconds % 60).toString().padStart(2, '0');
        timerEl.textContent = '⏱ ' + initM + ':' + initS;

        intervalId = setInterval(tick, 1000);

        // Cleanup on form submit
        form.addEventListener('submit', () => {
            clearInterval(intervalId);
            sessionStorage.removeItem(storageKey);
        });
    }

    return { init };
})();
