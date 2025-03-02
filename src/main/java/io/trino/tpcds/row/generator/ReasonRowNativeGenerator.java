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
import io.trino.tpcds.row.ReasonRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.REASON;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.ReasonColumn.R_REASON_DESC;
import static io.trino.tpcds.column.ReasonColumn.R_REASON_ID;
import static io.trino.tpcds.column.ReasonColumn.R_REASON_SK;
import static io.trino.tpcds.generator.ReasonGeneratorColumn.R_NULLS;

public class ReasonRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_reason";
    private final StructLayout reasonRowLayout;

    public ReasonRowNativeGenerator()
    {
        super(REASON);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            reasonRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(R_REASON_SK.getName()),
                    columnToLayoutMap.get(R_REASON_ID.getName()),
                    columnToLayoutMap.get(R_REASON_DESC.getName()));
            allocateRow(reasonRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("ReasonRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find reason row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new ReasonRow(createNullBitMap(REASON, getRandomNumberStream(R_NULLS)),
                nativeLong(R_REASON_SK),
                nativeString(R_REASON_ID),
                nativeString(R_REASON_DESC)));
    }
}
