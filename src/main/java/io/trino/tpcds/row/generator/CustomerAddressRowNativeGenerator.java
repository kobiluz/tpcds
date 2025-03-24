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
import io.trino.tpcds.row.CustomerAddressRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.CUSTOMER_ADDRESS;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_ADDRESS_ID;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_ADDRESS_SK;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_CITY;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_COUNTRY;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_COUNTY;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_GMT_OFFSET;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_LOCATION_TYPE;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_PADDING;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_STATE;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_STREET_NAME;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_STREET_NAME2;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_STREET_NUMBER;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_STREET_TYPE;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_SUITE_NUMBER;
import static io.trino.tpcds.column.generator.CustomerAddressNativeGeneratorColumn.CA_ZIP;
import static io.trino.tpcds.generator.CustomerAddressGeneratorColumn.CA_NULLS;
import static io.trino.tpcds.type.Address.AddressBuilder;

public class CustomerAddressRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_customer_address";
    private final StructLayout caRowLayout;

    public CustomerAddressRowNativeGenerator()
    {
        super(CUSTOMER_ADDRESS);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            caRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(CA_ADDRESS_SK.getName()),
                    columnToLayoutMap.get(CA_ADDRESS_ID.getName()),
                    columnToLayoutMap.get(CA_LOCATION_TYPE.getName()),
                    columnToLayoutMap.get(CA_STREET_NAME.getName()),
                    columnToLayoutMap.get(CA_STREET_NAME2.getName()),
                    columnToLayoutMap.get(CA_STREET_TYPE.getName()),
                    columnToLayoutMap.get(CA_SUITE_NUMBER.getName()),
                    columnToLayoutMap.get(CA_CITY.getName()),
                    columnToLayoutMap.get(CA_COUNTY.getName()),
                    columnToLayoutMap.get(CA_STATE.getName()),
                    columnToLayoutMap.get(CA_COUNTRY.getName()),
                    columnToLayoutMap.get(CA_STREET_NUMBER.getName()),
                    columnToLayoutMap.get(CA_ZIP.getName()),
                    columnToLayoutMap.get(CA_GMT_OFFSET.getName()),
                    columnToLayoutMap.get(CA_PADDING.getName()));
            allocateRow(caRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("CustomerAddressRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find customer address row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        AddressBuilder addressBuilder = new AddressBuilder();
        addressBuilder.setStreetName1(nativeString(CA_STREET_NAME));
        addressBuilder.setStreetName2(nativeString(CA_STREET_NAME2));
        addressBuilder.setStreetType(nativeString(CA_STREET_TYPE));
        addressBuilder.setSuiteNumber(nativeString(CA_SUITE_NUMBER));
        addressBuilder.setCity(nativeString(CA_CITY));
        addressBuilder.setCounty(nativeString(CA_COUNTY));
        addressBuilder.setState(nativeString(CA_STATE));
        addressBuilder.setCountry(nativeString(CA_COUNTRY));
        addressBuilder.setStreetNumber(nativeInt(CA_STREET_NUMBER));
        addressBuilder.setZip(nativeInt(CA_ZIP));
        addressBuilder.setGmtOffset(nativeInt(CA_GMT_OFFSET));
        return new RowGeneratorResult(new CustomerAddressRow(createNullBitMap(CUSTOMER_ADDRESS, getRandomNumberStream(CA_NULLS)),
                nativeLong(CA_ADDRESS_SK),
                nativeString(CA_ADDRESS_ID),
                addressBuilder.build(),
                nativeString(CA_LOCATION_TYPE)));
    }
}
