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
import io.trino.tpcds.row.CatalogPageRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.CATALOG_PAGE;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.CatalogPageColumn.CP_CATALOG_NUMBER;
import static io.trino.tpcds.column.CatalogPageColumn.CP_CATALOG_PAGE_ID;
import static io.trino.tpcds.column.CatalogPageColumn.CP_CATALOG_PAGE_NUMBER;
import static io.trino.tpcds.column.CatalogPageColumn.CP_CATALOG_PAGE_SK;
import static io.trino.tpcds.column.CatalogPageColumn.CP_DEPARTMENT;
import static io.trino.tpcds.column.CatalogPageColumn.CP_DESCRIPTION;
import static io.trino.tpcds.column.CatalogPageColumn.CP_END_DATE_SK;
import static io.trino.tpcds.column.CatalogPageColumn.CP_START_DATE_SK;
import static io.trino.tpcds.column.CatalogPageColumn.CP_TYPE;
import static io.trino.tpcds.generator.CatalogPageGeneratorColumn.CP_NULLS;

public class CatalogPageRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_catalog_page";
    private final StructLayout cpRowLayout;

    public CatalogPageRowNativeGenerator()
    {
        super(CATALOG_PAGE);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            cpRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(CP_CATALOG_PAGE_SK.getName()),
                    columnToLayoutMap.get(CP_CATALOG_PAGE_ID.getName()),
                    columnToLayoutMap.get(CP_START_DATE_SK.getName()),
                    columnToLayoutMap.get(CP_END_DATE_SK.getName()),
                    columnToLayoutMap.get(CP_DEPARTMENT.getName()),
                    columnToLayoutMap.get(CP_DESCRIPTION.getName()),
                    columnToLayoutMap.get(CP_TYPE.getName()),
                    columnToLayoutMap.get(CP_CATALOG_NUMBER.getName()),
                    columnToLayoutMap.get(CP_CATALOG_PAGE_NUMBER.getName()));
            allocateRow(cpRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("CatalogPageRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find catalog page row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new CatalogPageRow(
                nativeLong(CP_CATALOG_PAGE_SK),
                nativeString(CP_CATALOG_PAGE_ID),
                nativeLong(CP_START_DATE_SK),
                nativeLong(CP_END_DATE_SK),
                nativeString(CP_DEPARTMENT),
                nativeInt(CP_CATALOG_NUMBER),
                nativeInt(CP_CATALOG_PAGE_NUMBER),
                nativeString(CP_DESCRIPTION),
                nativeString(CP_TYPE),
                createNullBitMap(CATALOG_PAGE, getRandomNumberStream(CP_NULLS))));
    }
}
