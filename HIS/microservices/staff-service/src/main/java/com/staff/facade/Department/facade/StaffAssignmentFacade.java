package com.staff.facade.Department.facade;



public interface StaffAssignmentFacade {

    void assignDoctorToDepartment(String staffId, String departmentId);
    void assignNurseToUnit(String staffId, String unitId);
}
