package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.UserServlet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger log = getLogger(UserServlet.class);
    static List<String> info = new ArrayList<>();

    @Autowired
    private MealService service;

    static {
        SLF4JBridgeHandler.install();
    }

    private static void logInfo(Description description, String status, long nanos){
        String testName = description.getMethodName();
        log.info(String.format("Test %s %s, in %d microseconds", testName, status, TimeUnit.NANOSECONDS.toMicros(nanos)));
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();



    @AfterClass
    public static void close(){
        info.forEach(System.out::println);

    }

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        public long runtime(TimeUnit unit) {
            return super.runtime(unit);
        }

        @Override
        protected void succeeded(long nanos, Description description) {
            logInfo(description, "succeeded", nanos);
            info.add(description + " succeeded in "+nanos);
        }

        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            logInfo(description, "failed", nanos);
            info.add(description + " failed in "+nanos);
        }

        @Override
        protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
            logInfo(description, "skipped", nanos);
            info.add(description + " skipped in "+nanos);
        }

        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, "finished", nanos);
            info.add(description + " finished in "+nanos);
        }
    };


    @Test
    public void delete() throws Exception {
        int start = LocalTime.now().getNano();
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
        int end = LocalTime.now().getNano();
        getLogger("Delete : " + (end-start) + "ms");
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        int start = LocalTime.now().getNano();
        service.delete(MEAL1_ID, 1);

        int end = LocalTime.now().getNano();
        getLogger("deleteNotFound : " + (end-start) + "ms");
    }

    @Test
    public void create() throws Exception {
        int start = LocalTime.now().getNano();


        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
        int end = LocalTime.now().getNano();
        getLogger("create : " + (end-start) + "ms");
    }

    @Test
    public void get() throws Exception {
        int start = LocalTime.now().getNano();

        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
        int end = LocalTime.now().getNano();
        getLogger("get : " + (end-start) + "ms");

    }

    @Test(expected = NotFoundException.class)

    public void getNotFound() throws Exception {
    int start = LocalTime.now().getNano();
        service.get(MEAL1_ID, ADMIN_ID);

        int end = LocalTime.now().getNano();
        getLogger("getNotFound : " + (end-start) + "ms");
    }

    @Test
    public void update() throws Exception {
        int start = LocalTime.now().getNano();

        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);

        int end = LocalTime.now().getNano();
        getLogger("update : " + (end-start) + "ms");
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        int start = LocalTime.now().getNano();

        service.update(MEAL1, ADMIN_ID);
        int end = LocalTime.now().getNano();
        getLogger("updateNotFound : " + (end-start) + "ms");
    }

    @Test
    public void getAll() throws Exception {
        int start = LocalTime.now().getNano();


        assertMatch(service.getAll(USER_ID), MEALS);
        int end = LocalTime.now().getNano();
        getLogger("getAll : " + (end-start) + "ms");
    }

    @Test
    public void getBetween() throws Exception {
        int start = LocalTime.now().getNano();

        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);

        int end = LocalTime.now().getNano();
        getLogger("getBetween : " + (end-start) + "ms");
    }
}