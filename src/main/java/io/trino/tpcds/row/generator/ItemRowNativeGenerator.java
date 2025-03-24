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
import io.trino.tpcds.row.ItemRow;
import io.trino.tpcds.type.Decimal;

import javax.annotation.concurrent.NotThreadSafe;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.ITEM;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_BRAND;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_BRAND_ID;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_CATEGORY;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_CATEGORY_ID;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_CLASS;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_CLASS_ID;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_COLOR;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_CONTAINER;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_CURRENT_PRICE_FLAGS;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_CURRENT_PRICE_NUMBER;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_CURRENT_PRICE_PRECISION;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_CURRENT_PRICE_SCALE;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_FORMULATION;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_ITEM_DESC;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_ITEM_ID;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_ITEM_SK;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_MANAGER_ID;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_MANUFACT;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_MANUFACT_ID;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_PADDING1;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_PADDING2;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_PRODUCT_NAME;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_PROMO_SK;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_REC_END_DATE;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_REC_START_DATE;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_SIZE;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_UNITS;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_WHOLESALE_COST_FLAGS;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_WHOLESALE_COST_NUMBER;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_WHOLESALE_COST_PRECISION;
import static io.trino.tpcds.column.generator.ItemNativeGeneratorColumn.I_WHOLESALE_COST_SCALE;
import static io.trino.tpcds.generator.ItemGeneratorColumn.I_NULLS;

@NotThreadSafe
public class ItemRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_item";
    private final StructLayout iRowLayout;

    public ItemRowNativeGenerator()
    {
        super(ITEM);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            iRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(I_ITEM_SK.getName()),
                    columnToLayoutMap.get(I_ITEM_ID.getName()),
                    columnToLayoutMap.get(I_REC_START_DATE.getName()),
                    columnToLayoutMap.get(I_REC_END_DATE.getName()),
                    columnToLayoutMap.get(I_ITEM_DESC.getName()),
                    columnToLayoutMap.get(I_BRAND_ID.getName()),
                    columnToLayoutMap.get(I_BRAND.getName()),
                    columnToLayoutMap.get(I_CLASS_ID.getName()),
                    columnToLayoutMap.get(I_CLASS.getName()),
                    columnToLayoutMap.get(I_CATEGORY_ID.getName()),
                    columnToLayoutMap.get(I_CATEGORY.getName()),
                    columnToLayoutMap.get(I_MANUFACT_ID.getName()),
                    columnToLayoutMap.get(I_MANUFACT.getName()),
                    columnToLayoutMap.get(I_SIZE.getName()),
                    columnToLayoutMap.get(I_FORMULATION.getName()),
                    columnToLayoutMap.get(I_COLOR.getName()),
                    columnToLayoutMap.get(I_UNITS.getName()),
                    columnToLayoutMap.get(I_CONTAINER.getName()),
                    columnToLayoutMap.get(I_MANAGER_ID.getName()),
                    columnToLayoutMap.get(I_PRODUCT_NAME.getName()),
                    columnToLayoutMap.get(I_PROMO_SK.getName()),
                    columnToLayoutMap.get(I_CURRENT_PRICE_NUMBER.getName()),
                    columnToLayoutMap.get(I_CURRENT_PRICE_PRECISION.getName()),
                    columnToLayoutMap.get(I_CURRENT_PRICE_SCALE.getName()),
                    columnToLayoutMap.get(I_CURRENT_PRICE_FLAGS.getName()),
                    columnToLayoutMap.get(I_PADDING1.getName()),
                    columnToLayoutMap.get(I_WHOLESALE_COST_NUMBER.getName()),
                    columnToLayoutMap.get(I_WHOLESALE_COST_PRECISION.getName()),
                    columnToLayoutMap.get(I_WHOLESALE_COST_SCALE.getName()),
                    columnToLayoutMap.get(I_WHOLESALE_COST_FLAGS.getName()),
                    columnToLayoutMap.get(I_PADDING2.getName()));
            allocateRow(iRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("ItemRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find item row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new ItemRow(createNullBitMap(ITEM, getRandomNumberStream(I_NULLS)),
                nativeLong(I_ITEM_SK),
                nativeString(I_ITEM_ID),
                nativeLong(I_REC_START_DATE),
                nativeLong(I_REC_END_DATE),
                nativeString(I_ITEM_DESC),
                new Decimal(nativeDecimal(I_CURRENT_PRICE_NUMBER), nativeInt(I_CURRENT_PRICE_PRECISION)),
                new Decimal(nativeDecimal(I_WHOLESALE_COST_NUMBER), nativeInt(I_WHOLESALE_COST_PRECISION)),
                nativeLong(I_BRAND_ID),
                nativeString(I_BRAND),
                nativeLong(I_CLASS_ID),
                nativeString(I_CLASS),
                nativeLong(I_CATEGORY_ID),
                nativeString(I_CATEGORY),
                nativeLong(I_MANUFACT_ID),
                nativeString(I_MANUFACT),
                nativeString(I_SIZE),
                nativeString(I_FORMULATION),
                nativeString(I_COLOR),
                nativeString(I_UNITS),
                nativeString(I_CONTAINER),
                nativeLong(I_MANAGER_ID),
                nativeString(I_PRODUCT_NAME),
                nativeLong(I_PROMO_SK)));
    }
}
