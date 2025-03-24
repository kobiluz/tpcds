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
import io.trino.tpcds.row.WarehouseRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.WAREHOUSE;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_CITY;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_COUNTRY;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_COUNTY;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_GMT_OFFSET;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_PADDING1;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_PADDING2;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_STATE;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_STREET_NAME;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_STREET_NAME2;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_STREET_NUMBER;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_STREET_TYPE;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_SUITE_NUMBER;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_WAREHOUSE_ID;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_WAREHOUSE_NAME;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_WAREHOUSE_SK;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_WAREHOUSE_SQ_FT;
import static io.trino.tpcds.column.generator.WarehouseNativeGeneratorColumn.W_ZIP;
import static io.trino.tpcds.generator.WarehouseGeneratorColumn.W_NULLS;
import static io.trino.tpcds.type.Address.AddressBuilder;

public class WarehouseRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_warehouse";
    private final StructLayout wRowLayout;

    public WarehouseRowNativeGenerator()
    {
        super(WAREHOUSE);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            wRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(W_WAREHOUSE_SK.getName()),
                    columnToLayoutMap.get(W_WAREHOUSE_ID.getName()),
                    columnToLayoutMap.get(W_WAREHOUSE_NAME.getName()),
                    columnToLayoutMap.get(W_WAREHOUSE_SQ_FT.getName()),
                    columnToLayoutMap.get(W_PADDING1.getName()),
                    columnToLayoutMap.get(W_STREET_NAME.getName()),
                    columnToLayoutMap.get(W_STREET_NAME2.getName()),
                    columnToLayoutMap.get(W_STREET_TYPE.getName()),
                    columnToLayoutMap.get(W_SUITE_NUMBER.getName()),
                    columnToLayoutMap.get(W_CITY.getName()),
                    columnToLayoutMap.get(W_COUNTY.getName()),
                    columnToLayoutMap.get(W_STATE.getName()),
                    columnToLayoutMap.get(W_COUNTRY.getName()),
                    columnToLayoutMap.get(W_STREET_NUMBER.getName()),
                    columnToLayoutMap.get(W_ZIP.getName()),
                    columnToLayoutMap.get(W_GMT_OFFSET.getName()),
                    columnToLayoutMap.get(W_PADDING2.getName()));
            allocateRow(wRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("WarehouseRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find warehouse row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        AddressBuilder addressBuilder = new AddressBuilder();
        addressBuilder.setStreetName1(nativeString(W_STREET_NAME));
        addressBuilder.setStreetName2(nativeString(W_STREET_NAME2));
        addressBuilder.setStreetType(nativeString(W_STREET_TYPE));
        addressBuilder.setSuiteNumber(nativeString(W_SUITE_NUMBER));
        addressBuilder.setCity(nativeString(W_CITY));
        addressBuilder.setCounty(nativeString(W_COUNTY));
        addressBuilder.setState(nativeString(W_STATE));
        addressBuilder.setCountry(nativeString(W_COUNTRY));
        addressBuilder.setStreetNumber(nativeInt(W_STREET_NUMBER));
        addressBuilder.setZip(nativeInt(W_ZIP));
        addressBuilder.setGmtOffset(nativeInt(W_GMT_OFFSET));
        return new RowGeneratorResult(new WarehouseRow(createNullBitMap(WAREHOUSE, getRandomNumberStream(W_NULLS)),
                nativeLong(W_WAREHOUSE_SK),
                nativeString(W_WAREHOUSE_ID),
                nativeString(W_WAREHOUSE_NAME),
                nativeInt(W_WAREHOUSE_SQ_FT),
                addressBuilder.build()));
    }
}
