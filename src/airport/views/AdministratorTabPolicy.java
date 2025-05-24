package airport.views;

import javax.swing.JTabbedPane;

/**
 * Implements RoleTabPolicy for the Administrator role.
 */
public class AdministratorTabPolicy implements RoleTabPolicy {

    // Tab indices for Administrator:
    // Enabled: Passenger reg (1), Airplane reg (2), Location reg (3), Flight reg (4),
    // Show all passengers (8), Show all flights (9), Show all planes (10), Show all locations (11), Delay flight (12).
    // Disabled: Update info (5), Add to flight (6), Show my flights (7).
    private static final int[] ADMIN_ENABLED_TABS = {1, 2, 3, 4, 8, 9, 10, 11, 12};
    private static final int[] ADMIN_DISABLED_TABS = {5, 6, 7};

    @Override
    public void applyTabVisibility(JTabbedPane tabbedPane, AirportFrame viewFrame) {
        for (int index : ADMIN_ENABLED_TABS) {
            if (index < tabbedPane.getTabCount()) {
                tabbedPane.setEnabledAt(index, true);
            }
        }
        for (int index : ADMIN_DISABLED_TABS) {
            if (index < tabbedPane.getTabCount()) {
                tabbedPane.setEnabledAt(index, false);
            }
        }
    }
}