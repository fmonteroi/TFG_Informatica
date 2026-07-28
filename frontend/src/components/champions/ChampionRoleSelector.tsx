import { formatRole, ROLE_ORDER } from '../../lib/lol'
import type { Role } from '../../types/api'

type ChampionRoleSelectorProps = {
    availableRoles: Role[]
    selectedRole: Role | null
    onSelectRole: (role: Role) => void
}

function ChampionRoleSelector({
    availableRoles,
    selectedRole,
    onSelectRole,
}: ChampionRoleSelectorProps) {
    return (
        <div
            className="mb-4 grid grid-cols-5 gap-2"
            role="group"
            aria-label="Seleccionar rol"
        >
            {ROLE_ORDER.map((role) => {
                const isAvailable = availableRoles.includes(role)
                const isSelected = selectedRole === role

                let buttonClass =
                    'flex min-h-20 flex-col items-center justify-center gap-2 rounded-md border px-2 py-3 transition'

                if (!isAvailable) {
                    buttonClass +=
                        ' cursor-not-allowed border-slate-800 bg-slate-950/65 opacity-35'
                } else if (isSelected) {
                    buttonClass +=
                        ' border-cyan-300 bg-cyan-400/15 text-cyan-200'
                } else {
                    buttonClass +=
                        ' border-slate-700 bg-slate-900 text-slate-300 hover:border-cyan-400/60 hover:text-cyan-200'
                }

                return (
                    <button
                        key={role}
                        type="button"
                        disabled={!isAvailable}
                        aria-pressed={isSelected}
                        title={formatRole(role)}
                        className={buttonClass}
                        onClick={() => onSelectRole(role)}
                    >
                        <img
                            src={`/role-icons/${role}.svg`}
                            alt=""
                            aria-hidden="true"
                            className="h-8 w-8 object-contain"
                        />
                        <span className="text-xs font-semibold uppercase">
                            {formatRole(role)}
                        </span>
                    </button>
                )
            })}
        </div>
    )
}

export default ChampionRoleSelector
