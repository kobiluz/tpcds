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
import io.trino.tpcds.row.StoreRow;
import io.trino.tpcds.type.Decimal;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.STORE;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.StoreColumn.S_CITY;
import static io.trino.tpcds.column.StoreColumn.S_CLOSED_DATE_SK;
import static io.trino.tpcds.column.StoreColumn.S_COMPANY_ID;
import static io.trino.tpcds.column.StoreColumn.S_COMPANY_NAME;
import static io.trino.tpcds.column.StoreColumn.S_COUNTRY;
import static io.trino.tpcds.column.StoreColumn.S_COUNTY;
import static io.trino.tpcds.column.StoreColumn.S_DIVISION_ID;
import static io.trino.tpcds.column.StoreColumn.S_DIVISION_NAME;
import static io.trino.tpcds.column.StoreColumn.S_FLOOR_SPACE;
import static io.trino.tpcds.column.StoreColumn.S_GEOGRAPHY_CLASS;
import static io.trino.tpcds.column.StoreColumn.S_GMT_OFFSET;
import static io.trino.tpcds.column.StoreColumn.S_HOURS;
import static io.trino.tpcds.column.StoreColumn.S_MANAGER;
import static io.trino.tpcds.column.StoreColumn.S_MARKET_DESC;
import static io.trino.tpcds.column.StoreColumn.S_MARKET_ID;
import static io.trino.tpcds.column.StoreColumn.S_MARKET_MANAGER;
import static io.trino.tpcds.column.StoreColumn.S_NUMBER_EMPLOYEES;
import static io.trino.tpcds.column.StoreColumn.S_PADDING1;
import static io.trino.tpcds.column.StoreColumn.S_PADDING2;
import static io.trino.tpcds.column.StoreColumn.S_PADDING3;
import static io.trino.tpcds.column.StoreColumn.S_REC_END_DATE;
import static io.trino.tpcds.column.StoreColumn.S_REC_START_DATE;
import static io.trino.tpcds.column.StoreColumn.S_STATE;
import static io.trino.tpcds.column.StoreColumn.S_STORE_ID;
import static io.trino.tpcds.column.StoreColumn.S_STORE_NAME;
import static io.trino.tpcds.column.StoreColumn.S_STORE_SK;
import static io.trino.tpcds.column.StoreColumn.S_STREET_NAME;
import static io.trino.tpcds.column.StoreColumn.S_STREET_NAME2;
import static io.trino.tpcds.column.StoreColumn.S_STREET_NUMBER;
import static io.trino.tpcds.column.StoreColumn.S_STREET_TYPE;
import static io.trino.tpcds.column.StoreColumn.S_SUITE_NUMBER;
import static io.trino.tpcds.column.StoreColumn.S_TAX_PRECENTAGE_FLAGS;
import static io.trino.tpcds.column.StoreColumn.S_TAX_PRECENTAGE_NUMBER;
import static io.trino.tpcds.column.StoreColumn.S_TAX_PRECENTAGE_PRECISION;
import static io.trino.tpcds.column.StoreColumn.S_TAX_PRECENTAGE_SCALE;
import static io.trino.tpcds.column.StoreColumn.S_ZIP;
import static io.trino.tpcds.generator.StoreGeneratorColumn.W_STORE_NULLS;
import static io.trino.tpcds.type.Address.AddressBuilder;

public class StoreRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_store";
    private final StructLayout sRowLayout;

    public StoreRowNativeGenerator()
    {
        super(STORE);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            sRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(S_STORE_SK.getName()),
                    columnToLayoutMap.get(S_STORE_ID.getName()),
                    columnToLayoutMap.get(S_REC_START_DATE.getName()),
                    columnToLayoutMap.get(S_REC_END_DATE.getName()),
                    columnToLayoutMap.get(S_CLOSED_DATE_SK.getName()),
                    columnToLayoutMap.get(S_STORE_NAME.getName()),
                    columnToLayoutMap.get(S_HOURS.getName()),
                    columnToLayoutMap.get(S_MANAGER.getName()),
                    columnToLayoutMap.get(S_GEOGRAPHY_CLASS.getName()),
                    columnToLayoutMap.get(S_MARKET_DESC.getName()),
                    columnToLayoutMap.get(S_MARKET_MANAGER.getName()),
                    columnToLayoutMap.get(S_DIVISION_ID.getName()),
                    columnToLayoutMap.get(S_DIVISION_NAME.getName()),
                    columnToLayoutMap.get(S_COMPANY_ID.getName()),
                    columnToLayoutMap.get(S_COMPANY_NAME.getName()),
                    columnToLayoutMap.get(S_NUMBER_EMPLOYEES.getName()),
                    columnToLayoutMap.get(S_FLOOR_SPACE.getName()),
                    columnToLayoutMap.get(S_MARKET_ID.getName()),
                    columnToLayoutMap.get(S_PADDING1.getName()),
                    columnToLayoutMap.get(S_TAX_PRECENTAGE_NUMBER.getName()),
                    columnToLayoutMap.get(S_TAX_PRECENTAGE_PRECISION.getName()),
                    columnToLayoutMap.get(S_TAX_PRECENTAGE_SCALE.getName()),
                    columnToLayoutMap.get(S_TAX_PRECENTAGE_FLAGS.getName()),
                    columnToLayoutMap.get(S_PADDING2.getName()),
                    columnToLayoutMap.get(S_STREET_NAME.getName()),
                    columnToLayoutMap.get(S_STREET_NAME2.getName()),
                    columnToLayoutMap.get(S_STREET_TYPE.getName()),
                    columnToLayoutMap.get(S_SUITE_NUMBER.getName()),
                    columnToLayoutMap.get(S_CITY.getName()),
                    columnToLayoutMap.get(S_COUNTY.getName()),
                    columnToLayoutMap.get(S_STATE.getName()),
                    columnToLayoutMap.get(S_COUNTRY.getName()),
                    columnToLayoutMap.get(S_STREET_NUMBER.getName()),
                    columnToLayoutMap.get(S_ZIP.getName()),
                    columnToLayoutMap.get(S_GMT_OFFSET.getName()),
                    columnToLayoutMap.get(S_PADDING3.getName()));
            allocateRow(sRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("StoreRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find store row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        AddressBuilder addressBuilder = new AddressBuilder();
        addressBuilder.setStreetName1(nativeString(S_STREET_NAME));
        addressBuilder.setStreetName2(nativeString(S_STREET_NAME2));
        addressBuilder.setStreetType(nativeString(S_STREET_TYPE));
        addressBuilder.setSuiteNumber(nativeString(S_SUITE_NUMBER));
        addressBuilder.setCity(nativeString(S_CITY));
        addressBuilder.setCounty(nativeString(S_COUNTY));
        addressBuilder.setState(nativeString(S_STATE));
        addressBuilder.setCountry(nativeString(S_COUNTRY));
        addressBuilder.setStreetNumber(nativeInt(S_STREET_NUMBER));
        addressBuilder.setZip(nativeInt(S_ZIP));
        addressBuilder.setGmtOffset(nativeInt(S_GMT_OFFSET));
        return new RowGeneratorResult(new StoreRow(createNullBitMap(STORE, getRandomNumberStream(W_STORE_NULLS)),
                nativeLong(S_STORE_SK),
                nativeString(S_STORE_ID),
                nativeLong(S_REC_START_DATE),
                nativeLong(S_REC_END_DATE),
                nativeLong(S_CLOSED_DATE_SK),
                nativeString(S_STORE_NAME),
                nativeInt(S_NUMBER_EMPLOYEES),
                nativeInt(S_FLOOR_SPACE),
                nativeString(S_HOURS),
                nativeString(S_MANAGER),
                nativeInt(S_MARKET_ID),
                new Decimal(nativeDecimal(S_TAX_PRECENTAGE_NUMBER), nativeInt(S_TAX_PRECENTAGE_PRECISION)),
                nativeString(S_GEOGRAPHY_CLASS),
                nativeString(S_MARKET_DESC),
                nativeString(S_MARKET_MANAGER),
                nativeLong(S_DIVISION_ID),
                nativeString(S_COMPANY_NAME),
                nativeLong(S_COMPANY_ID),
                nativeString(S_COMPANY_NAME),
                addressBuilder.build()));
    }
}
