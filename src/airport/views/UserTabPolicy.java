package airport.views;

import javax.swing.JTabbedPane;

/**
 * Implements RoleTabPolicy for the User role.
 */
public class UserTabPolicy implements RoleTabPolicy {

    // Indices de pestañas del usuario
    // Deshabilitadas: 1, 2, 3, 4, 8, 10, 12
    private static final int[] USER_ENABLED_TABS = {5, 6, 7, 9, 11};

    @Override
    public void applyTabVisibility(JTabbedPane tabbedPane, AirportFrame viewFrame) {
        // Deshabilitar primero todas las pestañas operativas 
        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, false);
        }
        // Habilitar las pestañas específicas del rol Usuario
        for (int index : USER_ENABLED_TABS) {
            if (index < tabbedPane.getTabCount()) {
                tabbedPane.setEnabledAt(index, true);
            }
        }
    }
}

