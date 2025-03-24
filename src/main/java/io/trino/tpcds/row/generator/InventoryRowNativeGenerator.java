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
import io.trino.tpcds.row.InventoryRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.INVENTORY;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.InventoryNativeGeneratorColumn.INV_DATE_SK;
import static io.trino.tpcds.column.generator.InventoryNativeGeneratorColumn.INV_ITEM_SK;
import static io.trino.tpcds.column.generator.InventoryNativeGeneratorColumn.INV_PADDING;
import static io.trino.tpcds.column.generator.InventoryNativeGeneratorColumn.INV_QUANTITY_ON_HAND;
import static io.trino.tpcds.column.generator.InventoryNativeGeneratorColumn.INV_WAREHOUSE_SK;
import static io.trino.tpcds.generator.InventoryGeneratorColumn.INV_NULLS;

public class InventoryRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_inventory";
    private final StructLayout invRowLayout;

    public InventoryRowNativeGenerator()
    {
        super(INVENTORY);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            invRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(INV_DATE_SK.getName()),
                    columnToLayoutMap.get(INV_ITEM_SK.getName()),
                    columnToLayoutMap.get(INV_WAREHOUSE_SK.getName()),
                    columnToLayoutMap.get(INV_QUANTITY_ON_HAND.getName()),
                    columnToLayoutMap.get(INV_PADDING.getName()));
            allocateRow(invRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("InventoryRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find inventory row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new InventoryRow(createNullBitMap(INVENTORY, getRandomNumberStream(INV_NULLS)),
                nativeLong(INV_DATE_SK),
                nativeLong(INV_ITEM_SK),
                nativeLong(INV_WAREHOUSE_SK),
                nativeInt(INV_QUANTITY_ON_HAND)));
    }
}
