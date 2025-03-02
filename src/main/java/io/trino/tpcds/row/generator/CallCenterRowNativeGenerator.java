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
import io.trino.tpcds.row.CallCenterRow;
import io.trino.tpcds.type.Decimal;

import javax.annotation.concurrent.NotThreadSafe;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.CALL_CENTER;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.CallCenterColumn.CC_CALL_CENTER_ID;
import static io.trino.tpcds.column.CallCenterColumn.CC_CALL_CENTER_SK;
import static io.trino.tpcds.column.CallCenterColumn.CC_CITY;
import static io.trino.tpcds.column.CallCenterColumn.CC_CLASS;
import static io.trino.tpcds.column.CallCenterColumn.CC_CLOSED_DATE_SK;
import static io.trino.tpcds.column.CallCenterColumn.CC_COMPANY;
import static io.trino.tpcds.column.CallCenterColumn.CC_COMPANY_NAME;
import static io.trino.tpcds.column.CallCenterColumn.CC_COUNTRY;
import static io.trino.tpcds.column.CallCenterColumn.CC_COUNTY;
import static io.trino.tpcds.column.CallCenterColumn.CC_DIVISION;
import static io.trino.tpcds.column.CallCenterColumn.CC_DIVISION_NAME;
import static io.trino.tpcds.column.CallCenterColumn.CC_EMPLOYEES;
import static io.trino.tpcds.column.CallCenterColumn.CC_GMT_OFFSET;
import static io.trino.tpcds.column.CallCenterColumn.CC_HOURS;
import static io.trino.tpcds.column.CallCenterColumn.CC_MANAGER;
import static io.trino.tpcds.column.CallCenterColumn.CC_MARKET_MANAGER;
import static io.trino.tpcds.column.CallCenterColumn.CC_MKT_CLASS;
import static io.trino.tpcds.column.CallCenterColumn.CC_MKT_DESC;
import static io.trino.tpcds.column.CallCenterColumn.CC_MKT_ID;
import static io.trino.tpcds.column.CallCenterColumn.CC_NAME;
import static io.trino.tpcds.column.CallCenterColumn.CC_OPEN_DATE_SK;
import static io.trino.tpcds.column.CallCenterColumn.CC_PADDING1;
import static io.trino.tpcds.column.CallCenterColumn.CC_PADDING2;
import static io.trino.tpcds.column.CallCenterColumn.CC_REC_END_DATE;
import static io.trino.tpcds.column.CallCenterColumn.CC_REC_START_DATE;
import static io.trino.tpcds.column.CallCenterColumn.CC_SQ_FT;
import static io.trino.tpcds.column.CallCenterColumn.CC_STATE;
import static io.trino.tpcds.column.CallCenterColumn.CC_STREET_NAME;
import static io.trino.tpcds.column.CallCenterColumn.CC_STREET_NAME2;
import static io.trino.tpcds.column.CallCenterColumn.CC_STREET_NUMBER;
import static io.trino.tpcds.column.CallCenterColumn.CC_STREET_TYPE;
import static io.trino.tpcds.column.CallCenterColumn.CC_SUITE_NUMBER;
import static io.trino.tpcds.column.CallCenterColumn.CC_TAX_PERCENTAGE_FLAGS;
import static io.trino.tpcds.column.CallCenterColumn.CC_TAX_PERCENTAGE_NUMBER;
import static io.trino.tpcds.column.CallCenterColumn.CC_TAX_PERCENTAGE_PRECISION;
import static io.trino.tpcds.column.CallCenterColumn.CC_TAX_PERCENTAGE_SCALE;
import static io.trino.tpcds.column.CallCenterColumn.CC_ZIP;
import static io.trino.tpcds.generator.CallCenterGeneratorColumn.CC_NULLS;
import static io.trino.tpcds.type.Address.AddressBuilder;

@NotThreadSafe
public class CallCenterRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_call_center";
    private final StructLayout ccRowLayout;

    public CallCenterRowNativeGenerator()
    {
        super(CALL_CENTER);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            ccRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(CC_CALL_CENTER_SK.getName()),
                    columnToLayoutMap.get(CC_CALL_CENTER_ID.getName()),
                    columnToLayoutMap.get(CC_REC_START_DATE.getName()),
                    columnToLayoutMap.get(CC_REC_END_DATE.getName()),
                    columnToLayoutMap.get(CC_CLOSED_DATE_SK.getName()),
                    columnToLayoutMap.get(CC_OPEN_DATE_SK.getName()),
                    columnToLayoutMap.get(CC_NAME.getName()),
                    columnToLayoutMap.get(CC_CLASS.getName()),
                    columnToLayoutMap.get(CC_HOURS.getName()),
                    columnToLayoutMap.get(CC_MANAGER.getName()),
                    columnToLayoutMap.get(CC_MKT_CLASS.getName()),
                    columnToLayoutMap.get(CC_MKT_DESC.getName()),
                    columnToLayoutMap.get(CC_MARKET_MANAGER.getName()),
                    columnToLayoutMap.get(CC_DIVISION_NAME.getName()),
                    columnToLayoutMap.get(CC_COMPANY_NAME.getName()),
                    columnToLayoutMap.get(CC_EMPLOYEES.getName()),
                    columnToLayoutMap.get(CC_SQ_FT.getName()),
                    columnToLayoutMap.get(CC_MKT_ID.getName()),
                    columnToLayoutMap.get(CC_DIVISION.getName()),
                    columnToLayoutMap.get(CC_COMPANY.getName()),
                    columnToLayoutMap.get(CC_PADDING1.getName()),
                    columnToLayoutMap.get(CC_STREET_NAME.getName()),
                    columnToLayoutMap.get(CC_STREET_NAME2.getName()),
                    columnToLayoutMap.get(CC_STREET_TYPE.getName()),
                    columnToLayoutMap.get(CC_SUITE_NUMBER.getName()),
                    columnToLayoutMap.get(CC_CITY.getName()),
                    columnToLayoutMap.get(CC_COUNTY.getName()),
                    columnToLayoutMap.get(CC_STATE.getName()),
                    columnToLayoutMap.get(CC_COUNTRY.getName()),
                    columnToLayoutMap.get(CC_STREET_NUMBER.getName()),
                    columnToLayoutMap.get(CC_ZIP.getName()),
                    columnToLayoutMap.get(CC_GMT_OFFSET.getName()),
                    columnToLayoutMap.get(CC_PADDING2.getName()),
                    columnToLayoutMap.get(CC_TAX_PERCENTAGE_NUMBER.getName()),
                    columnToLayoutMap.get(CC_TAX_PERCENTAGE_PRECISION.getName()),
                    columnToLayoutMap.get(CC_TAX_PERCENTAGE_SCALE.getName()),
                    columnToLayoutMap.get(CC_TAX_PERCENTAGE_FLAGS.getName()));
            allocateRow(ccRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("CallCenterRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find call center row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        CallCenterRow.Builder builder = new CallCenterRow.Builder();
        builder.setNullBitMap(createNullBitMap(CALL_CENTER, getRandomNumberStream(CC_NULLS)));
        builder.setCcCallCenterSk(nativeLong(CC_CALL_CENTER_SK));
        builder.setCcCallCenterId(nativeString(CC_CALL_CENTER_ID));
        builder.setCcRecStartDateId(nativeLong(CC_REC_START_DATE));
        builder.setCcRecEndDateId(nativeLong(CC_REC_END_DATE));
        builder.setCcClosedDateId(nativeLong(CC_CLOSED_DATE_SK));
        builder.setCcOpenDateId(nativeLong(CC_OPEN_DATE_SK));
        builder.setCcName(nativeString(CC_NAME));
        builder.setCcClass(nativeString(CC_CLASS));
        builder.setCcHours(nativeString(CC_HOURS));
        builder.setCcManager(nativeString(CC_MANAGER));
        builder.setCcMarketClass(nativeString(CC_MKT_CLASS));
        builder.setCcMarketDesc(nativeString(CC_MKT_DESC));
        builder.setCcMarketManager(nativeString(CC_MARKET_MANAGER));
        builder.setCcDivisionName(nativeString(CC_DIVISION_NAME));
        builder.setCcCompanyName(nativeString(CC_COMPANY_NAME));
        builder.setCcEmployees(nativeInt(CC_EMPLOYEES));
        builder.setCcSqFt(nativeInt(CC_SQ_FT));
        builder.setCcMarketId(nativeInt(CC_MKT_ID));
        builder.setCcDivisionId(nativeInt(CC_DIVISION));
        builder.setCcCompany(nativeInt(CC_COMPANY));

        AddressBuilder addressBuilder = new AddressBuilder();
        addressBuilder.setStreetName1(nativeString(CC_STREET_NAME));
        addressBuilder.setStreetName2(nativeString(CC_STREET_NAME2));
        addressBuilder.setStreetType(nativeString(CC_STREET_TYPE));
        addressBuilder.setSuiteNumber(nativeString(CC_SUITE_NUMBER));
        addressBuilder.setCity(nativeString(CC_CITY));
        addressBuilder.setCounty(nativeString(CC_COUNTY));
        addressBuilder.setState(nativeString(CC_STATE));
        addressBuilder.setCountry(nativeString(CC_COUNTRY));
        addressBuilder.setStreetNumber(nativeInt(CC_STREET_NUMBER));
        addressBuilder.setZip(nativeInt(CC_ZIP));
        addressBuilder.setGmtOffset(nativeInt(CC_GMT_OFFSET));
        builder.setCcAddress(addressBuilder.build());
        builder.setCcTaxPercentage(new Decimal(nativeDecimal(CC_TAX_PERCENTAGE_NUMBER), nativeInt(CC_TAX_PERCENTAGE_PRECISION)));
        return new RowGeneratorResult(builder.build());
    }
}
