/**
 * dashboard.js — AJAX Dashboard Stats Polling
 *
 * Polls the server every 10 seconds for updated dashboard statistics.
 * Works for both admin (/api/admin/stats) and student (/api/student/stats).
 *
 * Usage: Include this script and call:
 *   DashboardPoller.init('/api/admin/stats', { totalExams: 'stat-exams', ... });
 */
const DashboardPoller = (() => {

    let intervalId = null;

    /**
     * @param {string} url - The API endpoint to poll
     * @param {Object} fieldMap - Maps JSON keys to element IDs
     *   e.g. { totalExams: 'stat-exams', totalStudents: 'stat-students' }
     * @param {number} intervalMs - Polling interval in milliseconds (default 10000)
     */
    function init(url, fieldMap, intervalMs = 10000) {
        async function fetchStats() {
            try {
                const res = await fetch(url);
                if (!res.ok) return;
                const data = await res.json();

                for (const [key, elementId] of Object.entries(fieldMap)) {
                    const el = document.getElementById(elementId);
                    if (el && data[key] !== undefined) {
                        const val = data[key];
                        // Format percentage values
                        if (typeof val === 'number' && key.toLowerCase().includes('score')) {
                            el.textContent = val > 0 ? val + '%' : '—';
                        } else {
                            el.textContent = val;
                        }
                    }
                }
            } catch (e) {
                // Silently ignore network errors
            }
        }

        // Initial fetch
        fetchStats();

        // Poll
        intervalId = setInterval(fetchStats, intervalMs);
    }

    function stop() {
        if (intervalId) clearInterval(intervalId);
    }

    return { init, stop };
})();
