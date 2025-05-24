package airport.views;

import javax.swing.JTabbedPane;

/**
 * Defines the strategy for applying tab visibility rules based on user roles.
 */
public interface RoleTabPolicy {
    /**
     * Applies tab visibility rules to the given JTabbedPane.
     * @param tabbedPane The JTabbedPane whose tabs' visibility will be managed.
     * @param viewFrame  The AirportFrame instance, if needed by the policy (e.g., to access other components or methods).
     */
    void applyTabVisibility(JTabbedPane tabbedPane, AirportFrame viewFrame);
}
