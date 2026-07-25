function TestingNotice() {
    return (
        <aside
            role="alert"
            className="border border-amber-400/40 bg-amber-400/10 px-4 py-3 text-sm text-amber-100"
        >
            <p className="font-semibold">Aplicación en fase de pruebas</p>
            <p className="mt-1 text-amber-100/80">
                EasyRift está pendiente de aprobación por parte de Riot Games. Si has accedido
                accidentalmente a esta web, se ruega que salgas inmediatamente.
            </p>
        </aside>
    )
}

export default TestingNotice
