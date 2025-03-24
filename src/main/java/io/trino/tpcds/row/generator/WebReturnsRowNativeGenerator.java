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
import io.trino.tpcds.row.TableRow;
import io.trino.tpcds.row.WebReturnsRow;
import io.trino.tpcds.type.Decimal;
import io.trino.tpcds.type.Pricing;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.WEB_RETURNS;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_ITEM_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_ORDER_NUMBER;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL1;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL10;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL11;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL12;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL13;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL2;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL3;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL4;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL5;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL6;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL7;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL8;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_DECIMAL9;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_INT1;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_INT2;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_INT3;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_INT4;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_INT5;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_INT6;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_INT7;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_INT8;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PADDING_INT9;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_EXT_SHIP_COST_FLAGS;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_EXT_SHIP_COST_NUMBER;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_EXT_SHIP_COST_PRECISION;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_EXT_SHIP_COST_SCALE;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_EXT_TAX_FLAGS;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_EXT_TAX_NUMBER;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_EXT_TAX_PRECISION;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_EXT_TAX_SCALE;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_FEE_FLAGS;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_FEE_NUMBER;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_FEE_PRECISION;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_FEE_SCALE;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_LOSS_FLAGS;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_LOSS_NUMBER;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_LOSS_PRECISION;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_LOSS_SCALE;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_PAID_FLAGS;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_PAID_INC_TAX_FLAGS;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_PAID_INC_TAX_NUMBER;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_PAID_INC_TAX_PRECISION;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_PAID_INC_TAX_SCALE;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_PAID_NUMBER;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_PAID_PRECISION;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_NET_PAID_SCALE;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_QUANTITY;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_REFUNDED_CASH_FLAGS;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_REFUNDED_CASH_NUMBER;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_REFUNDED_CASH_PRECISION;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_REFUNDED_CASH_SCALE;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_REVERSED_CHARGE_FLAGS;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_REVERSED_CHARGE_NUMBER;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_REVERSED_CHARGE_PRECISION;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_REVERSED_CHARGE_SCALE;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_STORE_CREDIT_FLAGS;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_STORE_CREDIT_NUMBER;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_STORE_CREDIT_PRECISION;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_PRICING_STORE_CREDIT_SCALE;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_REASON_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_REFUNDED_ADDR_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_REFUNDED_CDEMO_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_REFUNDED_CUSTOMER_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_REFUNDED_HDEMO_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_RETURNED_DATE_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_RETURNED_TIME_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_RETURNING_ADDR_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_RETURNING_CDEMO_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_RETURNING_CUSTOMER_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_RETURNING_HDEMO_SK;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_VALID;
import static io.trino.tpcds.column.generator.WebReturnsNativeGeneratorColumn.WR_WEB_PAGE_SK;
import static io.trino.tpcds.generator.WebReturnsGeneratorColumn.WR_NULLS;

public class WebReturnsRowNativeGenerator
        extends AbstractRowGenerator
{
    private final StructLayout wrRowLayout;

    public WebReturnsRowNativeGenerator()
    {
        super(WEB_RETURNS);

        try {
            wrRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(WR_RETURNED_DATE_SK.getName()),
                    columnToLayoutMap.get(WR_RETURNED_TIME_SK.getName()),
                    columnToLayoutMap.get(WR_ITEM_SK.getName()),
                    columnToLayoutMap.get(WR_REFUNDED_CUSTOMER_SK.getName()),
                    columnToLayoutMap.get(WR_REFUNDED_CDEMO_SK.getName()),
                    columnToLayoutMap.get(WR_REFUNDED_HDEMO_SK.getName()),
                    columnToLayoutMap.get(WR_REFUNDED_ADDR_SK.getName()),
                    columnToLayoutMap.get(WR_RETURNING_CUSTOMER_SK.getName()),
                    columnToLayoutMap.get(WR_RETURNING_CDEMO_SK.getName()),
                    columnToLayoutMap.get(WR_RETURNING_HDEMO_SK.getName()),
                    columnToLayoutMap.get(WR_RETURNING_ADDR_SK.getName()),
                    columnToLayoutMap.get(WR_WEB_PAGE_SK.getName()),
                    columnToLayoutMap.get(WR_REASON_SK.getName()),
                    columnToLayoutMap.get(WR_ORDER_NUMBER.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL1.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL2.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL3.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL4.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL5.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL6.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL7.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL8.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL9.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL10.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL11.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL12.getName()),
                    columnToLayoutMap.get(WR_PADDING_DECIMAL13.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_PAID_NUMBER.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_PAID_PRECISION.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_PAID_SCALE.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_PAID_FLAGS.getName()),
                    columnToLayoutMap.get(WR_PADDING_INT1.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_PAID_INC_TAX_NUMBER.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_PAID_INC_TAX_PRECISION.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_PAID_INC_TAX_SCALE.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_PAID_INC_TAX_FLAGS.getName()),
                    columnToLayoutMap.get(WR_PADDING_INT2.getName()),
                    columnToLayoutMap.get(WR_PRICING_EXT_TAX_NUMBER.getName()),
                    columnToLayoutMap.get(WR_PRICING_EXT_TAX_PRECISION.getName()),
                    columnToLayoutMap.get(WR_PRICING_EXT_TAX_SCALE.getName()),
                    columnToLayoutMap.get(WR_PRICING_EXT_TAX_FLAGS.getName()),
                    columnToLayoutMap.get(WR_PADDING_INT3.getName()),
                    columnToLayoutMap.get(WR_PRICING_EXT_SHIP_COST_NUMBER.getName()),
                    columnToLayoutMap.get(WR_PRICING_EXT_SHIP_COST_PRECISION.getName()),
                    columnToLayoutMap.get(WR_PRICING_EXT_SHIP_COST_SCALE.getName()),
                    columnToLayoutMap.get(WR_PRICING_EXT_SHIP_COST_FLAGS.getName()),
                    columnToLayoutMap.get(WR_PADDING_INT4.getName()),
                    columnToLayoutMap.get(WR_PRICING_QUANTITY.getName()),
                    columnToLayoutMap.get(WR_VALID.getName()),
                    columnToLayoutMap.get(WR_PRICING_REFUNDED_CASH_NUMBER.getName()),
                    columnToLayoutMap.get(WR_PRICING_REFUNDED_CASH_PRECISION.getName()),
                    columnToLayoutMap.get(WR_PRICING_REFUNDED_CASH_SCALE.getName()),
                    columnToLayoutMap.get(WR_PRICING_REFUNDED_CASH_FLAGS.getName()),
                    columnToLayoutMap.get(WR_PADDING_INT5.getName()),
                    columnToLayoutMap.get(WR_PRICING_REVERSED_CHARGE_NUMBER.getName()),
                    columnToLayoutMap.get(WR_PRICING_REVERSED_CHARGE_PRECISION.getName()),
                    columnToLayoutMap.get(WR_PRICING_REVERSED_CHARGE_SCALE.getName()),
                    columnToLayoutMap.get(WR_PRICING_REVERSED_CHARGE_FLAGS.getName()),
                    columnToLayoutMap.get(WR_PADDING_INT6.getName()),
                    columnToLayoutMap.get(WR_PRICING_STORE_CREDIT_NUMBER.getName()),
                    columnToLayoutMap.get(WR_PRICING_STORE_CREDIT_PRECISION.getName()),
                    columnToLayoutMap.get(WR_PRICING_STORE_CREDIT_SCALE.getName()),
                    columnToLayoutMap.get(WR_PRICING_STORE_CREDIT_FLAGS.getName()),
                    columnToLayoutMap.get(WR_PADDING_INT7.getName()),
                    columnToLayoutMap.get(WR_PRICING_FEE_NUMBER.getName()),
                    columnToLayoutMap.get(WR_PRICING_FEE_PRECISION.getName()),
                    columnToLayoutMap.get(WR_PRICING_FEE_SCALE.getName()),
                    columnToLayoutMap.get(WR_PRICING_FEE_FLAGS.getName()),
                    columnToLayoutMap.get(WR_PADDING_INT8.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_LOSS_NUMBER.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_LOSS_PRECISION.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_LOSS_SCALE.getName()),
                    columnToLayoutMap.get(WR_PRICING_NET_LOSS_FLAGS.getName()),
                    columnToLayoutMap.get(WR_PADDING_INT9.getName()));
        }
        catch (Throwable t) {
            System.err.println("WebReturnsRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find web returns row generator method", t);
        }
    }

    public StructLayout getLayout()
    {
        return wrRowLayout;
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        // The web returns table is a child of the store_sales table because you can only return things that have already been purchased
        throw new RuntimeException("Generating web returns is not supported as a stand alone");
    }

    public TableRow generateRow(MemorySegment rowSegment)
    {
        this.rowSegment = rowSegment;
        if (nativeInt(WR_VALID) == 0) {
            // no row this round, just reutrn
            return null;
        }

        return new WebReturnsRow(createNullBitMap(WEB_RETURNS, getRandomNumberStream(WR_NULLS)),
                nativeLong(WR_RETURNED_DATE_SK),
                nativeLong(WR_RETURNED_TIME_SK),
                nativeLong(WR_ITEM_SK),
                nativeLong(WR_REFUNDED_CUSTOMER_SK),
                nativeLong(WR_REFUNDED_CDEMO_SK),
                nativeLong(WR_REFUNDED_HDEMO_SK),
                nativeLong(WR_REFUNDED_ADDR_SK),
                nativeLong(WR_RETURNING_CUSTOMER_SK),
                nativeLong(WR_RETURNING_CDEMO_SK),
                nativeLong(WR_RETURNING_HDEMO_SK),
                nativeLong(WR_RETURNING_ADDR_SK),
                nativeLong(WR_WEB_PAGE_SK),
                nativeLong(WR_REASON_SK),
                nativeLong(WR_ORDER_NUMBER),
                new Pricing(new Decimal(nativeDecimal(WR_PRICING_NET_PAID_NUMBER), nativeInt(WR_PRICING_NET_PAID_PRECISION)),
                            new Decimal(nativeDecimal(WR_PRICING_EXT_TAX_NUMBER), nativeInt(WR_PRICING_EXT_TAX_PRECISION)),
                            new Decimal(nativeDecimal(WR_PRICING_NET_PAID_INC_TAX_NUMBER), nativeInt(WR_PRICING_NET_PAID_INC_TAX_PRECISION)),
                            new Decimal(nativeDecimal(WR_PRICING_FEE_NUMBER), nativeInt(WR_PRICING_FEE_PRECISION)),
                            new Decimal(nativeDecimal(WR_PRICING_EXT_SHIP_COST_NUMBER), nativeInt(WR_PRICING_EXT_SHIP_COST_PRECISION)),
                            new Decimal(nativeDecimal(WR_PRICING_REFUNDED_CASH_NUMBER), nativeInt(WR_PRICING_REFUNDED_CASH_PRECISION)),
                            new Decimal(nativeDecimal(WR_PRICING_REVERSED_CHARGE_NUMBER), nativeInt(WR_PRICING_REVERSED_CHARGE_PRECISION)),
                            new Decimal(nativeDecimal(WR_PRICING_STORE_CREDIT_NUMBER), nativeInt(WR_PRICING_STORE_CREDIT_PRECISION)),
                            new Decimal(nativeDecimal(WR_PRICING_NET_LOSS_NUMBER), nativeInt(WR_PRICING_NET_LOSS_PRECISION)),
                            nativeInt(WR_PRICING_QUANTITY)));
    }
}
