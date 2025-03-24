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
import io.trino.tpcds.row.IncomeBandRow;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.INCOME_BAND;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.generator.IncomeBandNativeGeneratorColumn.IB_INCOME_BAND_SK;
import static io.trino.tpcds.column.generator.IncomeBandNativeGeneratorColumn.IB_LOWER_BOUND;
import static io.trino.tpcds.column.generator.IncomeBandNativeGeneratorColumn.IB_PADDING;
import static io.trino.tpcds.column.generator.IncomeBandNativeGeneratorColumn.IB_UPPER_BOUND;
import static io.trino.tpcds.generator.IncomeBandGeneratorColumn.IB_NULLS;

public class IncomeBandRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_income_band";
    private final StructLayout ibRowLayout;

    public IncomeBandRowNativeGenerator()
    {
        super(INCOME_BAND);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            ibRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(IB_INCOME_BAND_SK.getName()),
                    columnToLayoutMap.get(IB_LOWER_BOUND.getName()),
                    columnToLayoutMap.get(IB_UPPER_BOUND.getName()),
                    columnToLayoutMap.get(IB_PADDING.getName()));
            allocateRow(ibRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("IncomeBandRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find income band generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new IncomeBandRow(createNullBitMap(INCOME_BAND, getRandomNumberStream(IB_NULLS)),
                nativeInt(IB_INCOME_BAND_SK),
                nativeInt(IB_LOWER_BOUND),
                nativeInt(IB_UPPER_BOUND)));
    }
}
