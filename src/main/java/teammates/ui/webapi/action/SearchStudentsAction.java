package teammates.ui.webapi.action;

import java.util.ArrayList;
import java.util.List;

import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.UnauthorizedAccessException;
import teammates.common.util.Const;
import teammates.common.util.StringHelper;
import teammates.ui.webapi.output.StudentData;
import teammates.ui.webapi.output.StudentsData;

/**
 * Action for searching for students.
 */
public class SearchStudentsAction extends Action {

    @Override
    protected AuthType getMinAuthLevel() {
        return AuthType.LOGGED_IN;
    }

    @Override
    public void checkSpecificAccessControl() {
        // Only instructors and admins can search for student
        if (!userInfo.isInstructor && !userInfo.isAdmin) {
            throw new UnauthorizedAccessException("Instructor or Admin privilege is required to access this resource.");
        }
    }

    private String getInstituteFromCourseId(String courseId) {
        String instructorForCourseGoogleId = findAvailableInstructorGoogleIdForCourse(courseId);
        if (instructorForCourseGoogleId == null) {
            return null;
        }

        AccountAttributes account = logic.getAccount(instructorForCourseGoogleId);
        if (account == null) {
            return null;
        }

        return StringHelper.isEmpty(account.institute) ? "None" : account.institute;
    }

    /**
     * Finds the googleId of a registered instructor with co-owner privileges.
     * If there is no such instructor, finds the googleId of a registered
     * instructor with the privilege to modify instructors.
     *
     * @param courseId
     *            the ID of the course
     * @return the googleId of a suitable instructor if found, otherwise an
     *         empty string
     */
    private String findAvailableInstructorGoogleIdForCourse(String courseId) {
        List<InstructorAttributes> instructorList = logic.getInstructorsForCourse(courseId);

        for (InstructorAttributes instructor : instructorList) {
            if (instructor.isRegistered()
                    && (instructor.hasCoownerPrivileges()
                    || instructor.isAllowedForPrivilege(Const.ParamsNames.INSTRUCTOR_PERMISSION_MODIFY_INSTRUCTOR))) {
                return instructor.googleId;
            }

        }

        return "";
    }

    @Override
    public ActionResult execute() {
        String searchKey = getNonNullRequestParamValue(Const.ParamsNames.SEARCH_KEY);
        List<StudentAttributes> students;
        List<StudentData> studentDataList = new ArrayList<>();

        // Search for students
        if (userInfo.isAdmin) {
            students = logic.searchStudentsInWholeSystem(searchKey).studentList;
        } else {
            List<InstructorAttributes> instructors = logic.getInstructorsForGoogleId(userInfo.id);
            students = logic.searchStudents(searchKey, instructors).studentList;
        }

        for (StudentAttributes s : students) {
            StudentData studentData = new StudentData(s);

            if (userInfo.isAdmin) {
                studentData.addAdditionalInformationForAdminSearch(
                        StringHelper.encrypt(s.getKey()),
                        getInstituteFromCourseId(s.getCourse()));
            } else {
                studentData.hideInformationForInstructor();
            }
            studentData.hideLastName();

            studentDataList.add(studentData);
        }
        StudentsData studentsData = new StudentsData();
        studentsData.setStudents(studentDataList);

        return new JsonResult(studentsData);
    }
}
