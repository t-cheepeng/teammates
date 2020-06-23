package teammates.e2e.pageobjects;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import teammates.common.datatransfer.attributes.CourseAttributes;

/**
 * Represents the "Courses" page for Instructors.
 */
public class InstructorCoursesPage extends AppPage {

    @FindBy(id = "btn-add-course")
    private WebElement addCourseButton;

    @FindBy(id = "new-course-id")
    private WebElement courseIdTextBox;

    @FindBy(id = "new-course-name")
    private WebElement courseNameTextBox;

    @FindBy(id = "new-time-zone")
    private WebElement timeZoneDropdown;

    @FindBy(id = "btn-save-course")
    private WebElement submitButton;

    @FindBy(id = "sort-course-name")
    private WebElement sortByCourseNameIcon;

    @FindBy (id = "sort-course-id")
    private WebElement sortByCourseIdIcon;

    @FindBy (id = "sort-creation-date")
    private WebElement sortByCreationDateIcon;

    @FindBy(id = "active-courses-table")
    private WebElement activeCoursesTable;

    @FindBy (id = "archived-courses-table")
    private WebElement archivedCoursesTable;

    @FindBy (id = "deleted-courses-table")
    private WebElement deletedCoursesTable;

    @FindBy(id = "deleted-table-heading")
    private WebElement deleteTableHeading;

    @FindBy(id = "archived-table-heading")
    private WebElement archiveTableHeading;

    public InstructorCoursesPage(Browser browser) {
        super(browser);
    }

    @Override
    protected boolean containsExpectedPageContents() {
        return getPageTitle().contains("Courses");
    }

    public void verifyActiveCoursesDetails(CourseAttributes[] courses) {
        String[][] courseDetails = getCourseDetails(courses);
        // use verifyTableBodyValues as active courses are sorted
        verifyTableBodyValues(activeCoursesTable, courseDetails);
    }

    public void verifyActiveCourseStatistics(CourseAttributes course, String numSections, String numTeams,
                                             String numStudents, String numUnregistered) {
        showStatistics(course.getId());
        String[] courseDetail = { course.getId(), course.getName(), course.getCreatedAtDateString(),
                numSections, numTeams, numStudents, numUnregistered };
        verifyTableRowValues(getActiveTableRow(course.getId()), courseDetail);
    }

    public void verifyArchivedCoursesDetails(CourseAttributes[] courses) {
        showArchiveTable();
        String[][] courseDetails = getCourseDetails(courses);
        for (int i = 0; i < courses.length; i++) {
            // use verifyTableRowValues as archive courses are not sorted
            verifyTableRowValues(getArchivedTableRow(courses[i].getId()), courseDetails[i]);
        }
    }

    public void verifyDeletedCoursesDetails(CourseAttributes[] courses) {
        showDeleteTable();
        String[][] courseDetails = getDeletedCourseDetails(courses);
        for (int i = 0; i < courses.length; i++) {
            // use verifyTableRowValues as deleted courses are not sorted
            verifyTableRowValues(getDeletedTableRow(courses[i].getId()), courseDetails[i]);
        }
    }

    public void verifyNotModifiable(String courseId) {
        // verify enroll button is disabled
        int courseRowNumber = getRowNumberOfCourse(courseId);
        assertTrue(isElementPresent(By.id("btn-enroll-disabled-" + courseRowNumber)));
        assertFalse(isElementPresent(By.id("btn-enroll-" + courseRowNumber)));

        // verify delete button is disabled
        click(getOtherActionsButton(courseId));
        assertTrue(isElementPresent(By.id("btn-soft-delete-disabled-" + courseRowNumber)));
        assertFalse(isElementPresent(By.id("btn-soft-delete-" + courseRowNumber)));
        click(getOtherActionsButton(courseId));
    }

    public void verifyNumActiveCourses(int expectedNum) {
        assertEquals(expectedNum, getCourseCount());
    }

    public void verifyNumArchivedCourses(int expectedNum) {
        assertEquals(expectedNum, getArchivedCourseCount());
    }

    public void verifyNumDeletedCourses(int expectedNum) {
        assertEquals(expectedNum, getDeletedCourseCount());
    }

    public void verifyStatusMessageWithLinks(String expected, String[] expectedLinks) {
        WebElement statusMessage = browser.driver.findElement(By.className("mat-snack-bar-container"));
        assertEquals(expected, statusMessage.getText());

        List<WebElement> actualLinks = statusMessage.findElements(By.tagName("a"));
        for (int i = 0; i < expectedLinks.length; i++) {
            assertTrue(actualLinks.get(i).getAttribute("href").contains(expectedLinks[i]));
        }
    }

    public void addCourse(CourseAttributes newCourse) {
        click(addCourseButton);

        fillTextBox(courseIdTextBox, newCourse.getId());
        fillTextBox(courseNameTextBox, newCourse.getName());
        selectNewTimeZone(newCourse.getTimeZone().toString());
        waitForPageToLoad();

        click(submitButton);
        waitForElementPresence(By.className("mat-snack-bar-container"));
    }

    public void showStatistics(String courseId) {
        try {
            click(getShowStatisticsLink(courseId));
            waitForPageToLoad();
        } catch (NoSuchElementException e) {
            // Do nothing
        }
    }

    public void archiveCourse(String courseId) {
        WebElement otherActionButton = getOtherActionsButton(courseId);
        click(otherActionButton);
        click(getArchiveButton(courseId));

        waitForElementStaleness(otherActionButton);
    }

    public void moveCourseToRecycleBin(String courseId) {
        WebElement otherActionButton = getOtherActionsButton(courseId);
        click(otherActionButton);
        clickAndConfirm(getMoveToRecycleBinButton(courseId));

        waitForElementStaleness(otherActionButton);
    }

    public void unarchiveCourse(String courseId) {
        WebElement unarchiveButton = getUnarchiveButton(courseId);
        click(unarchiveButton);

        waitForElementStaleness(unarchiveButton);
    }

    public void moveArchivedCourseToRecycleBin(String courseId) {
        WebElement moveArchivedToRecycleBinButton = getMoveArchivedToRecycleBinButton(courseId);
        clickAndConfirm(moveArchivedToRecycleBinButton);

        waitForElementStaleness(moveArchivedToRecycleBinButton);
    }

    public void showDeleteTable() {
        if (!isElementVisible(By.id("deleted-course-id-0"))) {
            click(deleteTableHeading);
        }
    }

    public void showArchiveTable() {
        if (!isElementVisible(By.id("archived-course-id-0"))) {
            click(archiveTableHeading);
        }
    }

    public void restoreCourse(String courseId) {
        WebElement restoreButton = getRestoreButton(courseId);
        click(restoreButton);

        waitForElementStaleness(restoreButton);
    }

    public void deleteCourse(String courseId) {
        WebElement deleteButton = getDeleteButton(courseId);
        clickAndConfirm(deleteButton);

        waitForElementStaleness(deleteButton);
    }

    public void restoreAllCourses() {
        WebElement restoreAllButton = getRestoreAllButton();
        click(restoreAllButton);

        waitForElementStaleness(restoreAllButton);
    }

    public void deleteAllCourses() {
        WebElement deleteAllButton = getDeleteAllButton();
        clickAndConfirm(deleteAllButton);

        waitForElementStaleness(deleteAllButton);
    }

    public void sortByCourseName() {
        click(sortByCourseNameIcon);
        waitForPageToLoad();
    }

    public void sortByCourseId() {
        click(sortByCourseIdIcon);
        waitForPageToLoad();
    }

    public void sortByCreationDate() {
        click(sortByCreationDateIcon);
        waitForPageToLoad();
    }

    private WebElement getActiveTableRow(String courseId) {
        int courseRowNumber = getRowNumberOfCourse(courseId);
        return activeCoursesTable.findElements(By.cssSelector("tbody tr")).get(courseRowNumber);
    }

    private WebElement getArchivedTableRow(String courseId) {
        int courseRowNumber = getRowNumberOfArchivedCourse(courseId);
        return archivedCoursesTable.findElements(By.cssSelector("tbody tr")).get(courseRowNumber);
    }

    private WebElement getDeletedTableRow(String courseId) {
        int courseRowNumber = getRowNumberOfDeletedCourse(courseId);
        return deletedCoursesTable.findElements(By.cssSelector("tbody tr")).get(courseRowNumber);
    }

    private String[][] getCourseDetails(CourseAttributes[] courses) {
        String[][] courseDetails = new String[courses.length][3];
        for (int i = 0; i < courses.length; i++) {
            String[] courseDetail = { courses[i].getId(), courses[i].getName(),
                    getDateString(courses[i].getCreatedAt()) };
            courseDetails[i] = courseDetail;
        }
        return courseDetails;
    }

    private String getDateString(Instant instant) {
        return DateTimeFormatter
                .ofPattern("d MMM yyyy")
                .withZone(ZoneId.systemDefault())
                .format(instant);
    }

    private String[][] getDeletedCourseDetails(CourseAttributes[] courses) {
        String[][] courseDetails = new String[courses.length][4];
        for (int i = 0; i < courses.length; i++) {
            String[] courseDetail = {courses[i].getId(), courses[i].getName(),
                    getDateString(courses[i].getCreatedAt()), getDateString(courses[i].getDeletedAt()) };
            courseDetails[i] = courseDetail;
        }
        return courseDetails;
    }

    private WebElement getRestoreAllButton() {
        return browser.driver.findElement(By.id("btn-restore-all"));
    }

    private WebElement getDeleteAllButton() {
        return browser.driver.findElement(By.id("btn-delete-all"));
    }

    private void selectNewTimeZone(String timeZone) {
        Select dropdown = new Select(timeZoneDropdown);
        dropdown.selectByValue(timeZone);
    }

    private WebElement getShowStatisticsLink(String courseId) {
        int courseRowNumber = getRowNumberOfCourse(courseId);
        return getShowStatisticsLinkInRow(courseRowNumber);
    }

    private WebElement getOtherActionsButton(String courseId) {
        int courseRowNumber = getRowNumberOfCourse(courseId);
        return getOtherActionsButtonInRow(courseRowNumber);
    }

    private WebElement getArchiveButton(String courseId) {
        int courseRowNumber = getRowNumberOfCourse(courseId);
        return getArchiveButtonInRow(courseRowNumber);
    }

    private WebElement getMoveToRecycleBinButton(String courseId) {
        int courseRowNumber = getRowNumberOfCourse(courseId);
        return getMoveToRecycleBinButtonInRow(courseRowNumber);
    }

    private WebElement getUnarchiveButton(String courseId) {
        int courseRowNumber = getRowNumberOfArchivedCourse(courseId);
        return getUnarchiveButtonInRow(courseRowNumber);
    }

    private WebElement getMoveArchivedToRecycleBinButton(String courseId) {
        int courseRowNumber = getRowNumberOfArchivedCourse(courseId);
        return getMoveArchivedToRecycleBinButtonInRow(courseRowNumber);
    }

    private WebElement getRestoreButton(String courseId) {
        showDeleteTable();
        int courseRowNumber = getRowNumberOfDeletedCourse(courseId);
        return getRestoreButtonInRow(courseRowNumber);
    }

    private WebElement getDeleteButton(String courseId) {
        showDeleteTable();
        int courseRowNumber = getRowNumberOfDeletedCourse(courseId);
        return getDeleteButtonInRow(courseRowNumber);
    }

    private int getCourseCount() {
        try {
            return activeCoursesTable.findElements(By.cssSelector("tbody tr")).size();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    private int getArchivedCourseCount() {
        try {
            return archivedCoursesTable.findElements(By.cssSelector("tbody tr")).size();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    private int getDeletedCourseCount() {
        try {
            return deletedCoursesTable.findElements(By.cssSelector("tbody tr")).size();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    private int getRowNumberOfCourse(String courseId) {
        for (int i = 0; i < getCourseCount(); i++) {
            if (getCourseIdCell(i).getText().equals(courseId)) {
                return i;
            }
        }
        return -1;
    }

    private int getRowNumberOfArchivedCourse(String courseId) {
        for (int i = 0; i < getArchivedCourseCount(); i++) {
            if (getArchivedCourseIdCell(i).getText().equals(courseId)) {
                return i;
            }
        }
        return -1;
    }

    private int getRowNumberOfDeletedCourse(String courseId) {
        for (int i = 0; i < getDeletedCourseCount(); i++) {
            if (getDeletedCourseIdCell(i).getText().equals(courseId)) {
                return i;
            }
        }
        return -1;
    }

    private WebElement getCourseIdCell(int rowId) {
        return browser.driver.findElement(By.id("course-id-" + rowId));
    }

    private WebElement getArchivedCourseIdCell(int rowId) {
        return browser.driver.findElement(By.id("archived-course-id-" + rowId));
    }

    private WebElement getDeletedCourseIdCell(int rowId) {
        return browser.driver.findElement(By.id("deleted-course-id-" + rowId));
    }

    private WebElement getShowStatisticsLinkInRow(int rowId) {
        By showStatisticsLink = By.id("show-statistics-" + rowId);
        return browser.driver.findElement(showStatisticsLink);
    }

    private WebElement getOtherActionsButtonInRow(int rowId) {
        By otherActionsButton = By.id("btn-other-actions-" + rowId);
        return browser.driver.findElement(otherActionsButton);
    }

    private WebElement getArchiveButtonInRow(int rowId) {
        By archiveButton = By.id("btn-archive-" + rowId);
        return browser.driver.findElement(archiveButton);
    }

    private WebElement getMoveToRecycleBinButtonInRow(int rowId) {
        By moveToRecycleBinButton = By.id("btn-soft-delete-" + rowId);
        return browser.driver.findElement(moveToRecycleBinButton);
    }

    private WebElement getUnarchiveButtonInRow(int rowId) {
        By archiveButton = By.id("btn-unarchive-" + rowId);
        return browser.driver.findElement(archiveButton);
    }

    private WebElement getMoveArchivedToRecycleBinButtonInRow(int rowId) {
        By moveToRecycleBinButton = By.id("btn-soft-delete-archived-" + rowId);
        return browser.driver.findElement(moveToRecycleBinButton);
    }

    private WebElement getRestoreButtonInRow(int rowId) {
        By restoreButton = By.id("btn-restore-" + rowId);
        return browser.driver.findElement(restoreButton);
    }

    private WebElement getDeleteButtonInRow(int rowId) {
        By deleteButton = By.id("btn-delete-" + rowId);
        return browser.driver.findElement(deleteButton);
    }
}

