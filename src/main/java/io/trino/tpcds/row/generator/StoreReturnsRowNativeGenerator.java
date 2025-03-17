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
import io.trino.tpcds.row.StoreReturnsRow;
import io.trino.tpcds.row.TableRow;
import io.trino.tpcds.type.Decimal;
import io.trino.tpcds.type.Pricing;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.StructLayout;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.STORE_RETURNS;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_ADDR_SK;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_CDEMO_SK;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_CUSTOMER_SK;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_HDEMO_SK;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_ITEM_SK;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL1;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL10;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL11;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL12;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL13;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL2;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL3;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL4;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL5;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL6;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL7;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL8;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_DECIMAL9;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_INT1;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_INT2;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_INT3;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_INT4;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_INT5;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_INT6;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_INT7;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_INT8;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PADDING_INT9;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_EXT_SHIP_COST_FLAGS;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_EXT_SHIP_COST_NUMBER;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_EXT_SHIP_COST_PRECISION;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_EXT_SHIP_COST_SCALE;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_EXT_TAX_FLAGS;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_EXT_TAX_NUMBER;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_EXT_TAX_PRECISION;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_EXT_TAX_SCALE;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_FEE_FLAGS;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_FEE_NUMBER;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_FEE_PRECISION;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_FEE_SCALE;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_LOSS_FLAGS;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_LOSS_NUMBER;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_LOSS_PRECISION;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_LOSS_SCALE;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_PAID_FLAGS;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_PAID_INC_TAX_FLAGS;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_PAID_INC_TAX_NUMBER;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_PAID_INC_TAX_PRECISION;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_PAID_INC_TAX_SCALE;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_PAID_NUMBER;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_PAID_PRECISION;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_NET_PAID_SCALE;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_QUANTITY;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_REFUNDED_CASH_FLAGS;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_REFUNDED_CASH_NUMBER;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_REFUNDED_CASH_PRECISION;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_REFUNDED_CASH_SCALE;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_REVERSED_CHARGE_FLAGS;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_REVERSED_CHARGE_NUMBER;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_REVERSED_CHARGE_PRECISION;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_REVERSED_CHARGE_SCALE;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_STORE_CREDIT_FLAGS;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_STORE_CREDIT_NUMBER;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_STORE_CREDIT_PRECISION;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_PRICING_STORE_CREDIT_SCALE;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_REASON_SK;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_RETURNED_DATE_SK;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_RETURNED_TIME_SK;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_STORE_SK;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_TICKET_NUMBER;
import static io.trino.tpcds.column.StoreReturnsColumn.SR_VALID;
import static io.trino.tpcds.generator.StoreReturnsGeneratorColumn.SR_NULLS;

public class StoreReturnsRowNativeGenerator
        extends AbstractRowGenerator
{
    private final StructLayout srRowLayout;

    public StoreReturnsRowNativeGenerator()
    {
        super(STORE_RETURNS);

        try {
            srRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(SR_RETURNED_DATE_SK.getName()),
                    columnToLayoutMap.get(SR_RETURNED_TIME_SK.getName()),
                    columnToLayoutMap.get(SR_ITEM_SK.getName()),
                    columnToLayoutMap.get(SR_CUSTOMER_SK.getName()),
                    columnToLayoutMap.get(SR_CDEMO_SK.getName()),
                    columnToLayoutMap.get(SR_HDEMO_SK.getName()),
                    columnToLayoutMap.get(SR_ADDR_SK.getName()),
                    columnToLayoutMap.get(SR_STORE_SK.getName()),
                    columnToLayoutMap.get(SR_REASON_SK.getName()),
                    columnToLayoutMap.get(SR_TICKET_NUMBER.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL1.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL2.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL3.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL4.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL5.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL6.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL7.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL8.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL9.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL10.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL11.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL12.getName()),
                    columnToLayoutMap.get(SR_PADDING_DECIMAL13.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_PAID_NUMBER.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_PAID_PRECISION.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_PAID_SCALE.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_PAID_FLAGS.getName()),
                    columnToLayoutMap.get(SR_PADDING_INT1.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_PAID_INC_TAX_NUMBER.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_PAID_INC_TAX_PRECISION.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_PAID_INC_TAX_SCALE.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_PAID_INC_TAX_FLAGS.getName()),
                    columnToLayoutMap.get(SR_PADDING_INT2.getName()),
                    columnToLayoutMap.get(SR_PRICING_EXT_TAX_NUMBER.getName()),
                    columnToLayoutMap.get(SR_PRICING_EXT_TAX_PRECISION.getName()),
                    columnToLayoutMap.get(SR_PRICING_EXT_TAX_SCALE.getName()),
                    columnToLayoutMap.get(SR_PRICING_EXT_TAX_FLAGS.getName()),
                    columnToLayoutMap.get(SR_PADDING_INT3.getName()),
                    columnToLayoutMap.get(SR_PRICING_EXT_SHIP_COST_NUMBER.getName()),
                    columnToLayoutMap.get(SR_PRICING_EXT_SHIP_COST_PRECISION.getName()),
                    columnToLayoutMap.get(SR_PRICING_EXT_SHIP_COST_SCALE.getName()),
                    columnToLayoutMap.get(SR_PRICING_EXT_SHIP_COST_FLAGS.getName()),
                    columnToLayoutMap.get(SR_PADDING_INT4.getName()),
                    columnToLayoutMap.get(SR_PRICING_QUANTITY.getName()),
                    columnToLayoutMap.get(SR_VALID.getName()),
                    columnToLayoutMap.get(SR_PRICING_REFUNDED_CASH_NUMBER.getName()),
                    columnToLayoutMap.get(SR_PRICING_REFUNDED_CASH_PRECISION.getName()),
                    columnToLayoutMap.get(SR_PRICING_REFUNDED_CASH_SCALE.getName()),
                    columnToLayoutMap.get(SR_PRICING_REFUNDED_CASH_FLAGS.getName()),
                    columnToLayoutMap.get(SR_PADDING_INT5.getName()),
                    columnToLayoutMap.get(SR_PRICING_REVERSED_CHARGE_NUMBER.getName()),
                    columnToLayoutMap.get(SR_PRICING_REVERSED_CHARGE_PRECISION.getName()),
                    columnToLayoutMap.get(SR_PRICING_REVERSED_CHARGE_SCALE.getName()),
                    columnToLayoutMap.get(SR_PRICING_REVERSED_CHARGE_FLAGS.getName()),
                    columnToLayoutMap.get(SR_PADDING_INT6.getName()),
                    columnToLayoutMap.get(SR_PRICING_STORE_CREDIT_NUMBER.getName()),
                    columnToLayoutMap.get(SR_PRICING_STORE_CREDIT_PRECISION.getName()),
                    columnToLayoutMap.get(SR_PRICING_STORE_CREDIT_SCALE.getName()),
                    columnToLayoutMap.get(SR_PRICING_STORE_CREDIT_FLAGS.getName()),
                    columnToLayoutMap.get(SR_PADDING_INT7.getName()),
                    columnToLayoutMap.get(SR_PRICING_FEE_NUMBER.getName()),
                    columnToLayoutMap.get(SR_PRICING_FEE_PRECISION.getName()),
                    columnToLayoutMap.get(SR_PRICING_FEE_SCALE.getName()),
                    columnToLayoutMap.get(SR_PRICING_FEE_FLAGS.getName()),
                    columnToLayoutMap.get(SR_PADDING_INT8.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_LOSS_NUMBER.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_LOSS_PRECISION.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_LOSS_SCALE.getName()),
                    columnToLayoutMap.get(SR_PRICING_NET_LOSS_FLAGS.getName()),
                    columnToLayoutMap.get(SR_PADDING_INT9.getName()));
        }
        catch (Throwable t) {
            System.err.println("StoreReturnsRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find store returns row generator method", t);
        }
    }

    public StructLayout getLayout()
    {
        return srRowLayout;
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        // The store returns table is a child of the store_sales table because you can only return things that have already been purchased
        throw new RuntimeException("Generating store returns is not supported as a stand alone");
    }

    public TableRow generateRow(MemorySegment rowSegment)
    {
        this.rowSegment = rowSegment;
        if (nativeInt(SR_VALID) == 0) {
            // no row this round, just reutrn
            return null;
        }

        return new StoreReturnsRow(createNullBitMap(STORE_RETURNS, getRandomNumberStream(SR_NULLS)),
                nativeLong(SR_RETURNED_DATE_SK),
                nativeLong(SR_RETURNED_TIME_SK),
                nativeLong(SR_ITEM_SK),
                nativeLong(SR_CUSTOMER_SK),
                nativeLong(SR_CDEMO_SK),
                nativeLong(SR_HDEMO_SK),
                nativeLong(SR_ADDR_SK),
                nativeLong(SR_STORE_SK),
                nativeLong(SR_REASON_SK),
                nativeLong(SR_TICKET_NUMBER),
                new Pricing(new Decimal(nativeDecimal(SR_PRICING_NET_PAID_NUMBER), nativeInt(SR_PRICING_NET_PAID_PRECISION)),
                            new Decimal(nativeDecimal(SR_PRICING_EXT_TAX_NUMBER), nativeInt(SR_PRICING_EXT_TAX_PRECISION)),
                            new Decimal(nativeDecimal(SR_PRICING_NET_PAID_INC_TAX_NUMBER), nativeInt(SR_PRICING_NET_PAID_INC_TAX_PRECISION)),
                            new Decimal(nativeDecimal(SR_PRICING_FEE_NUMBER), nativeInt(SR_PRICING_FEE_PRECISION)),
                            new Decimal(nativeDecimal(SR_PRICING_EXT_SHIP_COST_NUMBER), nativeInt(SR_PRICING_EXT_SHIP_COST_PRECISION)),
                            new Decimal(nativeDecimal(SR_PRICING_REFUNDED_CASH_NUMBER), nativeInt(SR_PRICING_REFUNDED_CASH_PRECISION)),
                            new Decimal(nativeDecimal(SR_PRICING_REVERSED_CHARGE_NUMBER), nativeInt(SR_PRICING_REVERSED_CHARGE_PRECISION)),
                            new Decimal(nativeDecimal(SR_PRICING_STORE_CREDIT_NUMBER), nativeInt(SR_PRICING_STORE_CREDIT_PRECISION)),
                            new Decimal(nativeDecimal(SR_PRICING_NET_LOSS_NUMBER), nativeInt(SR_PRICING_NET_LOSS_PRECISION)),
                            nativeInt(SR_PRICING_QUANTITY)));
    }
}
