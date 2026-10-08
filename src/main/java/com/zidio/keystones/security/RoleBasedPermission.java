package com.zidio.keystones.security;

import com.zidio.keystones.enums.Permission;
import com.zidio.keystones.enums.Role;

import java.util.*;

public class RoleBasedPermission {
    public static Map<Role, Set<Permission>> getRoleBasedPermission(){
        Map<Role, Set<Permission>> permissions = new HashMap<>();

        permissions.put(Role.ADMIN, new HashSet<>(
                Arrays.asList(
                        Permission.LOGIN,
                        Permission.LOGOUT,
                        Permission.CREATE_CUSTOMER,
                        Permission.VIEW_CUSTOMER,
                        Permission.UPDATE_CUSTOMER,
                        Permission.DELETE_CUSTOMER,
                        Permission.CREATE_USER,
                        Permission.VIEW_USER,
                        Permission.UPDATE_USER,
                        Permission.DELETE_USER,
                        Permission.CREATE_SITE,
                        Permission.VIEW_SITE,
                        Permission.UPDATE_SITE,
                        Permission.DELETE_SITE,
                        Permission.CREATE_WO,
                        Permission.VIEW_WO,
                        Permission.UPDATE_WO,
                        Permission.DELETE_WO,
                        Permission.CANCEL_WO,
                        Permission.ASSIGN_WO,
                        Permission.CANCEL_WO,
                        Permission.CLOSE_WO,
                        Permission.START_WORK,
                        Permission.HOLD_WORK,
                        Permission.RESUME_WORK,
                        Permission.COMPLETE_WORK,
                        Permission.ADD_PARTS,
                        Permission.UPDATE_PARTS,
                        Permission.VIEW_PARTS,
                        Permission.USE_PARTS,
                        Permission.DELETE_PARTS,
                        Permission.ADD_TIME_LOGS,
                        Permission.VIEW_TIME_LOGS,
                        Permission.VIEW_DASHBOARD,
                        Permission.VIEW_PARTS,
                        Permission.VIEW_REPORT,
                        Permission.SEND_NOTIFICATION
                )
        ));

        permissions.put(Role.MANAGER, new HashSet<>(
                Arrays.asList(
                        Permission.LOGIN,
                        Permission.LOGOUT,
                        Permission.CREATE_USER,
                        Permission.UPDATE_USER,
                        Permission.VIEW_USER,
                        Permission.CREATE_CUSTOMER,
                        Permission.UPDATE_CUSTOMER,
                        Permission.VIEW_CUSTOMER,
                        Permission.CREATE_SITE,
                        Permission.UPDATE_SITE,
                        Permission.VIEW_SITE,
                        Permission.CANCEL_WO,
                        Permission.UPDATE_WO,
                        Permission.ASSIGN_WO,
                        Permission.VIEW_WO,
                        Permission.CREATE_WO,
                        Permission.CLOSE_WO,
                        Permission.VIEW_DASHBOARD,
                        Permission.VIEW_REPORT,
                        Permission.SEND_NOTIFICATION,
                        Permission.VIEW_PARTS,
                        Permission.VIEW_TIME_LOGS
                )
        ));

        permissions.put(Role.DISPATCHER, new HashSet<>(
                Arrays.asList(
                        Permission.LOGOUT,
                        Permission.LOGIN,
                        Permission.VIEW_SITE,
                        Permission.VIEW_CUSTOMER,
                        Permission.CREATE_WO,
                        Permission.VIEW_WO,
                        Permission.UPDATE_WO,
                        Permission.ASSIGN_WO,
                        Permission.VIEW_DASHBOARD
                )
        ));

        permissions.put(Role.TECHNICIAN, new HashSet<>(
                Arrays.asList(
                        Permission.LOGIN,
                        Permission.LOGOUT,
                        Permission.VIEW_WO,
                        Permission.VIEW_SITE,
                        Permission.VIEW_PARTS,
                        Permission.COMPLETE_WORK,
                        Permission.START_WORK,
                        Permission.RESUME_WORK,
                        Permission.HOLD_WORK,
                        Permission.USE_PARTS,
                        Permission.VIEW_TIME_LOGS,
                        Permission.ADD_TIME_LOGS
                )
        ));

        permissions.put(Role.CUSTOMER, new HashSet<>(
                Arrays.asList(
                        Permission.LOGIN,
                        Permission.LOGOUT,
                        Permission.REQUEST_RAISE,
                        Permission.VIEW_OWN_REQUEST
                )
        ));

        return permissions;
    }
}
