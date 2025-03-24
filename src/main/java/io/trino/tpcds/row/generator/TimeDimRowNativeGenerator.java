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
import io.trino.tpcds.row.TimeDimRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.TIME_DIM;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.TimeDimNativeGeneratorColumn.T_AM_PM;
import static io.trino.tpcds.column.generator.TimeDimNativeGeneratorColumn.T_HOUR;
import static io.trino.tpcds.column.generator.TimeDimNativeGeneratorColumn.T_MEAL_TIME;
import static io.trino.tpcds.column.generator.TimeDimNativeGeneratorColumn.T_MINUTE;
import static io.trino.tpcds.column.generator.TimeDimNativeGeneratorColumn.T_SECOND;
import static io.trino.tpcds.column.generator.TimeDimNativeGeneratorColumn.T_SHIFT;
import static io.trino.tpcds.column.generator.TimeDimNativeGeneratorColumn.T_SUB_SHIFT;
import static io.trino.tpcds.column.generator.TimeDimNativeGeneratorColumn.T_TIME;
import static io.trino.tpcds.column.generator.TimeDimNativeGeneratorColumn.T_TIME_ID;
import static io.trino.tpcds.column.generator.TimeDimNativeGeneratorColumn.T_TIME_SK;
import static io.trino.tpcds.generator.TimeDimGeneratorColumn.T_NULLS;

public class TimeDimRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_time";
    private final StructLayout tRowLayout;

    public TimeDimRowNativeGenerator()
    {
        super(TIME_DIM);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            tRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(T_TIME_SK.getName()),
                    columnToLayoutMap.get(T_TIME_ID.getName()),
                    columnToLayoutMap.get(T_AM_PM.getName()),
                    columnToLayoutMap.get(T_SHIFT.getName()),
                    columnToLayoutMap.get(T_SUB_SHIFT.getName()),
                    columnToLayoutMap.get(T_MEAL_TIME.getName()),
                    columnToLayoutMap.get(T_TIME.getName()),
                    columnToLayoutMap.get(T_HOUR.getName()),
                    columnToLayoutMap.get(T_MINUTE.getName()),
                    columnToLayoutMap.get(T_SECOND.getName()));
            allocateRow(tRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("TimeDimRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find time dim row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new TimeDimRow(createNullBitMap(TIME_DIM, getRandomNumberStream(T_NULLS)),
                nativeLong(T_TIME_SK),
                nativeString(T_TIME_ID),
                nativeInt(T_TIME),
                nativeInt(T_HOUR),
                nativeInt(T_MINUTE),
                nativeInt(T_SECOND),
                nativeString(T_AM_PM),
                nativeString(T_SHIFT),
                nativeString(T_SUB_SHIFT),
                nativeString(T_MEAL_TIME)));
    }
}
