package airport.views;

import javax.swing.JTabbedPane;

/**
 * Implements RoleTabPolicy for the User role.
 */
public class UserTabPolicy implements RoleTabPolicy {

    // Tab indices for User:
    // Enabled: Update info (5), Add to flight (6), Show my flights (7), 
    // Show all flights (9), Show all locations (11).
    // Disabled: All others (1, 2, 3, 4, 8, 10, 12).
    private static final int[] USER_ENABLED_TABS = {5, 6, 7, 9, 11};
    // All other operational tabs (indices 1-4, 8, 10, 12) should be disabled.
    // Tab 0 is Administration.

    @Override
    public void applyTabVisibility(JTabbedPane tabbedPane, AirportFrame viewFrame) {
        // Disable all operational tabs first (index 0 is Administration, always accessible based on current logic)
        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, false);
        }
        // Enable tabs specific to the User role
        for (int index : USER_ENABLED_TABS) {
            if (index < tabbedPane.getTabCount()) {
                tabbedPane.setEnabledAt(index, true);
            }
        }
    }
}
