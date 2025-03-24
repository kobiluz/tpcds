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
import io.trino.tpcds.row.ShipModeRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.SHIP_MODE;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.ShipModeNativeGeneratorColumn.SM_CARRIER;
import static io.trino.tpcds.column.generator.ShipModeNativeGeneratorColumn.SM_CODE;
import static io.trino.tpcds.column.generator.ShipModeNativeGeneratorColumn.SM_CONTRACT;
import static io.trino.tpcds.column.generator.ShipModeNativeGeneratorColumn.SM_SHIP_MODE_ID;
import static io.trino.tpcds.column.generator.ShipModeNativeGeneratorColumn.SM_SHIP_MODE_SK;
import static io.trino.tpcds.column.generator.ShipModeNativeGeneratorColumn.SM_TYPE;
import static io.trino.tpcds.generator.ShipModeGeneratorColumn.SM_NULLS;

public class ShipModeRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_ship_mode";
    private final StructLayout smRowLayout;

    public ShipModeRowNativeGenerator()
    {
        super(SHIP_MODE);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            smRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(SM_SHIP_MODE_SK.getName()),
                    columnToLayoutMap.get(SM_SHIP_MODE_ID.getName()),
                    columnToLayoutMap.get(SM_TYPE.getName()),
                    columnToLayoutMap.get(SM_CODE.getName()),
                    columnToLayoutMap.get(SM_CARRIER.getName()),
                    columnToLayoutMap.get(SM_CONTRACT.getName()));
            allocateRow(smRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("ShipModeRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find ship mode row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new ShipModeRow(createNullBitMap(SHIP_MODE, getRandomNumberStream(SM_NULLS)),
                nativeLong(SM_SHIP_MODE_SK),
                nativeString(SM_SHIP_MODE_ID),
                nativeString(SM_TYPE),
                nativeString(SM_CODE),
                nativeString(SM_CARRIER),
                nativeString(SM_CONTRACT)));
    }
}
