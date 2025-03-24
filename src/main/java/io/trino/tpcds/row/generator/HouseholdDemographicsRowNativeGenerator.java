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
import io.trino.tpcds.row.HouseholdDemographicsRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.HOUSEHOLD_DEMOGRAPHICS;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.HouseholdDemographicsNativeGeneratorColumn.HD_BUY_POTENTIAL;
import static io.trino.tpcds.column.generator.HouseholdDemographicsNativeGeneratorColumn.HD_DEMO_SK;
import static io.trino.tpcds.column.generator.HouseholdDemographicsNativeGeneratorColumn.HD_DEP_COUNT;
import static io.trino.tpcds.column.generator.HouseholdDemographicsNativeGeneratorColumn.HD_INCOME_BAND_SK;
import static io.trino.tpcds.column.generator.HouseholdDemographicsNativeGeneratorColumn.HD_VEHICLE_COUNT;
import static io.trino.tpcds.generator.HouseholdDemographicsGeneratorColumn.HD_NULLS;

public class HouseholdDemographicsRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_household_demographics";
    private final StructLayout hdRowLayout;

    public HouseholdDemographicsRowNativeGenerator()
    {
        super(HOUSEHOLD_DEMOGRAPHICS);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            hdRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(HD_DEMO_SK.getName()),
                    columnToLayoutMap.get(HD_INCOME_BAND_SK.getName()),
                    columnToLayoutMap.get(HD_BUY_POTENTIAL.getName()),
                    columnToLayoutMap.get(HD_DEP_COUNT.getName()),
                    columnToLayoutMap.get(HD_VEHICLE_COUNT.getName()));
            allocateRow(hdRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("HouseholdDemographicsRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find household demographic generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new HouseholdDemographicsRow(createNullBitMap(HOUSEHOLD_DEMOGRAPHICS, getRandomNumberStream(HD_NULLS)),
                nativeLong(HD_DEMO_SK),
                nativeLong(HD_INCOME_BAND_SK),
                nativeString(HD_BUY_POTENTIAL),
                nativeInt(HD_DEP_COUNT),
                nativeInt(HD_VEHICLE_COUNT)));
    }
}
