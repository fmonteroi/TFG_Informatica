type JsonViewerProps = {
    data: unknown
}

function JsonViewer({ data }: JsonViewerProps) {
    return (
        <pre className="max-h-[700px] overflow-auto rounded-lg border border-slate-200 bg-slate-50 p-4 text-sm leading-6 text-slate-800">
      {JSON.stringify(data, null, 2)}
    </pre>
    )
}

export default JsonViewer