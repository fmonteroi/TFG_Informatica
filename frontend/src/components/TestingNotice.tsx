import { useState } from 'react'

function TestingNotice() {
    const [visible, setVisible] = useState(true)

    if (!visible) {
        return null
    }

    return (
        <div
            role="presentation"
            className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/75 p-4"
        >
            <section
                role="alertdialog"
                aria-modal="true"
                aria-labelledby="testing-notice-title"
                aria-describedby="testing-notice-description"
                className="relative w-full max-w-lg rounded-lg border border-amber-400/40 bg-slate-900 p-6 shadow-2xl"
            >
                <button
                    type="button"
                    aria-label="Cerrar aviso"
                    onClick={() => setVisible(false)}
                    className="absolute right-3 top-3 flex h-9 w-9 items-center justify-center rounded-md text-xl text-slate-400 transition hover:bg-slate-800 hover:text-white"
                >
                    ×
                </button>

                <h2 id="testing-notice-title" className="pr-10 text-lg font-bold text-amber-200">
                    Aplicación en fase de pruebas
                </h2>
                <p id="testing-notice-description" className="mt-3 text-sm text-slate-300">
                    EasyRift está pendiente de aprobación por parte de Riot Games. Si has
                    accedido accidentalmente a esta web, se ruega que salgas inmediatamente.
                </p>
            </section>
        </div>
    )
}

export default TestingNotice
