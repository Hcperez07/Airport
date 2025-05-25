package airport.views;

import javax.swing.JTabbedPane;

/**
 * Implements RoleTabPolicy for the Administrator role.
 */
public class AdministratorTabPolicy implements RoleTabPolicy {

    // Aqui utilizamos el patrón "Policy" para definir cuales pestañas son visibles para el usuario segun si es admin o no
    // Índices de pestañas para Administrador:
    // Habilitadas: Registro de pasajeros (1), Registro de aviones (2), Registro de ubicaciones (3), Registro de vuelos (4),
    // Deshabilitadas: Actualizar información (5), Agregar a vuelo (6), Mostrar mis vuelos (7).
    private static final int[] ADMIN_ENABLED_TABS = {1, 2, 3, 4, 8, 9, 10, 11, 12};
    private static final int[] ADMIN_DISABLED_TABS = {5, 6, 7};

    @Override
    public void applyTabVisibility(JTabbedPane tabbedPane, AirportFrame viewFrame) {
        // Habilita las pestañas permitidas para el administrador
        for (int index : ADMIN_ENABLED_TABS) {
            if (index < tabbedPane.getTabCount()) {
                tabbedPane.setEnabledAt(index, true);
            }
        }
        // Deshabilita las pestañas que no debe ver el administrador
        for (int index : ADMIN_DISABLED_TABS) {
            if (index < tabbedPane.getTabCount()) {
                tabbedPane.setEnabledAt(index, false);
            }
        }
    }
}
