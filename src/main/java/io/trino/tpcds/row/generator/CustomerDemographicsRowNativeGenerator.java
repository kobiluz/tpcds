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
import io.trino.tpcds.row.CustomerDemographicsRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.CUSTOMER_DEMOGRAPHICS;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.CustomerDemographicsNativeGeneratorColumn.CD_CREDIT_RATING;
import static io.trino.tpcds.column.generator.CustomerDemographicsNativeGeneratorColumn.CD_DEMO_SK;
import static io.trino.tpcds.column.generator.CustomerDemographicsNativeGeneratorColumn.CD_DEP_COLLEGE_COUNT;
import static io.trino.tpcds.column.generator.CustomerDemographicsNativeGeneratorColumn.CD_DEP_COUNT;
import static io.trino.tpcds.column.generator.CustomerDemographicsNativeGeneratorColumn.CD_DEP_EMPLOYED_COUNT;
import static io.trino.tpcds.column.generator.CustomerDemographicsNativeGeneratorColumn.CD_EDUCATION_STATUS;
import static io.trino.tpcds.column.generator.CustomerDemographicsNativeGeneratorColumn.CD_GENDER;
import static io.trino.tpcds.column.generator.CustomerDemographicsNativeGeneratorColumn.CD_MARITAL_STATUS;
import static io.trino.tpcds.column.generator.CustomerDemographicsNativeGeneratorColumn.CD_PURCHASE_ESTIMATE;
import static io.trino.tpcds.generator.CustomerDemographicsGeneratorColumn.CD_NULLS;

public class CustomerDemographicsRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_customer_demographics";
    private final StructLayout cdRowLayout;

    public CustomerDemographicsRowNativeGenerator()
    {
        super(CUSTOMER_DEMOGRAPHICS);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            cdRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(CD_DEMO_SK.getName()),
                    columnToLayoutMap.get(CD_GENDER.getName()),
                    columnToLayoutMap.get(CD_MARITAL_STATUS.getName()),
                    columnToLayoutMap.get(CD_EDUCATION_STATUS.getName()),
                    columnToLayoutMap.get(CD_CREDIT_RATING.getName()),
                    columnToLayoutMap.get(CD_PURCHASE_ESTIMATE.getName()),
                    columnToLayoutMap.get(CD_DEP_COUNT.getName()),
                    columnToLayoutMap.get(CD_DEP_EMPLOYED_COUNT.getName()),
                    columnToLayoutMap.get(CD_DEP_COLLEGE_COUNT.getName()));
            allocateRow(cdRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("CustomerDemographicsRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find customer demographics row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new CustomerDemographicsRow(createNullBitMap(CUSTOMER_DEMOGRAPHICS, getRandomNumberStream(CD_NULLS)),
                nativeLong(CD_DEMO_SK),
                nativeString(CD_GENDER),
                nativeString(CD_MARITAL_STATUS),
                nativeString(CD_EDUCATION_STATUS),
                nativeInt(CD_PURCHASE_ESTIMATE),
                nativeString(CD_CREDIT_RATING),
                nativeInt(CD_DEP_COUNT),
                nativeInt(CD_DEP_EMPLOYED_COUNT),
                nativeInt(CD_DEP_COLLEGE_COUNT)));
    }
}
