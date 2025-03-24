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
import io.trino.tpcds.row.WebSiteRow;
import io.trino.tpcds.type.Decimal;

import javax.annotation.concurrent.NotThreadSafe;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.WEB_SITE;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_CITY;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_CLASS;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_CLOSE_DATE_SK;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_COMPANY_ID;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_COMPANY_NAME;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_COUNTRY;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_COUNTY;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_GMT_OFFSET;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_MANAGER;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_MARKET_CLASS;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_MARKET_DESC;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_MARKET_ID;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_MARKET_MANAGER;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_NAME;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_OPEN_DATE_SK;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_PADDING1;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_PADDING2;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_REC_END_DATE;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_REC_START_DATE;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_SITE_ID;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_SITE_SK;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_STATE;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_STREET_NAME;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_STREET_NAME2;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_STREET_NUMBER;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_STREET_TYPE;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_SUITE_NUMBER;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_TAX_PERCENTAGE_FLAGS;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_TAX_PERCENTAGE_NUMBER;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_TAX_PERCENTAGE_PRECISION;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_TAX_PERCENTAGE_SCALE;
import static io.trino.tpcds.column.generator.WebSiteNativeGeneratorColumn.WEB_ZIP;
import static io.trino.tpcds.generator.WebSiteGeneratorColumn.WEB_NULLS;
import static io.trino.tpcds.type.Address.AddressBuilder;

@NotThreadSafe
public class WebSiteRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_web_site";
    private final StructLayout webRowLayout;

    public WebSiteRowNativeGenerator()
    {
        super(WEB_SITE);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            webRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(WEB_SITE_SK.getName()),
                    columnToLayoutMap.get(WEB_SITE_ID.getName()),
                    columnToLayoutMap.get(WEB_REC_START_DATE.getName()),
                    columnToLayoutMap.get(WEB_REC_END_DATE.getName()),
                    columnToLayoutMap.get(WEB_NAME.getName()),
                    columnToLayoutMap.get(WEB_OPEN_DATE_SK.getName()),
                    columnToLayoutMap.get(WEB_CLOSE_DATE_SK.getName()),
                    columnToLayoutMap.get(WEB_CLASS.getName()),
                    columnToLayoutMap.get(WEB_MANAGER.getName()),
                    columnToLayoutMap.get(WEB_MARKET_CLASS.getName()),
                    columnToLayoutMap.get(WEB_MARKET_DESC.getName()),
                    columnToLayoutMap.get(WEB_MARKET_MANAGER.getName()),
                    columnToLayoutMap.get(WEB_COMPANY_NAME.getName()),
                    columnToLayoutMap.get(WEB_MARKET_ID.getName()),
                    columnToLayoutMap.get(WEB_COMPANY_ID.getName()),
                    columnToLayoutMap.get(WEB_STREET_NAME.getName()),
                    columnToLayoutMap.get(WEB_STREET_NAME2.getName()),
                    columnToLayoutMap.get(WEB_STREET_TYPE.getName()),
                    columnToLayoutMap.get(WEB_SUITE_NUMBER.getName()),
                    columnToLayoutMap.get(WEB_CITY.getName()),
                    columnToLayoutMap.get(WEB_COUNTY.getName()),
                    columnToLayoutMap.get(WEB_STATE.getName()),
                    columnToLayoutMap.get(WEB_COUNTRY.getName()),
                    columnToLayoutMap.get(WEB_STREET_NUMBER.getName()),
                    columnToLayoutMap.get(WEB_ZIP.getName()),
                    columnToLayoutMap.get(WEB_GMT_OFFSET.getName()),
                    columnToLayoutMap.get(WEB_PADDING1.getName()),
                    columnToLayoutMap.get(WEB_TAX_PERCENTAGE_NUMBER.getName()),
                    columnToLayoutMap.get(WEB_TAX_PERCENTAGE_PRECISION.getName()),
                    columnToLayoutMap.get(WEB_TAX_PERCENTAGE_SCALE.getName()),
                    columnToLayoutMap.get(WEB_TAX_PERCENTAGE_FLAGS.getName()),
                    columnToLayoutMap.get(WEB_PADDING2.getName()));
            allocateRow(webRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("WebSiteRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find web site row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        AddressBuilder addressBuilder = new AddressBuilder();
        addressBuilder.setStreetName1(nativeString(WEB_STREET_NAME));
        addressBuilder.setStreetName2(nativeString(WEB_STREET_NAME2));
        addressBuilder.setStreetType(nativeString(WEB_STREET_TYPE));
        addressBuilder.setSuiteNumber(nativeString(WEB_SUITE_NUMBER));
        addressBuilder.setCity(nativeString(WEB_CITY));
        addressBuilder.setCounty(nativeString(WEB_COUNTY));
        addressBuilder.setState(nativeString(WEB_STATE));
        addressBuilder.setCountry(nativeString(WEB_COUNTRY));
        addressBuilder.setStreetNumber(nativeInt(WEB_STREET_NUMBER));
        addressBuilder.setZip(nativeInt(WEB_ZIP));
        addressBuilder.setGmtOffset(nativeInt(WEB_GMT_OFFSET));
        return new RowGeneratorResult(new WebSiteRow(createNullBitMap(WEB_SITE, getRandomNumberStream(WEB_NULLS)),
                nativeLong(WEB_SITE_SK),
                nativeString(WEB_SITE_ID),
                nativeLong(WEB_REC_START_DATE),
                nativeLong(WEB_REC_END_DATE),
                nativeString(WEB_NAME),
                nativeLong(WEB_OPEN_DATE_SK),
                nativeLong(WEB_CLOSE_DATE_SK),
                nativeString(WEB_CLASS),
                nativeString(WEB_MANAGER),
                nativeInt(WEB_MARKET_ID),
                nativeString(WEB_MARKET_CLASS),
                nativeString(WEB_MARKET_DESC),
                nativeString(WEB_MARKET_MANAGER),
                nativeInt(WEB_COMPANY_ID),
                nativeString(WEB_COMPANY_NAME),
                addressBuilder.build(),
                new Decimal(nativeDecimal(WEB_TAX_PERCENTAGE_NUMBER), nativeInt(WEB_TAX_PERCENTAGE_PRECISION))));
    }
}
