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
import io.trino.tpcds.row.PromotionRow;
import io.trino.tpcds.type.Decimal;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.PROMOTION;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.PromotionColumn.P_CHANNEL_CATALOG;
import static io.trino.tpcds.column.PromotionColumn.P_CHANNEL_DEMO;
import static io.trino.tpcds.column.PromotionColumn.P_CHANNEL_DETAILS;
import static io.trino.tpcds.column.PromotionColumn.P_CHANNEL_DMAIL;
import static io.trino.tpcds.column.PromotionColumn.P_CHANNEL_EMAIL;
import static io.trino.tpcds.column.PromotionColumn.P_CHANNEL_EVENT;
import static io.trino.tpcds.column.PromotionColumn.P_CHANNEL_PRESS;
import static io.trino.tpcds.column.PromotionColumn.P_CHANNEL_RADIO;
import static io.trino.tpcds.column.PromotionColumn.P_CHANNEL_TV;
import static io.trino.tpcds.column.PromotionColumn.P_COST_FLAGS;
import static io.trino.tpcds.column.PromotionColumn.P_COST_NUMBER;
import static io.trino.tpcds.column.PromotionColumn.P_COST_PRECISION;
import static io.trino.tpcds.column.PromotionColumn.P_COST_SCALE;
import static io.trino.tpcds.column.PromotionColumn.P_DISCOUNT_ACTIVE;
import static io.trino.tpcds.column.PromotionColumn.P_END_DATE_SK;
import static io.trino.tpcds.column.PromotionColumn.P_ITEM_SK;
import static io.trino.tpcds.column.PromotionColumn.P_PROMO_ID;
import static io.trino.tpcds.column.PromotionColumn.P_PROMO_NAME;
import static io.trino.tpcds.column.PromotionColumn.P_PROMO_SK;
import static io.trino.tpcds.column.PromotionColumn.P_PURPOSE;
import static io.trino.tpcds.column.PromotionColumn.P_RESPONSE_TARGET;
import static io.trino.tpcds.column.PromotionColumn.P_START_DATE_SK;
import static io.trino.tpcds.generator.PromotionGeneratorColumn.P_NULLS;

public class PromotionRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_promotion";
    private final StructLayout pRowLayout;

    public PromotionRowNativeGenerator()
    {
        super(PROMOTION);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            pRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(P_PROMO_SK.getName()),
                    columnToLayoutMap.get(P_PROMO_ID.getName()),
                    columnToLayoutMap.get(P_START_DATE_SK.getName()),
                    columnToLayoutMap.get(P_END_DATE_SK.getName()),
                    columnToLayoutMap.get(P_ITEM_SK.getName()),
                    columnToLayoutMap.get(P_PROMO_NAME.getName()),
                    columnToLayoutMap.get(P_CHANNEL_DETAILS.getName()),
                    columnToLayoutMap.get(P_PURPOSE.getName()),
                    columnToLayoutMap.get(P_RESPONSE_TARGET.getName()),
                    columnToLayoutMap.get(P_CHANNEL_DMAIL.getName()),
                    columnToLayoutMap.get(P_CHANNEL_EMAIL.getName()),
                    columnToLayoutMap.get(P_CHANNEL_CATALOG.getName()),
                    columnToLayoutMap.get(P_CHANNEL_TV.getName()),
                    columnToLayoutMap.get(P_CHANNEL_RADIO.getName()),
                    columnToLayoutMap.get(P_CHANNEL_PRESS.getName()),
                    columnToLayoutMap.get(P_CHANNEL_EVENT.getName()),
                    columnToLayoutMap.get(P_CHANNEL_DEMO.getName()),
                    columnToLayoutMap.get(P_DISCOUNT_ACTIVE.getName()),
                    columnToLayoutMap.get(P_COST_NUMBER.getName()),
                    columnToLayoutMap.get(P_COST_PRECISION.getName()),
                    columnToLayoutMap.get(P_COST_SCALE.getName()),
                    columnToLayoutMap.get(P_COST_FLAGS.getName()));
            allocateRow(pRowLayout.byteSize());
        }
        catch (Throwable t) {
            System.err.println("PromotionRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find promotion row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        generateRow(rowNumber);
        return new RowGeneratorResult(new PromotionRow(createNullBitMap(PROMOTION, getRandomNumberStream(P_NULLS)),
                nativeLong(P_PROMO_SK),
                nativeString(P_PROMO_ID),
                nativeLong(P_START_DATE_SK),
                nativeLong(P_END_DATE_SK),
                nativeLong(P_ITEM_SK),
                new Decimal(nativeDecimal(P_COST_NUMBER), nativeInt(P_COST_PRECISION)),
                nativeInt(P_RESPONSE_TARGET),
                nativeString(P_PROMO_NAME),
                nativeInt(P_CHANNEL_DMAIL) != 0,
                nativeInt(P_CHANNEL_EMAIL) != 0,
                nativeInt(P_CHANNEL_CATALOG) != 0,
                nativeInt(P_CHANNEL_TV) != 0,
                nativeInt(P_CHANNEL_RADIO) != 0,
                nativeInt(P_CHANNEL_PRESS) != 0,
                nativeInt(P_CHANNEL_EVENT) != 0,
                nativeInt(P_CHANNEL_DEMO) != 0,
                nativeString(P_CHANNEL_DETAILS),
                nativeString(P_PURPOSE),
                nativeInt(P_DISCOUNT_ACTIVE) != 0));
    }
}
