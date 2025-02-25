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

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemoryLayout.PathElement;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SequenceLayout;
import java.lang.foreign.StructLayout;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.REASON;
import static io.trino.tpcds.TableGenerator.nativeGeneratorLookup;
import static io.trino.tpcds.column.ReasonColumn.R_REASON_DESC;
import static io.trino.tpcds.column.ReasonColumn.R_REASON_ID;
import static io.trino.tpcds.column.ReasonColumn.R_REASON_SK;
import static io.trino.tpcds.generator.ReasonGeneratorColumn.R_NULLS;
import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_BYTE;
import static java.lang.foreign.ValueLayout.JAVA_INT;
import static java.lang.foreign.ValueLayout.JAVA_LONG;

public class ReasonRowNativeGenerator
        extends AbstractRowGenerator
{
    private final SequenceLayout rReasonIdLayout;
    private final StructLayout rReasonRowLayout;
    private final long rReasonSkOffset;
    private final long rReasonIdOffset;
    private final long rReasonDescriptionOffset;
    private final MethodHandle rReasonMakeRow;

    public ReasonRowNativeGenerator()
    {
        super(REASON);

        try {
            SymbolLookup nativeGeneratorLookup = nativeGeneratorLookup();
            rReasonMakeRow = Linker.nativeLinker().downcallHandle(nativeGeneratorLookup.find("mk_w_reason").orElseThrow(), FunctionDescriptor.of(JAVA_INT, ADDRESS, JAVA_LONG));
            rReasonIdLayout = MemoryLayout.sequenceLayout(R_REASON_ID.getType().getPrecision().get() + JAVA_LONG.byteSize(), JAVA_BYTE);
            rReasonRowLayout = MemoryLayout.structLayout(
                    JAVA_LONG.withName(R_REASON_SK.getName()),
                    rReasonIdLayout.withName(R_REASON_ID.getName()),
                    ADDRESS.withName(R_REASON_DESC.getName()));
            rReasonSkOffset = rReasonRowLayout.byteOffset(PathElement.groupElement(R_REASON_SK.getName()));
            rReasonIdOffset = rReasonRowLayout.byteOffset(PathElement.groupElement(R_REASON_ID.getName()));
            rReasonDescriptionOffset = rReasonRowLayout.byteOffset(PathElement.groupElement(R_REASON_DESC.getName()));
        }
        catch (Throwable t) {
            System.err.println("ReasonRowGenerator failed");
            throw new RuntimeException("failed to find reason row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        try (Arena arena = Arena.ofConfined()) {
            long rReasonDescriptionLength = (long) R_REASON_DESC.getType().getPrecision().get();
            MemorySegment rReasonDescriptionSegment = arena.allocate(rReasonDescriptionLength, JAVA_INT.byteSize());
            MemorySegment rReasonRowSegment = arena.allocate(rReasonRowLayout.byteSize(), JAVA_LONG.byteSize());
            rReasonRowSegment.set(ADDRESS, rReasonDescriptionOffset, rReasonDescriptionSegment);
            int res = (int) rReasonMakeRow.invokeExact(rReasonRowSegment, rowNumber);
            if (res < 0) {
                throw new RuntimeException("make row for reason table failed no error " + res);
            }

            long nullBitMap = createNullBitMap(REASON, getRandomNumberStream(R_NULLS));
            long rReasonSk = rReasonRowSegment.get(JAVA_LONG, rReasonSkOffset);
            String rReasonId = rReasonRowSegment.asSlice(rReasonIdOffset, rReasonIdLayout).getString(0);
            String rReasonDescription = rReasonRowSegment.get(ADDRESS, rReasonDescriptionOffset).reinterpret(rReasonDescriptionLength).getString(0);
            return new RowGeneratorResult(new ReasonRow(nullBitMap, rReasonSk, rReasonId, rReasonDescription));
        }
        catch (Throwable t) {
            throw new RuntimeException("failed to invoke reason row generator method", t);
        }
    }
}
