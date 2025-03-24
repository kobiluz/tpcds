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
import io.trino.tpcds.row.WebPageRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.WEB_PAGE;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_ACCESS_DATE_SK;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_AUTOGEN_FLAG;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_CHAR_COUNT;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_CREATION_DATE_SK;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_CUSTOMER_SK;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_IMAGE_COUNT;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_LINK_COUNT;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_MAX_AD_COUNT;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_PADDING;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_REC_END_DATE;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_REC_START_DATE;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_TYPE;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_URL;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_WEB_PAGE_ID;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_WEB_PAGE_SK;
import static io.trino.tpcds.column.generator.WebPageNativeGeneratorColumn.WP_WEB_SITE_ID;
import static io.trino.tpcds.generator.WebPageGeneratorColumn.WP_NULLS;

public class WebPageRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_web_page";
    private final StructLayout wpRowLayout;

    public WebPageRowNativeGenerator()
    {
        super(WEB_PAGE);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            wpRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(WP_WEB_PAGE_SK.getName()),
                    columnToLayoutMap.get(WP_WEB_PAGE_ID.getName()),
                    columnToLayoutMap.get(WP_WEB_SITE_ID.getName()),
                    columnToLayoutMap.get(WP_REC_START_DATE.getName()),
                    columnToLayoutMap.get(WP_REC_END_DATE.getName()),
                    columnToLayoutMap.get(WP_CREATION_DATE_SK.getName()),
                    columnToLayoutMap.get(WP_ACCESS_DATE_SK.getName()),
                    columnToLayoutMap.get(WP_CUSTOMER_SK.getName()),
                    columnToLayoutMap.get(WP_URL.getName()),
                    columnToLayoutMap.get(WP_TYPE.getName()),
                    columnToLayoutMap.get(WP_AUTOGEN_FLAG.getName()),
                    columnToLayoutMap.get(WP_CHAR_COUNT.getName()),
                    columnToLayoutMap.get(WP_LINK_COUNT.getName()),
                    columnToLayoutMap.get(WP_IMAGE_COUNT.getName()),
                    columnToLayoutMap.get(WP_MAX_AD_COUNT.getName()),
                    columnToLayoutMap.get(WP_PADDING.getName()));
            allocateRow(wpRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("WebPageRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find web page row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new WebPageRow(createNullBitMap(WEB_PAGE, getRandomNumberStream(WP_NULLS)),
                nativeLong(WP_WEB_PAGE_SK),
                nativeString(WP_WEB_PAGE_ID),
                nativeLong(WP_REC_START_DATE),
                nativeLong(WP_REC_END_DATE),
                nativeLong(WP_CREATION_DATE_SK),
                nativeLong(WP_ACCESS_DATE_SK),
                nativeInt(WP_AUTOGEN_FLAG) != 0,
                nativeLong(WP_CUSTOMER_SK),
                nativeString(WP_URL),
                nativeString(WP_TYPE),
                nativeInt(WP_CHAR_COUNT),
                nativeInt(WP_LINK_COUNT),
                nativeInt(WP_IMAGE_COUNT),
                nativeInt(WP_MAX_AD_COUNT)));
    }
}
