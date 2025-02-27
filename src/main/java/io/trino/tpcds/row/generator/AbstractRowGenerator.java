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

import com.google.common.collect.ImmutableMap;
import io.trino.tpcds.Table;
import io.trino.tpcds.column.Column;
import io.trino.tpcds.generator.GeneratorColumn;
import io.trino.tpcds.random.RandomNumberStream;
import io.trino.tpcds.random.RandomNumberStreamImpl;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import static io.trino.tpcds.random.RandomValueGenerator.generateUniformRandomInt;
import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_LONG;

public abstract class AbstractRowGenerator
        implements RowGenerator
{
    private final ImmutableMap<GeneratorColumn, RandomNumberStream> randomNumberStreamMap;
    protected final ImmutableMap<String, Long> columnToOffsetMap;
    protected final ImmutableMap<String, ValueLayout> columnToLayoutMap;
    protected MemorySegment rowSegment;
    protected MethodHandle generateRowMethod;

    public AbstractRowGenerator(Table table)
    {
        ImmutableMap.Builder<GeneratorColumn, RandomNumberStream> randomNumberStreamMapBuilder = ImmutableMap.builder();
        for (GeneratorColumn column : table.getGeneratorColumns()) {
            randomNumberStreamMapBuilder.put(column, new RandomNumberStreamImpl(column.getGlobalColumnNumber(), column.getSeedsPerRow()));
        }
        randomNumberStreamMap = randomNumberStreamMapBuilder.build();

        ImmutableMap.Builder<String, ValueLayout> columnToLayoutMapBuilder = ImmutableMap.builder();
        for (Column column : table.getColumns()) {
            columnToLayoutMapBuilder.put(column.getName(), column.getType().getLayout().withName(column.getName()));
        }
        columnToLayoutMap = columnToLayoutMapBuilder.build();

        ImmutableMap.Builder<String, Long> columnToOffsetMapBuilder = ImmutableMap.builder();
        long offset = 0;
        for (Column column : table.getColumns()) {
            columnToOffsetMapBuilder.put(column.getName(), offset);
            offset += columnToLayoutMap.get(column.getName()).byteSize();
        }
        columnToOffsetMap = columnToOffsetMapBuilder.build();
    }

    @Override
    public void consumeRemainingSeedsForRow()
    {
        for (RandomNumberStream randomNumberStream : randomNumberStreamMap.values()) {
            while (randomNumberStream.getSeedsUsed() < randomNumberStream.getSeedsPerRow()) {
                generateUniformRandomInt(1, 100, randomNumberStream);
            }
            randomNumberStream.resetSeedsUsed();
        }
    }

    public void skipRowsUntilStartingRowNumber(long startingRowNumber)
    {
        for (RandomNumberStream randomNumberStream : randomNumberStreamMap.values()) {
            randomNumberStream.skipRows((int) startingRowNumber - 1);  // casting long to int copies C code
        }
    }

    public RandomNumberStream getRandomNumberStream(GeneratorColumn column)
    {
        return randomNumberStreamMap.get(column);
    }

    protected void allocateRow(long rowSize)
    {
        rowSegment = Arena.ofConfined().allocate(rowSize, JAVA_LONG.byteSize());
    }

    protected void generateRow(long rowNumber)
    {
        try {
            if ((int) generateRowMethod.invokeExact(rowSegment, rowNumber) == 0) {
                return;
            }
            throw new RuntimeException("generate row failed in native");
        }
        catch (Throwable t) {
            throw new RuntimeException("generate row invoke failed " + t);
        }
    }

    protected String nativeString(Column column)
    {
        return rowSegment.get(ADDRESS, columnToOffsetMap.get(column.getName())).reinterpret((long) column.getType().getPrecision().get() + 1).getString(0);
    }

    protected long nativeLong(Column column)
    {
        return rowSegment.get(JAVA_LONG, columnToOffsetMap.get(column.getName()));
    }
}
