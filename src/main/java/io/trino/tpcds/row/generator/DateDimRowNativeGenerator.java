/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.trino.tpcds.row.generator;

import io.trino.tpcds.Session;
import io.trino.tpcds.row.DateDimRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.DATE_DIM;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.DateDimColumn.D_CURRENT_DAY;
import static io.trino.tpcds.column.DateDimColumn.D_CURRENT_MONTH;
import static io.trino.tpcds.column.DateDimColumn.D_CURRENT_QUARTER;
import static io.trino.tpcds.column.DateDimColumn.D_CURRENT_WEEK;
import static io.trino.tpcds.column.DateDimColumn.D_CURRENT_YEAR;
import static io.trino.tpcds.column.DateDimColumn.D_DATE_ID;
import static io.trino.tpcds.column.DateDimColumn.D_DATE_SK;
import static io.trino.tpcds.column.DateDimColumn.D_DAY_NAME;
import static io.trino.tpcds.column.DateDimColumn.D_DOM;
import static io.trino.tpcds.column.DateDimColumn.D_DOW;
import static io.trino.tpcds.column.DateDimColumn.D_FIRST_DOM;
import static io.trino.tpcds.column.DateDimColumn.D_FOLLOWING_HOLIDAY;
import static io.trino.tpcds.column.DateDimColumn.D_FY_QUARTER_SEQ;
import static io.trino.tpcds.column.DateDimColumn.D_FY_WEEK_SEQ;
import static io.trino.tpcds.column.DateDimColumn.D_FY_YEAR;
import static io.trino.tpcds.column.DateDimColumn.D_HOLIDAY;
import static io.trino.tpcds.column.DateDimColumn.D_LAST_DOM;
import static io.trino.tpcds.column.DateDimColumn.D_MONTH_SEQ;
import static io.trino.tpcds.column.DateDimColumn.D_MOY;
import static io.trino.tpcds.column.DateDimColumn.D_PADDING;
import static io.trino.tpcds.column.DateDimColumn.D_QOY;
import static io.trino.tpcds.column.DateDimColumn.D_QUARTER_SEQ;
import static io.trino.tpcds.column.DateDimColumn.D_SAME_DAY_LQ;
import static io.trino.tpcds.column.DateDimColumn.D_SAME_DAY_LY;
import static io.trino.tpcds.column.DateDimColumn.D_WEEKEND;
import static io.trino.tpcds.column.DateDimColumn.D_WEEK_SEQ;
import static io.trino.tpcds.column.DateDimColumn.D_YEAR;
import static io.trino.tpcds.generator.DateDimGeneratorColumn.D_NULLS;

public class DateDimRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_date";
    private final StructLayout dRowLayout;

    public DateDimRowNativeGenerator()
    {
        super(DATE_DIM);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            dRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(D_DATE_SK.getName()),
                    columnToLayoutMap.get(D_DATE_ID.getName()),
                    columnToLayoutMap.get(D_DAY_NAME.getName()),
                    columnToLayoutMap.get(D_MONTH_SEQ.getName()),
                    columnToLayoutMap.get(D_WEEK_SEQ.getName()),
                    columnToLayoutMap.get(D_QUARTER_SEQ.getName()),
                    columnToLayoutMap.get(D_YEAR.getName()),
                    columnToLayoutMap.get(D_DOW.getName()),
                    columnToLayoutMap.get(D_MOY.getName()),
                    columnToLayoutMap.get(D_DOM.getName()),
                    columnToLayoutMap.get(D_QOY.getName()),
                    columnToLayoutMap.get(D_FY_YEAR.getName()),
                    columnToLayoutMap.get(D_FY_QUARTER_SEQ.getName()),
                    columnToLayoutMap.get(D_FY_WEEK_SEQ.getName()),
                    columnToLayoutMap.get(D_HOLIDAY.getName()),
                    columnToLayoutMap.get(D_WEEKEND.getName()),
                    columnToLayoutMap.get(D_FOLLOWING_HOLIDAY.getName()),
                    columnToLayoutMap.get(D_FIRST_DOM.getName()),
                    columnToLayoutMap.get(D_LAST_DOM.getName()),
                    columnToLayoutMap.get(D_SAME_DAY_LY.getName()),
                    columnToLayoutMap.get(D_SAME_DAY_LQ.getName()),
                    columnToLayoutMap.get(D_CURRENT_DAY.getName()),
                    columnToLayoutMap.get(D_CURRENT_WEEK.getName()),
                    columnToLayoutMap.get(D_CURRENT_MONTH.getName()),
                    columnToLayoutMap.get(D_CURRENT_QUARTER.getName()),
                    columnToLayoutMap.get(D_CURRENT_YEAR.getName()),
                    columnToLayoutMap.get(D_PADDING.getName()));
            allocateRow(dRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("DateDimRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find date dim generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new DateDimRow(createNullBitMap(DATE_DIM, getRandomNumberStream(D_NULLS)),
                nativeLong(D_DATE_SK),
                nativeString(D_DATE_ID),
                nativeInt(D_MONTH_SEQ),
                nativeInt(D_WEEK_SEQ),
                nativeInt(D_QUARTER_SEQ),
                nativeInt(D_YEAR),
                nativeInt(D_DOW),
                nativeInt(D_MOY),
                nativeInt(D_DOM),
                nativeInt(D_QOY),
                nativeInt(D_FY_YEAR),
                nativeInt(D_FY_QUARTER_SEQ),
                nativeInt(D_FY_WEEK_SEQ),
                nativeString(D_DAY_NAME),
                nativeInt(D_HOLIDAY) != 0,
                nativeInt(D_WEEKEND) != 0,
                nativeInt(D_FOLLOWING_HOLIDAY) != 0,
                nativeInt(D_FIRST_DOM),
                nativeInt(D_LAST_DOM),
                nativeInt(D_SAME_DAY_LY),
                nativeInt(D_SAME_DAY_LQ),
                nativeInt(D_CURRENT_DAY) != 0,
                nativeInt(D_CURRENT_WEEK) != 0,
                nativeInt(D_CURRENT_MONTH) != 0,
                nativeInt(D_CURRENT_QUARTER) != 0,
                nativeInt(D_CURRENT_YEAR) != 0));
    }
}
