package com.raizlabs.android.dbflow.test.querymodel;

import android.database.Cursor;

import com.raizlabs.android.dbflow.sql.language.ColumnAlias;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.test.FlowTestCase;

import java.util.List;
import java.util.UUID;

/**
 * Description: Tests the {@link TestQueryModel} to ensure it works as expected.
 */
public class QueryModelTest extends FlowTestCase {


    public void testSalaryModel() {
        Delete.tables(SalaryModel.class);

        SalaryModel salaryModel = new SalaryModel();
        salaryModel.uid = UUID.randomUUID().toString();
        salaryModel.salary = 15000;
        salaryModel.name = "Andrew Grosner";
        salaryModel.department = "Developer";
        salaryModel.save();

        salaryModel = new SalaryModel();
        salaryModel.uid = UUID.randomUUID().toString();
        salaryModel.salary = 30000;
        salaryModel.name = "Bill Gates";
        salaryModel.department = "Developer";
        salaryModel.save();

        Where<SalaryModel> selectQuery = new Select(ColumnAlias.column(SalaryModel$Table.DEPARTMENT),
                                                    ColumnAlias.column(SalaryModel$Table.SALARY).as("average_salary"),
                                                    ColumnAlias.column(SalaryModel$Table.NAME).as("newName"))
                                            .from(SalaryModel.class).where().limit(1).groupBy(SalaryModel$Table.DEPARTMENT);

        TestQueryModel testQueryModel = selectQuery.queryCustomSingle(TestQueryModel.class);

        assertTrue(testQueryModel.average_salary > 0);

        assertNotNull(testQueryModel.newName);

        assertNotNull(testQueryModel.department);
        assertEquals(testQueryModel.department, "Developer");

        List<TestQueryModel> testQueryModels = selectQuery.queryCustomList(TestQueryModel.class);
        assertTrue(!testQueryModels.isEmpty());

        TestQueryModel model = testQueryModels.get(0);
        assertEquals(model, testQueryModel);


        Delete.tables(SalaryModel.class);
    }
}
