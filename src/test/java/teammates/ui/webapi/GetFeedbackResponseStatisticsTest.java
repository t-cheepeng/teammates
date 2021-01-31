package teammates.ui.webapi;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Const;
import teammates.ui.output.CourseData;
import teammates.ui.output.MessageOutput;

/**
 * SUT: {@link GetFeedbackResponseStatistics}.
 */
public class GetFeedbackResponseStatisticsTest extends BaseActionTest<GetFeedbackResponseStatistics> {

    @Override
    protected String getActionUri() {
        return Const.ResourceURIs.RESPONSES_STATISTICS;
    }

    @Override
    protected String getRequestMethod() {
        return GET;
    }

    @Test
    @Override
    protected void testExecute() throws Exception {
        //See test cases below
    }

    @Test
    protected void testExecute_typicalUsage_shouldPass() {
//        InstructorAttributes instructor1OfCourse1 = typicalBundle.instructors.get("instructor1OfCourse1");
//        CourseAttributes expectedCourse = logic.getCourse(instructor1OfCourse1.getCourseId());
//
//        loginAsInstructor(instructor1OfCourse1.googleId);
//
//        ______TS("typical success case for instructor");
//
//        String[] params = {
//                Const.ParamsNames.COURSE_ID, instructor1OfCourse1.getCourseId(),
//                Const.ParamsNames.ENTITY_TYPE, Const.EntityType.INSTRUCTOR,
//        };
//        GetCourseAction getCourseAction = getAction(params);
//        JsonResult response = getJsonResult(getCourseAction);
//
//        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
//        CourseData courseData = (CourseData) response.getOutput();
//
//        assertEquals(expectedCourse.getId(), courseData.getCourseId());
//        assertEquals(expectedCourse.getName(), courseData.getCourseName());
//        assertEquals(expectedCourse.getTimeZone().getId(), courseData.getTimeZone());
//
//        StudentAttributes student1OfCourse1 = typicalBundle.students.get("student1InCourse1");
//        expectedCourse = logic.getCourse(student1OfCourse1.getCourse());
//        loginAsStudent(student1OfCourse1.googleId);
//
//        ______TS("typical success case for student");
//
//        params = new String[] {
//                Const.ParamsNames.COURSE_ID, student1OfCourse1.getCourse(),
//                Const.ParamsNames.ENTITY_TYPE, Const.EntityType.STUDENT,
//        };
//        getCourseAction = getAction(params);
//        response = getJsonResult(getCourseAction);
//
//        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
//        courseData = (CourseData) response.getOutput();
//
//        assertEquals(expectedCourse.getId(), courseData.getCourseId());
//        assertEquals(expectedCourse.getName(), courseData.getCourseName());
//        assertEquals(expectedCourse.getTimeZone().getId(), courseData.getTimeZone());
    }

    @Test
    protected void testExecute_notEnoughParameters_shouldFail() {
        loginAsAdmin();

        ______TS("Not enough parameters");

        verifyHttpParameterFailure();

        String[] submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "1000",
        };
        verifyHttpParameterFailure(submissionParams);
    }

    @Test
    protected void testExecute_invalidParameters_shouldFail() {
        loginAsAdmin();

        ______TS("Invalid parameter types");

        String[] submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "thousand",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "two thousand",
        };
        verifyHttpParameterFailure(submissionParams);

        submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "-300",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "-700",
        };
        verifyHttpParameterFailure(submissionParams);
    }

    @Test
    @Override
    protected void testAccessControl() throws Exception {
        //see test cases below
    }

    @Test
    protected void testAccessControl_testAdminAccess_shouldPass() {
        String[] submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "1000",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "2000",
        };

        verifyAccessibleForAdmin(submissionParams);
    }

    @Test
    protected void testAccessControl_testInstructorAccess_shouldFail() {
        String[] submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "1000",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "2000",
        };

        verifyInaccessibleForInstructors(submissionParams);
        verifyCannotAccess(submissionParams);
    }

    @Test
    protected void testAccessControl_testStudentAccess_shouldFail() {
        String[] submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "1000",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "2000",
        };

        verifyInaccessibleForStudents(submissionParams);
        verifyCannotAccess(submissionParams);
    }

}
