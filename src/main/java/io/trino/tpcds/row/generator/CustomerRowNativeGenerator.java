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
import io.trino.tpcds.row.CustomerRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.CUSTOMER;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_BIRTH_COUNTRY;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_BIRTH_DAY;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_BIRTH_MONTH;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_BIRTH_YEAR;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_CURRENT_ADDR_SK;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_CURRENT_CDEMO_SK;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_CURRENT_HDEMO_SK;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_CUSTOMER_ID;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_CUSTOMER_SK;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_EMAIL_ADDRESS;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_FIRST_NAME;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_FIRST_SALES_DATE_SK;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_FIRST_SHIPTO_DATE_SK;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_LAST_NAME;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_LAST_REVIEW_DATE_SK;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_LOGIN;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_PADDING;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_PREFERRED_CUST_FLAG;
import static io.trino.tpcds.column.generator.CustomerNativeGeneratorColumn.C_SALUTATION;
import static io.trino.tpcds.generator.CustomerGeneratorColumn.C_NULLS;

public class CustomerRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_customer";
    private final StructLayout cRowLayout;

    public CustomerRowNativeGenerator()
    {
        super(CUSTOMER);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            cRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(C_CUSTOMER_SK.getName()),
                    columnToLayoutMap.get(C_CUSTOMER_ID.getName()),
                    columnToLayoutMap.get(C_CURRENT_CDEMO_SK.getName()),
                    columnToLayoutMap.get(C_CURRENT_HDEMO_SK.getName()),
                    columnToLayoutMap.get(C_CURRENT_ADDR_SK.getName()),
                    columnToLayoutMap.get(C_SALUTATION.getName()),
                    columnToLayoutMap.get(C_FIRST_NAME.getName()),
                    columnToLayoutMap.get(C_LAST_NAME.getName()),
                    columnToLayoutMap.get(C_BIRTH_COUNTRY.getName()),
                    columnToLayoutMap.get(C_LOGIN.getName()),
                    columnToLayoutMap.get(C_EMAIL_ADDRESS.getName()),
                    columnToLayoutMap.get(C_FIRST_SHIPTO_DATE_SK.getName()),
                    columnToLayoutMap.get(C_FIRST_SALES_DATE_SK.getName()),
                    columnToLayoutMap.get(C_PREFERRED_CUST_FLAG.getName()),
                    columnToLayoutMap.get(C_BIRTH_DAY.getName()),
                    columnToLayoutMap.get(C_BIRTH_MONTH.getName()),
                    columnToLayoutMap.get(C_BIRTH_YEAR.getName()),
                    columnToLayoutMap.get(C_LAST_REVIEW_DATE_SK.getName()),
                    columnToLayoutMap.get(C_PADDING.getName()));
            allocateRow(cRowLayout.byteSize());
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("fail");
        }
        catch (Throwable t) {
            System.err.println("CustomerRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find customer row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new CustomerRow(nativeLong(C_CUSTOMER_SK),
                nativeString(C_CUSTOMER_ID),
                nativeLong(C_CURRENT_CDEMO_SK),
                nativeLong(C_CURRENT_HDEMO_SK),
                nativeLong(C_CURRENT_ADDR_SK),
                nativeInt(C_FIRST_SHIPTO_DATE_SK),
                nativeInt(C_FIRST_SALES_DATE_SK),
                nativeString(C_SALUTATION),
                nativeString(C_FIRST_NAME),
                nativeString(C_LAST_NAME),
                nativeInt(C_PREFERRED_CUST_FLAG) != 0,
                nativeInt(C_BIRTH_DAY),
                nativeInt(C_BIRTH_MONTH),
                nativeInt(C_BIRTH_YEAR),
                nativeString(C_BIRTH_COUNTRY),
                nativeString(C_EMAIL_ADDRESS),
                nativeInt(C_LAST_REVIEW_DATE_SK),
                nativeString(C_LOGIN),
                createNullBitMap(CUSTOMER, getRandomNumberStream(C_NULLS))));
    }
}
